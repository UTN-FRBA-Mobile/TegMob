package com.tegMob.view

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.drawable.AnimationDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.tegMob.MainActivity
import com.tegMob.R
import com.tegMob.connectivity.dtos.MatchDTOs
import com.tegMob.connectivity.socket.MatchHandler
import com.tegMob.utils.MyFragment
import com.tegMob.viewModel.MapViewModel
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.map_fragment.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.abs


class MapFragment : MyFragment(), SensorEventListener {
    private lateinit var viewModel: MapViewModel
    private lateinit var dice1: AnimationDrawable
    private lateinit var dice2: AnimationDrawable
    private lateinit var dice3: AnimationDrawable
    private lateinit var dice4: AnimationDrawable
    private lateinit var dice5: AnimationDrawable
    private lateinit var dice6: AnimationDrawable
    private lateinit var countryObjects: Map<String, Map<String, Any>>
    private lateinit var attackResult: JSONObject
    lateinit var currentPlayerColor: String
    private val displayMetrics = DisplayMetrics()
    private var attackerCountry: String? = null
    private var defenderCountry: String? = null
    private var attackInCourse: Boolean = false

    private lateinit var mySensorManager: SensorManager
    private lateinit var mySensor: Sensor
    private var matchId: String = ""
    private var initMapData: String = ""
    private var userId: String = ""

    private lateinit var myColor: String

    private var countriesOwners: MutableMap<ImageView, String?> = mutableMapOf(
        imageArgentina to null,
        imageBrazil to null,
        imageChile to null,
        imageColombia to null,
        imagePeru to null,
        imageUruguay to null,
        imageSahara to null,
        imageZaire to null,
        imageEgypt to null,
        imageEthiopia to null,
        imageSouthafrica to null,
        imageMadagascar to null
    )

    private lateinit var countriesNeighbours: Map<ImageView, List<ImageView>>

    companion object {
        fun newInstance(args: Bundle) = MapFragment().apply {
            arguments = args
            //            println("arguments: "+arguments);
            //            MatchHandler.getSocket()!!.on("START_TURN", onStartTurn)

        }
    }

    override fun getCountryImages() =
        listOf<ImageView>(imageChile, imageBrazil, imageUruguay, imageArgentina, imageColombia, imagePeru, imageSahara, imageZaire, imageMadagascar, imageEthiopia, imageSouthafrica, imageEgypt)

    override fun getCountryNumbers() =
        listOf<TextView>(numberChile, numberBrazil, numberUruguay, numberArgentina, numberColombia, numberPeru, numberSahara, numberZaire, numberMadagascar, numberEthiopia, numberSouthafrica, numberEgypt)

    override fun getCountryTexts() =
        listOf("chile", "brazil", "uruguay", "argentina", "colombia", "peru", "sahara", "zaire", "madagascar", "ethiopia", "southafrica", "egypt")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mySensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager  //accede al servicio de sensores
        mySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        (activity as MainActivity).hideSystemUI()
        //        var lado=resources.getDimensionPixelSize(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (activity?.requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        eventSubscriptions()
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    private fun eventSubscriptions() {
        println("event suscriptions begin")
        MatchHandler.getSocket()!!.on("MAP_CHANGE", onMapChange)
        MatchHandler.getSocket()!!.on("START_TURN", onStartTurn)
        println("event suscriptions fin")
    }

    private fun show(items: List<View>) = items.forEach { it.visibility = View.VISIBLE }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        matchId = arguments!!.getString("matchId").toString()
        initMapData = arguments!!.getString("initMapData").toString()
        userId = arguments!!.getString("userId").toString()
        dice1 = movingDicesAttacker1.background as AnimationDrawable
        dice2 = movingDicesAttacker2.background as AnimationDrawable
        dice3 = movingDicesAttacker3.background as AnimationDrawable
        dice4 = movingDicesDefender1.background as AnimationDrawable
        dice5 = movingDicesDefender2.background as AnimationDrawable
        dice6 = movingDicesDefender3.background as AnimationDrawable

