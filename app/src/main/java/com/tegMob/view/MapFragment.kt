package com.tegMob.view

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
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
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text


class MapFragment : MyFragment() {
    private lateinit var viewModel: MapViewModel
    private lateinit var initMapData: JSONObject
    private lateinit var dice1: AnimationDrawable
    private lateinit var dice2: AnimationDrawable
    private lateinit var dice3: AnimationDrawable
    private lateinit var dice4: AnimationDrawable
    private lateinit var dice5: AnimationDrawable
    private lateinit var dice6: AnimationDrawable
    private lateinit var countryObjects: Map<String, Map<String, Any>>

    private var windowHeight: Int = 0
    private var windowWidth: Int = 0
    private val displayMetrics = DisplayMetrics()
    private var attackerCountry: String? = null
    private var defenderCountry: String? = null

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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (activity?.requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        return inflater.inflate(R.layout.map_fragment, container, false)
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
                imageChile, imageZaire, imageEgypt
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

        btnAtack.setOnClickListener {
            val attackerArmies = countryObjects[attackerCountry]?.get("number") as TextView
            val attackerArmiesNumber = Integer.parseInt(attackerArmies.text.toString())
            var defenderArmies = countryObjects[defenderCountry]?.get("number") as TextView
            val defenderArmiesNumber = Integer.parseInt(defenderArmies.text.toString())
            Log.i("attackerArmiesNumber", attackerArmiesNumber.toString())
            Log.i("defenderArmiesNumber", defenderArmiesNumber.toString())

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


            btnAtack.visibility = View.INVISIBLE
            btnStop.visibility = View.VISIBLE
        }

        btnStop.setOnClickListener {
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

        btnAccept.setOnClickListener {
            resetAttack()
            val updateData = JSONObject(mockupCountriesDataUpdate())
            val countriesData = updateData.getJSONArray("countries")

            for (i in 0 until countriesData.length()) {
                val item = countriesData.getJSONObject(i)
                Log.i("país", item.getString("country"))
                Log.i("color", item.getString("owner"))
                Log.i("number", item.getString("armies"))
                val countryImage = countryObjects[item.getString("country")]!!["image"] as ImageView
                val countryNumber = countryObjects[item.getString("country")]!!["number"] as TextView
                countryImage.setColorFilter(viewModel.playerColors[item.getString("owner")]!!)
                countryNumber.setText(item.getString("armies"))
                countriesOwners[countryImage] = item.getString("owner")
            }
            //            viewModel.updateData(updateData.getJSONArray("countries"))
        }

        locationIcon.setOnClickListener() {
            if (countryNames.visibility == View.VISIBLE)
                countryNames.visibility = View.INVISIBLE
            else
                countryNames.visibility = View.VISIBLE
        }

        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        windowWidth = displayMetrics.widthPixels
        windowHeight = displayMetrics.heightPixels

        viewModel.run { Map(view, windowWidth, windowHeight, initMapData) }
        /*        windowWidth = mapaBack.layoutParams.width
                windowHeight = mapaBack.layoutParams.height


                if (windowWidthHeightRelation > 1.8F) {
                    widthRelation = windowWidth / 703F
                    xRelation = windowWidth / 810F
                }*/

    }

    /**
     * resetea los parámetros y elementos que se usan para los ataques para poder usarlos en un ataque futuro
     */
    private fun resetAttack() {
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
        btnAtack.visibility = View.INVISIBLE
        btnStop.visibility = View.INVISIBLE
    }

    /**
     * define las acciones al tocar la pantalla
     */
    private fun screenTouched(view: View, event: MotionEvent): Boolean {
        errorNoOwnCountry.visibility = View.INVISIBLE
        errorNoNeighbourCountry.visibility = View.INVISIBLE
        errorAttackingOwnCountry.visibility = View.INVISIBLE
        errorNotEnoughArmies.visibility = View.INVISIBLE

        val touchedCountryName = viewModel.getCountryImageTouched(view, event)  //puede ser nulo si toco el mar o los bordes de los países

        if (touchedCountryName == null) {
            resetAttack()
            Log.i("país tocado", "ninguno")
            return false
        }
        Log.i("pais tocado", touchedCountryName)
        val touchedCountry = countryObjects[touchedCountryName]!!["image"] as ImageView
        val attackerCountryLocal = attackerCountry  //variable local para evitar error: Smart cast to 'Type' is impossible, because 'variable' is a mutable property that could have been changed by this time
        if (attackerCountryLocal === null) {    //elige el país desde el cual ataca
            if (countriesOwners[touchedCountry] != initMapData.getString("currentPlayer")) {
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
            if (countriesOwners.get(touchedCountry) == initMapData.getString("currentPlayer")) {
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
            btnAtack.visibility = View.VISIBLE
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

        return jsonObjData
    }


    override fun getPassedData() {
        TODO("Not yet implemented")
    }

    override fun initViewModel() {
        viewModel = MapViewModel()
        context?.let { viewModel.init(this, listener, it) }
    }


    private fun mockupDataInit(): String {
        return "{" +
                "\"countries\":" +
                "{" +
                "\"brazil\":{\"owner\":\"cyan\",\"armies\": \"5\"}," +
                "\"colombia\":{\"owner\":\"magenta\",\"armies\": \"1\"}," +
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

    private fun mockupCountriesDataUpdate(): String {
        return "{" +
                "\"countries\":" +
                "[" +
                "{\"country\":\"brazil\",\"owner\":\"cyan\",\"armies\": \"2\"}," +
                "{\"country\":\"colombia\",\"owner\":\"cyan\",\"armies\": \"3\"}" +
                "]" +
                "}"
    }

//    private fun makeAttack() {
//        val armiesAttacker = countryObjects[attackerCountry]?.get("number") as TextView
//        val armiesDefender = countryObjects[defenderCountry]?.get("number") as TextView
//
//    }
}
