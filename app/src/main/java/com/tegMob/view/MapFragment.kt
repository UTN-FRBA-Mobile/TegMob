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
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tegMob.R
import com.tegMob.utils.MyFragment
import com.tegMob.viewModel.MapViewModel
import kotlinx.android.synthetic.main.map_fragment.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.abs
import kotlin.random.Random


class MapFragment : MyFragment(), SensorEventListener {
    private lateinit var viewModel: MapViewModel
    private lateinit var initMapData: JSONObject
    private lateinit var dice1: AnimationDrawable
    private lateinit var dice2: AnimationDrawable
    private lateinit var dice3: AnimationDrawable
    private lateinit var dice4: AnimationDrawable
    private lateinit var dice5: AnimationDrawable
    private lateinit var dice6: AnimationDrawable
    private lateinit var countryObjects: Map<String, Map<String, Any>>
    private lateinit var countriesStateArray: JSONArray
    lateinit var currentPlayer: String
    private var windowHeight: Int = 0
    private var windowWidth: Int = 0
    private val displayMetrics = DisplayMetrics()
    private var attackerCountry: String? = null
    private var defenderCountry: String? = null
    private var attackInCourse: Boolean = false

    private lateinit var mySensorManager: SensorManager
    private lateinit var mySensor: Sensor


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
        fun newInstance() = MapFragment().apply {
            arguments = Bundle()
        }
    }


    override fun getCountryImages() =
        listOf<ImageView>(imageChile, imageBrazil, imageUruguay, imageArgentina, imageColombia, imagePeru, imageSahara, imageZaire, imageMadagascar, imageEthiopia, imageSouthafrica, imageEgypt)

    override fun getCountryNumbers() =
        listOf<TextView>(numberChile, numberBrazil, numberUruguay, numberArgentina, numberColombia, numberPeru, numberSahara, numberZaire, numberMadagascar, numberEthiopia, numberSouthafrica, numberEgypt)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mySensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager  //accede al servicio de sensores
        mySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        println("Sensor: " + Sensor.TYPE_ACCELEROMETER)
        //        var lado=resources.getDimensionPixelSize(view)
    }

    /* ************ acelerómetro ******************/
    //    override fun onResume() {
    //        super.onResume()
    //        mySensorManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_UI)
    //    }
    //
    //    override fun onPause() {
    //        super.onPause()
    //        mySensorManager.unregisterListener(this)
    //    }

    val alpha: Float = 0.1F;
    var gravity: FloatArray = floatArrayOf(0F, 0F, 0F)
    var linearAcceleration: FloatArray = floatArrayOf(0F, 0F, 0F)
    var sensorReadingsQuant: Int = 0

    override fun onSensorChanged(event: SensorEvent?) {
        //        viewModel.sensorMovements(event!!.values)

        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        //        println("values 0: " + event!!.values[0])
        //        println("values 1: " + event!!.values[1])
        //        println("values 2: " + event!!.values[2])

        gravity[0] = alpha * gravity[0] + (1F - alpha) * event!!.values[0];
        gravity[1] = alpha * gravity[1] + (1F - alpha) * event!!.values[1];
        gravity[2] = alpha * gravity[2] + (1F - alpha) * event!!.values[2];

        //        println("gravity 0: " + gravity[0])
        //        println("gravity 1: " + gravity[1])
        //        println("gravity 2: " + gravity[2])

        linearAcceleration[0] = event!!.values[0] - gravity[0];
        linearAcceleration[1] = event!!.values[1] - gravity[1];
        linearAcceleration[2] = event!!.values[2] - gravity[2];

        println("accel 0: " + linearAcceleration[0])
        println("accel 1: " + linearAcceleration[1])
        println("accel 2: " + linearAcceleration[2])
        val accelSensitivity = 1F
        sensorReadingsQuant++
        if (sensorReadingsQuant > 10 && (abs(linearAcceleration[0]) > accelSensitivity || abs(linearAcceleration[1]) > accelSensitivity || abs(linearAcceleration[2]) > accelSensitivity))
        //            println("moví el teléfono " + sensorReadingsQuant)
            startMovingDices()
    }

    fun turnOffDicesSensor() {
        mySensorManager.unregisterListener(this)
        sensorReadingsQuant = 0
    }

    fun turnOnDicesSensor() {
        mySensorManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL) //habilita el acelerómetro para lanzar los dados
    }
    /* ************ acelerómetro ******************/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (activity?.requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    fun startMovingDices() {
        turnOffDicesSensor()
        attackInCourse = true
        val attackerArmies = countryObjects[attackerCountry]?.get("number") as TextView
        val attackerArmiesNumber = Integer.parseInt(attackerArmies.text.toString())
        var defenderArmies = countryObjects[defenderCountry]?.get("number") as TextView
        val defenderArmiesNumber = Integer.parseInt(defenderArmies.text.toString())

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

        btnAttack.visibility = View.INVISIBLE
        btnStop.visibility = View.VISIBLE
    }

    fun showDicesResult() {
        val resultAttackString = getAttackResults()   //datos que van a venir del backend
        val resultAttack = JSONObject(resultAttackString)
        val attackerDicesArray = resultAttack.getJSONArray("attackerDices")
        val defenderDicesArray = resultAttack.getJSONArray("defenderDices")
        countriesStateArray = resultAttack.getJSONArray("countries")

        val dicesImages = listOf(null, R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3, R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6)
        movingDicesAttacker1.visibility = View.INVISIBLE
        movingDicesAttacker2.visibility = View.INVISIBLE
        movingDicesAttacker3.visibility = View.INVISIBLE
        movingDicesDefender1.visibility = View.INVISIBLE
        movingDicesDefender2.visibility = View.INVISIBLE
        movingDicesDefender3.visibility = View.INVISIBLE
        when (attackerDicesArray.length()) {
            1 -> {
                resultDicesAttacker1.setImageResource(dicesImages[Integer.parseInt(attackerDicesArray.get(0).toString())]!!)
                resultDicesAttacker1.visibility = View.VISIBLE
            }
            2 -> {
                resultDicesAttacker1.setImageResource(dicesImages[Integer.parseInt(attackerDicesArray.get(0).toString())]!!)
                resultDicesAttacker2.setImageResource(dicesImages[Integer.parseInt(attackerDicesArray.get(1).toString())]!!)
                resultDicesAttacker1.visibility = View.VISIBLE
                resultDicesAttacker2.visibility = View.VISIBLE
            }
            3 -> {
                resultDicesAttacker1.setImageResource(dicesImages[Integer.parseInt(attackerDicesArray.get(0).toString())]!!)
                resultDicesAttacker2.setImageResource(dicesImages[Integer.parseInt(attackerDicesArray.get(1).toString())]!!)
                resultDicesAttacker3.setImageResource(dicesImages[Integer.parseInt(attackerDicesArray.get(2).toString())]!!)
                resultDicesAttacker1.visibility = View.VISIBLE
                resultDicesAttacker2.visibility = View.VISIBLE
                resultDicesAttacker3.visibility = View.VISIBLE
            }
        }
        when (defenderDicesArray.length()) {
            1 -> {
                resultDicesDefender1.setImageResource(dicesImages[Integer.parseInt(defenderDicesArray.get(0).toString())]!!)
                resultDicesDefender1.visibility = View.VISIBLE
            }
            2 -> {
                resultDicesDefender1.setImageResource(dicesImages[Integer.parseInt(defenderDicesArray.get(0).toString())]!!)
                resultDicesDefender2.setImageResource(dicesImages[Integer.parseInt(defenderDicesArray.get(1).toString())]!!)
                resultDicesDefender1.visibility = View.VISIBLE
                resultDicesDefender2.visibility = View.VISIBLE
            }
            3 -> {
                resultDicesDefender1.setImageResource(dicesImages[Integer.parseInt(defenderDicesArray.get(0).toString())]!!)
                resultDicesDefender2.setImageResource(dicesImages[Integer.parseInt(defenderDicesArray.get(1).toString())]!!)
                resultDicesDefender3.setImageResource(dicesImages[Integer.parseInt(defenderDicesArray.get(2).toString())]!!)
                resultDicesDefender1.visibility = View.VISIBLE
                resultDicesDefender2.visibility = View.VISIBLE
                resultDicesDefender3.visibility = View.VISIBLE
            }
        }

        val dice1 = movingDicesAttacker1.background as AnimationDrawable
        dice1.stop()
        val dice2 = movingDicesAttacker2.background as AnimationDrawable
        dice2.stop()
        val dice3 = movingDicesAttacker3.background as AnimationDrawable
        dice3.stop()
        val dice4 = movingDicesDefender1.background as AnimationDrawable
        dice4.stop()
        val dice5 = movingDicesDefender2.background as AnimationDrawable
        dice5.stop()
        val dice6 = movingDicesDefender3.background as AnimationDrawable
        dice6.stop()
        btnAccept.visibility = View.VISIBLE
        btnStop.visibility = View.INVISIBLE
    }

    fun acceptAttackResult(){
        resetAttack()
        val updateData = JSONObject(mockupCountriesDataUpdate())
        //            val updateData = JSONObject(mockupCountriesDataUpdate())
        //            val countriesData = updateData.getJSONArray("countries")
        val countriesData = countriesStateArray
        resultDicesAttacker1.visibility = View.INVISIBLE
        resultDicesAttacker2.visibility = View.INVISIBLE
        resultDicesAttacker3.visibility = View.INVISIBLE
        resultDicesDefender1.visibility = View.INVISIBLE
        resultDicesDefender2.visibility = View.INVISIBLE
        resultDicesDefender3.visibility = View.INVISIBLE

        for (i in 0 until countriesData.length()) {
            val item = countriesData.getJSONObject(i)
            val countryImage = countryObjects[item.getString("country")]!!["image"] as ImageView
            val countryNumber = countryObjects[item.getString("country")]!!["number"] as TextView
            countryImage.setColorFilter(viewModel.playerColors[item.getString("owner")]!!)
            countryNumber.setText(item.getString("armies"))
            countriesOwners[countryImage] = item.getString("owner")
        }
        //            viewModel.updateData(updateData.getJSONArray("countries"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        initMapData = initMapData()

        view.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                screenTouched(v, event)
            }
            true
        }

        btnAttack.setOnClickListener {
            startMovingDices()
        }

        /*
        reemplazar el listener del botón por el socket que manda el resultado del ataque
         */
        btnStop.setOnClickListener {
            showDicesResult()
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

        changePlayerIcon.setOnClickListener() {
            when (currentPlayer) {
                "cyan" -> {
                    currentPlayer = "magenta"
                }
                "magenta" -> {
                    currentPlayer = "red"
                }
                "red" -> {
                    currentPlayer = "black"
                }
                "black" -> {
                    currentPlayer = "yellow"
                }
                "yellow" -> {
                    currentPlayer = "green"
                }
                "green" -> {
                    currentPlayer = "cyan"
                }
            }
            viewModel.setCurrentPlayerText(currentPlayer)
        }

        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        windowWidth = displayMetrics.widthPixels
        windowHeight = displayMetrics.heightPixels
        //        println("ancho: " + windowWidth)
        //        println("alto: " + windowHeight)
        //        println(windowWidth.toFloat() / windowHeight.toFloat())
        viewModel.run { Map(view, windowWidth, windowHeight, initMapData) }
    }

    /**
     * resetea los parámetros y elementos que se usan para los ataques para poder usarlos en un ataque futuro
     */
    private fun resetAttack() {
        turnOffDicesSensor()
        attackInCourse = false
        attackerCountry = null
        defenderCountry = null
        attacker.visibility = View.INVISIBLE
        defender.visibility = View.INVISIBLE
        attackTitle.visibility = View.INVISIBLE
        //        movingDicesAttacker.visibility = View.INVISIBLE
        //        movingDicesDefender.visibility = View.INVISIBLE
        movingDicesAttacker1.visibility = View.INVISIBLE
        movingDicesAttacker2.visibility = View.INVISIBLE
        movingDicesAttacker3.visibility = View.INVISIBLE
        movingDicesDefender1.visibility = View.INVISIBLE
        movingDicesDefender2.visibility = View.INVISIBLE
        movingDicesDefender3.visibility = View.INVISIBLE
        btnAccept.visibility = View.INVISIBLE
        btnAttack.visibility = View.INVISIBLE
        btnStop.visibility = View.INVISIBLE
    }

    /**
     * define las acciones al tocar la pantalla
     */
    private fun screenTouched(view: View, event: MotionEvent): Boolean {
        turnOffDicesSensor()
        if (attackInCourse)    //si hay una ataque en curso no se deja hacer nada al tocar la pantalla
            return false

        errorNoOwnCountry.visibility = View.INVISIBLE
        errorNoNeighbourCountry.visibility = View.INVISIBLE
        errorAttackingOwnCountry.visibility = View.INVISIBLE
        errorNotEnoughArmies.visibility = View.INVISIBLE

        val touchedCountryName = viewModel.getCountryImageTouched(view, event)  //puede ser nulo si toco el mar o los bordes de los países

        if (touchedCountryName == null) {
            resetAttack()
            return false
        }
        val touchedCountry = countryObjects[touchedCountryName]!!["image"] as ImageView
        val attackerCountryLocal = attackerCountry  //variable local para evitar error: Smart cast to 'Type' is impossible, because 'variable' is a mutable property that could have been changed by this time
        if (attackerCountryLocal === null) {    //elige el país desde el cual ataca
            if (countriesOwners[touchedCountry] != currentPlayer) {
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
            if (countriesOwners.get(touchedCountry) == currentPlayer) {
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
    private fun initMapData(): JSONObject {
        //llama al servicio del backend
        val receivedData: String = mockupDataInit()
        //        val receivedObj = JSONObject(receivedData)
        val jsonObjData = JSONObject(receivedData)
        val countriesData = jsonObjData.getJSONObject("countries")

        countriesOwners.set(imageArgentina, countriesData.getJSONObject("argentina").getString("owner"))
        countriesOwners.set(imageBrazil, countriesData.getJSONObject("brazil").getString("owner"))
        countriesOwners.set(imageChile, countriesData.getJSONObject("chile").getString("owner"))
        countriesOwners.set(imageColombia, countriesData.getJSONObject("colombia").getString("owner"))
        countriesOwners.set(imagePeru, countriesData.getJSONObject("peru").getString("owner"))
        countriesOwners.set(imageUruguay, countriesData.getJSONObject("uruguay").getString("owner"))
        countriesOwners.set(imageSahara, countriesData.getJSONObject("sahara").getString("owner"))
        countriesOwners.set(imageZaire, countriesData.getJSONObject("zaire").getString("owner"))
        countriesOwners.set(imageSouthafrica, countriesData.getJSONObject("southafrica").getString("owner"))
        countriesOwners.set(imageEthiopia, countriesData.getJSONObject("ethiopia").getString("owner"))
        countriesOwners.set(imageEgypt, countriesData.getJSONObject("egypt").getString("owner"))
        countriesOwners.set(imageMadagascar, countriesData.getJSONObject("madagascar").getString("owner"))

        currentPlayer = jsonObjData.getString("currentPlayer")

        return jsonObjData
    }


    override fun getPassedData() {
        TODO("Not yet implemented")
    }

    override fun initViewModel() {
        viewModel = MapViewModel()
        context?.let { viewModel.init(this, listener, it) }
    }

    /**
     * simulador de backend
     */
    private fun mockupDataInit(): String {
        return "{" +
                "\"countries\":" +
                "{" +
                "\"brazil\":{\"owner\":\"cyan\",\"armies\": \"5\"}," +
                "\"colombia\":{\"owner\":\"magenta\",\"armies\": \"6\"}," +
                "\"chile\":{\"owner\":\"black\",\"armies\": \"1\"}," +
                "\"peru\":{\"owner\":\"green\",\"armies\": \"3\"}," +
                "\"argentina\":{\"owner\":\"cyan\",\"armies\": \"3\"}," +
                "\"uruguay\":{\"owner\":\"red\",\"armies\": \"6\"}," +
                "\"egypt\":{\"owner\":\"cyan\",\"armies\": \"1\"}," +
                "\"ethiopia\":{\"owner\":\"yellow\",\"armies\": \"2\"}," +
                "\"zaire\":{\"owner\":\"black\",\"armies\": \"3\"}," +
                "\"madagascar\":{\"owner\":\"yellow\",\"armies\": \"5\"}," +
                "\"southafrica\":{\"owner\":\"cyan\",\"armies\": \"2\"}," +
                "\"sahara\":{\"owner\":\"red\",\"armies\": \"4\"}" +
                "}," +
                "\"currentPlayer\":\"cyan\"," +
                "\"currentRound\":\"attack\"" +
                "}"
    }

    /**
     * simulador de backend
     */
    private fun mockupCountriesDataUpdate(): String {
        return "{" +
                "\"countries\":" +
                "[" +
                "{\"country\":\"brazil\",\"owner\":\"cyan\",\"armies\": \"2\"}," +
                "{\"country\":\"colombia\",\"owner\":\"cyan\",\"armies\": \"3\"}" +
                "]" +
                "}"
    }

    /**
     * simulador de backend
     */
    private fun getAttackResults(): String {
        var jsonReturn: String = "{"
        val imageAttacker = countryObjects[attackerCountry]?.get("image") as ImageView
        val imageDefender = countryObjects[defenderCountry]?.get("image") as ImageView
        val armiesAttacker = countryObjects[attackerCountry]?.get("number") as TextView
        val armiesDefender = countryObjects[defenderCountry]?.get("number") as TextView
        var attackerArmiesDices = if (Integer.parseInt(armiesAttacker.text.toString()) > 4) 3 else (Integer.parseInt(armiesAttacker.text.toString()) - 1)
        var defenderArmiesDices = if (Integer.parseInt(armiesDefender.text.toString()) > 3) 3 else Integer.parseInt(armiesDefender.text.toString())
        var attackerDicesResults = mutableListOf<Int>()
        var defenderDicesResults = mutableListOf<Int>()
        println("cantidad de ejercitos atacante: " + attackerArmiesDices)
        println("cantidad de ejercitos defensor: " + defenderArmiesDices)
        for (i in 0 until attackerArmiesDices) {
            attackerDicesResults.add((1..6).random())
        }
        for (i in 0 until defenderArmiesDices) {
            defenderDicesResults.add((1..6).random())
        }
        attackerDicesResults.sortDescending()
        defenderDicesResults.sortDescending()

        jsonReturn += "\"attackerDices\":["
        var iterator = attackerDicesResults.listIterator()
        iterator.forEach {
            jsonReturn += "\"" + it.toString() + "\","
            //            println("valor del dado atacante: " + it)
        }
        jsonReturn = jsonReturn.trimEnd(',')
        jsonReturn += "]"
        jsonReturn += ",\"defenderDices\":["
        iterator = defenderDicesResults.listIterator()
        iterator.forEach {
            jsonReturn += "\"" + it.toString() + "\","
            //            println("valor del dado defensor: " + it)
        }
        jsonReturn = jsonReturn.trimEnd(',')
        jsonReturn += "]"


        val significativeDices = if (attackerDicesResults.size <= defenderDicesResults.size) attackerDicesResults.size else defenderDicesResults.size
        var armiesAttackerQuant = Integer.parseInt(armiesAttacker.text.toString())
        var armiesDefenderQuant = Integer.parseInt(armiesDefender.text.toString())
        println("ejércitos del atacante: " + armiesAttackerQuant)
        println("ejércitos del defensor: " + armiesDefenderQuant)
        for (i in 0 until significativeDices) {
            if (attackerDicesResults.get(i) > defenderDicesResults.get(i))
                armiesDefenderQuant--
            else
                armiesAttackerQuant--
        }
        println("ejércitos del atacante: " + armiesAttackerQuant)
        println("ejércitos del defensor: " + armiesDefenderQuant)

        jsonReturn += ",\"countries\":["
        if (armiesDefenderQuant > 0) {  //NO conquistó país
            jsonReturn += "{\"country\":\"" + attackerCountry + "\",\"owner\":\"" + countriesOwners[imageAttacker] + "\",\"armies\": \"" + armiesAttackerQuant.toString() + "\"}," +
                    "{\"country\":\"" + defenderCountry + "\",\"owner\":\"" + countriesOwners[imageDefender] + "\",\"armies\": \"" + armiesDefenderQuant.toString() + "\"}"
        } else {    //conquistó país
            jsonReturn += "{\"country\":\"" + attackerCountry + "\",\"owner\":\"" + countriesOwners[imageAttacker] + "\",\"armies\": \"" + (armiesAttackerQuant - 1).toString() + "\"}," +
                    "{\"country\":\"" + defenderCountry + "\",\"owner\":\"" + countriesOwners[imageAttacker] + "\",\"armies\": \"1\"}"
        }
        jsonReturn += "]"
        jsonReturn += "}"   //cierre del objeto global
        return jsonReturn
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }


}