        countriesNeighbours = mapOf(
            imageArgentina to listOf<ImageView>(
                imageUruguay, imageBrazil, imageChile, imagePeru
            ),
            imageChile to listOf<ImageView>(
                imagePeru, imageArgentina, imageMadagascar
            ),
            imagePeru to listOf<ImageView>(
                imageArgentina, imageChile, imageColombia, imageBrazil
            ),
            imageBrazil to listOf<ImageView>(
                imageUruguay, imageArgentina, imagePeru, imageColombia, imageSahara
            ),
            imageUruguay to listOf<ImageView>(
                imageBrazil, imageArgentina
            ),
            imageColombia to listOf<ImageView>(
                imagePeru, imageBrazil, imageEgypt
            ),
            imageSahara to listOf<ImageView>(
                imageBrazil, imageEgypt, imageEthiopia, imageZaire
            ),
            imageZaire to listOf<ImageView>(
                imageSahara, imageSouthafrica, imageEthiopia
            ),
            imageEthiopia to listOf<ImageView>(
                imageSahara, imageZaire, imageSouthafrica, imageEgypt
            ),
            imageEgypt to listOf<ImageView>(
                imageColombia, imageSahara, imageEthiopia, imageMadagascar
            ),
            imageSouthafrica to listOf<ImageView>(
                imageZaire, imageEthiopia, imageMadagascar
            ),
            imageMadagascar to listOf<ImageView>(
                imageChile, imageZaire, imageEgypt, imageSouthafrica
            )
        )

        countryObjects = mapOf(
            "colombia" to mapOf(
                "image" to imageColombia,
                "number" to numberColombia
            ),
            "peru" to mapOf(
                "image" to imagePeru,
                "number" to numberPeru
            ),
            "argentina" to mapOf(
                "image" to imageArgentina,
                "number" to numberArgentina
            ),
            "brazil" to mapOf(
                "image" to imageBrazil,
                "number" to numberBrazil
            ),
            "chile" to mapOf(
                "image" to imageChile,
                "number" to numberChile
            ),
            "uruguay" to mapOf(
                "image" to imageUruguay,
                "number" to numberUruguay
            ),
            "zaire" to mapOf(
                "image" to imageZaire,
                "number" to numberZaire
            ),
            "sahara" to mapOf(
                "image" to imageSahara,
                "number" to numberSahara
            ),
            "egypt" to mapOf(
                "image" to imageEgypt,
                "number" to numberEgypt
            ),
            "southafrica" to mapOf(
                "image" to imageSouthafrica,
                "number" to numberSouthafrica
            ),
            "madagascar" to mapOf(
                "image" to imageMadagascar,
                "number" to numberMadagascar
            ),
            "ethiopia" to mapOf(
                "image" to imageEthiopia,
                "number" to numberEthiopia
            )


        )

        initViewModel()
        view.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (currentPlayerColor == myColor)
                    screenTouched(v, event)
            }
            true
        }

        btnAttack.setOnClickListener {
            initAttack()
        }

        btnAccept.setOnClickListener {
            acceptAttackResult()
        }

        locationIcon.setOnClickListener() {
            if (countryNames.visibility == View.VISIBLE)
                countryNames.visibility = View.INVISIBLE
            else
                countryNames.visibility = View.VISIBLE
        }

        //        changePlayerIcon.setOnClickListener() {
        //            if (currentPlayerColor == myColor) {
        //                changeTurn()
        //            }
        //        }

        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        viewModel.run { Map(view, displayMetrics, initMapData(initMapData)) }
    }

    private fun initAttack() {
        turnOffDicesSensor()
        attackInCourse = true

        val attackerArmies = countryObjects[attackerCountry]?.get("number") as TextView
        val attackerArmiesNumber = Integer.parseInt(attackerArmies.text.toString())
        val defenderArmies = countryObjects[defenderCountry]?.get("number") as TextView
        val defenderArmiesNumber = Integer.parseInt(defenderArmies.text.toString())

        showDices(attackerArmiesNumber, defenderArmiesNumber)

        btnAttack.visibility = View.INVISIBLE
        //        btnStop.visibility = View.VISIBLE

        //TODO TEST THIS
        Log.i(
            "try_attack",
            Gson().toJson(
                MatchDTOs.MatchTryAttack(
                    attacker = attackerCountry!!,
                    defender = defenderCountry!!,
                    id_match = matchId
                )
            )
        )

        MatchHandler.getSocket()?.emit("TRY_ATTACK", attackerCountry!!, defenderCountry!!, matchId)

    }


    /**
     * cambia el turno del jugador
     */
    //    private fun changeTurn() {
    //        when (currentPlayerColor) {
    //            "cyan" -> {
    //                currentPlayerColor = "magenta"
    //            }
    //            "magenta" -> {
    //                currentPlayerColor = "red"
    //            }
    //            "red" -> {
    //                currentPlayerColor = "black"
    //            }
    //            "black" -> {
    //                currentPlayerColor = "yellow"
    //            }
    //            "yellow" -> {
    //                currentPlayerColor = "green"
    //            }
    //            "green" -> {
    //                currentPlayerColor = "cyan"
    //            }
    //        }
    //        viewModel.setCurrentPlayerText(currentPlayerColor)
    ////        changePlayerIcon.visibility = View.INVISIBLE
    ////        MatchHandler.changeTurn(currentPlayerColor,matchId)
    //
    //    }

    override fun initViewModel() {
        viewModel = MapViewModel()
        context?.let { viewModel.init(this, listener, it) }
    }


    /**
     * muestra el resultado de los dados y setea la variable global con el resultado
     * queda mostrando los dados hasta que se toca el boton "aceptar resultado"
     * resultAttack:
     * {
     *  "attacker_id":"5eea7cbfa6d2090f2c96c192",
     *  "defender_id":"5eea7cf2a6d2090f2c96c193",
     *  "attack_result":{
     *       "dados":{
     *           "attacker":[5,5,4],
     *           "defender":[4,3,2]
     *       },
     *       "map_change":{
     *           "argentina":{
     *               "owner":"yellow","armies":1
     *          },
     *           "brazil":{
     *               "owner":"yellow","armies":4
     *           }
     *       },
     *       "message":"Jugador yellow a ocupado brazil"
     *       }
     * }
     */
    private fun showDicesResult(resultAttack: JSONObject) {
        //        val resultAttackString = getAttackResults()   //datos que van a venir del backend
        //        val resultAttack = JSONObject(resultAttackString)

        val attackerDicesArray = resultAttack.getJSONObject("attack_result").getJSONObject("dados").getJSONArray("attacker")
        val defenderDicesArray = resultAttack.getJSONObject("attack_result").getJSONObject("dados").getJSONArray("defender")
        val movingDicesImages = listOf(
            movingDicesAttacker1, movingDicesAttacker2, movingDicesAttacker3,
            movingDicesDefender1, movingDicesDefender2, movingDicesDefender3
        )
        val resultDicesAttacker = listOf(resultDicesAttacker1, resultDicesAttacker2, resultDicesAttacker3)
        val resultDicesDefender = listOf(resultDicesDefender1, resultDicesDefender2, resultDicesDefender3)


        attackResult = resultAttack.getJSONObject("attack_result")

        hide(movingDicesImages)

        setDicesImagesSource(resultDicesAttacker, attackerDicesArray)
        setDicesImagesSource(resultDicesDefender, defenderDicesArray)

        movingDicesImages.forEach { img -> (img.background as AnimationDrawable).stop() }

        btnAccept.visibility = View.VISIBLE
    }

    private fun setDicesImagesSource(resultDices: List<ImageView>, dicesArray: JSONArray) {
        val dicesImages = listOf(null, R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3, R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6)
        val resultDicesToShow = resultDices.take(dicesArray.length()).zip(listOf(0, 1, 2).take(dicesArray.length()))

        resultDicesToShow.forEach { (resultDice, index) ->
            resultDice.setImageResource(dicesImages[Integer.parseInt(dicesArray.get(index).toString())]!!)
            resultDice.visibility = View.VISIBLE
        }
    }

    /**
     * oculta los dados y cambia los datos en el mapa
     * colores y cantidad de ejércitos en los países
     */
    private fun acceptAttackResult() {
        resetAttack()
        hide(
            listOf(
                resultDicesAttacker1, resultDicesAttacker2, resultDicesAttacker3,
                resultDicesDefender1, resultDicesDefender2, resultDicesDefender3
            )
        )

        val mapChange = attackResult.getJSONObject("map_change")
        val countries = getCountryImages().zip(getCountryTexts())
        val affectedCountries = countries.filter { (_, c) -> c == attackerCountry!! || c == defenderCountry!! }

        affectedCountries.forEach { (countryImage, countryName) ->
            //            val countryImage = countryObjects[countryName]!!["image"] as ImageView

            val countryNumber = countryObjects[countryName]!!["number"] as TextView
            countryNumber.setText(mapChange.getJSONObject(countryName).getString("armies"))

            countryImage.setColorFilter(viewModel.playerColors[mapChange.getJSONObject(countryName).getString("owner")]!!)
            countriesOwners[countryImage] = mapChange.getJSONObject(countryName).getString("owner")
        }

    }

    /**
     * determina la cantidad de dados a mostrar para cada jugador e inicia las animaciones
     */
    private fun showDices(attackerArmiesNumber: Int, defenderArmiesNumber: Int) {
        if (attackerArmiesNumber >= 2) {
            dice1.start()
            movingDicesAttacker1.visibility = View.VISIBLE
        }
        if (attackerArmiesNumber >= 3) {
            dice2.start()
            movingDicesAttacker2.visibility = View.VISIBLE
        }
        if (attackerArmiesNumber >= 4) {
            dice3.start()
            movingDicesAttacker3.visibility = View.VISIBLE
        }

        if (defenderArmiesNumber >= 1) {
            dice4.start()
            movingDicesDefender1.visibility = View.VISIBLE
        }
        if (defenderArmiesNumber >= 2) {
            dice5.start()
            movingDicesDefender2.visibility = View.VISIBLE
        }
        if (defenderArmiesNumber >= 3) {
            dice6.start()
            movingDicesDefender3.visibility = View.VISIBLE
        }
    }

    //TODO test this logic
    private val onMapChange = Emitter.Listener {
        println("on map change: "+it[0].toString())
        showDicesResult(JSONObject(it[0].toString()))

        //        val attackResult = it[0].toString()
        //        val jsonObjData = JSONObject(attackResult)
        //        val affectedCountriesData = jsonObjData.getJSONObject("attack_result").getJSONObject("map_change")
        //        val countries = getCountryImages().zip(getCountryTexts())
        //        val affectedCountries = countries.filter { (_, c) -> c == attackerCountry!! || c == defenderCountry!! }

        //        affectedCountries.forEach { (img, country) ->
        //            countriesOwners[img] = affectedCountriesData.getJSONObject(country).getString("owner")
        //        }
        //TODO ver que tengo que mostrar en los dados
        //if (jsonObjData.getJSONObject("attacker_id"))
    }

    private val onStartTurn = Emitter.Listener {
        //        val stringData = it[0].toString()
        //        val jsonObjData = JSONObject(stringData)
        //        currentPlayerColor = jsonObjData.getString("turno")
        currentPlayerColor = it[0].toString()
        println("turno actual: " + currentPlayerColor)
        //        changePlayerIcon.visibility = View.VISIBLE
    }

    private fun hide(items: List<View>) = items.forEach { it.visibility = View.INVISIBLE }

    /**
     * resetea los parámetros y elementos que se usan para los ataques para poder usarlos en un ataque futuro
     */
    private fun resetAttack() {
        turnOffDicesSensor()
        attackInCourse = false
        attackerCountry = null
        defenderCountry = null
        val attackItems = listOf(attacker, defender, attackTitle, movingDicesAttacker1, movingDicesAttacker2, movingDicesAttacker3, movingDicesDefender1, movingDicesDefender2, movingDicesDefender3, btnAccept, btnAttack, btnStop)
        hide(attackItems)
    }

    /**
     * define las acciones al tocar la pantalla
     */
    private fun screenTouched(view: View, event: MotionEvent): Boolean {
        (activity as MainActivity).hideSystemUI()

        turnOffDicesSensor()
        if (attackInCourse)    //si hay una ataque en curso no se deja hacer nada al tocar la pantalla
            return false

        val errorMessages = listOf(errorNoOwnCountry, errorNoNeighbourCountry, errorAttackingOwnCountry, errorNotEnoughArmies)
        hide(errorMessages)

        val touchedCountryName = viewModel.getCountryImageTouched(view, event)  //puede ser nulo si toco el mar o los bordes de los países

        if (touchedCountryName == null) {
            resetAttack()
            return false
        }
        val touchedCountry = countryObjects[touchedCountryName]!!["image"] as ImageView
        val attackerCountryLocal = attackerCountry  //variable local para evitar error: Smart cast to 'Type' is impossible, because 'variable' is a mutable property that could have been changed by this time
        if (attackerCountryLocal === null) {    //elige el país desde el cual ataca
            if (countriesOwners[touchedCountry] != currentPlayerColor) {
                errorNoOwnCountry.visibility = View.VISIBLE
                return false
            }
            val attackerArmies = countryObjects[touchedCountryName]?.get("number") as TextView
            if (Integer.parseInt(attackerArmies.text.toString()) <= 1) {
                errorNotEnoughArmies.visibility = View.VISIBLE
                return false
            }
            attackerCountry = touchedCountryName
            attackTitle.visibility = View.VISIBLE
            attacker.text = touchedCountry?.contentDescription.toString()
            attacker.visibility = View.VISIBLE
        } else if (defenderCountry === null) {
            if (countriesOwners.get(touchedCountry) == currentPlayerColor) {
                errorAttackingOwnCountry.visibility = View.VISIBLE
                return false
            }
            if (!(countriesNeighbours[countryObjects[attackerCountry]!!["image"] as ImageView]!!.contains(touchedCountry))) {
                errorNoNeighbourCountry.visibility = View.VISIBLE
                return false
            }

            defenderCountry = touchedCountryName
            defender.text = touchedCountry?.contentDescription.toString()
            defender.visibility = View.VISIBLE
            btnAttack.visibility = View.VISIBLE
            turnOnDicesSensor()
        }

        return true
    }

    /**
     * Busca la data inicial de los países, jugadores que posee y cantidad de ejércitos de cada uno de ellos
     */
    private fun initMapData(receivedData: String): JSONObject {
        //        val receivedObj = JSONObject(receivedData)
        val jsonObjData = JSONObject(receivedData)
        val countriesData = jsonObjData.getJSONObject("countries")

        getCountryImages().zip(getCountryTexts()).forEach { (img, country) ->
            countriesOwners[img] = countriesData.getJSONObject(country).getString("owner")
        }

        currentPlayerColor = jsonObjData.getString("currentPlayerColor")
        viewModel.setCurrentPlayerText(currentPlayerColor)
        val colorPlayers = jsonObjData.getJSONArray("players")
        /*
        [{"color":"green","user":"5ee6ab47a6d2090f2c96c18f","armies":0},{"color":"yellow","user":"5ef8f9fdb3f6eedd0aaf7037","armies":0}]
         */
        println("colorPlayers: " + colorPlayers)
        println("userId: " + userId)
        for (i in 0 until colorPlayers.length()) {
            val player = colorPlayers.getJSONObject(i)

            if (userId == player.getString("user")) {
                myColor = player.getString("color")
                viewModel.myColor = myColor
            }
            // Your code here
        }

        return jsonObjData
    }


    override fun getPassedData() {
        TODO("Not yet implemented")
    }

    /****************
     * ACCELEROMETER
     ****************/

    val alpha: Float = 0.1F;
    var gravity: FloatArray = floatArrayOf(0F, 0F, 0F)
    var linearAcceleration: FloatArray = floatArrayOf(0F, 0F, 0F)
    var sensorReadingsQuant: Int = 0

    override fun onSensorChanged(event: SensorEvent?) {
        gravity[0] = alpha * gravity[0] + (1F - alpha) * event!!.values[0];
        gravity[1] = alpha * gravity[1] + (1F - alpha) * event!!.values[1];
        gravity[2] = alpha * gravity[2] + (1F - alpha) * event!!.values[2];

        linearAcceleration[0] = event!!.values[0] - gravity[0];
        linearAcceleration[1] = event!!.values[1] - gravity[1];
        linearAcceleration[2] = event!!.values[2] - gravity[2];

        val accelSensitivity = 1F
        sensorReadingsQuant++
        if (sensorReadingsQuant > 10 && (abs(linearAcceleration[0]) > accelSensitivity || abs(linearAcceleration[1]) > accelSensitivity || abs(linearAcceleration[2]) > accelSensitivity))
            initAttack()
    }

    fun turnOffDicesSensor() {
        mySensorManager.unregisterListener(this)
        sensorReadingsQuant = 0
    }

    fun turnOnDicesSensor() {
        mySensorManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL) //habilita el acelerómetro para lanzar los dados
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}
