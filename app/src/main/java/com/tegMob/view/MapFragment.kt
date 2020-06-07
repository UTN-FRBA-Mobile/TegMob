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
import com.tegMob.R
import com.tegMob.utils.MyFragment
import com.tegMob.viewModel.MapViewModel
import kotlinx.android.synthetic.main.map_fragment.*
import kotlinx.coroutines.delay
import org.json.JSONObject


class MapFragment : MyFragment() {
    private lateinit var viewModel: MapViewModel
    private lateinit var initMapData: JSONObject

    private var windowHeight: Int = 0
    private var windowWidth: Int = 0
    private val displayMetrics = DisplayMetrics()
    private var attackerCountry: ImageView? = null
    private var defenderCountry: ImageView? = null

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
                imageSahara, imageSouthafrica, imageEthiopia, imageMadagascar
            ),
            imageEthiopia to listOf<ImageView>(
                imageSahara, imageZaire, imageSouthafrica, imageEgypt
            ),
            imageEgypt to listOf<ImageView>(
                imageColombia, imageSahara, imageEthiopia, imageMadagascar
            ),
            imageSouthafrica to listOf<ImageView>(
                imageZaire, imageEthiopia
            ),
            imageMadagascar to listOf<ImageView>(
                imageChile, imageZaire, imageEgypt
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
            val dice1 = movingDicesAttacker1.background as AnimationDrawable
            dice1.start()
            val dice2 = movingDicesAttacker2.background as AnimationDrawable
            dice2.start()
            val dice3 = movingDicesAttacker3.background as AnimationDrawable
            dice3.start()
            val dice4 = movingDicesDefender1.background as AnimationDrawable
            dice4.start()
            val dice5 = movingDicesDefender2.background as AnimationDrawable
            dice5.start()
            val dice6 = movingDicesDefender3.background as AnimationDrawable
            dice6.start()
            movingDicesAttacker.visibility = View.VISIBLE
            movingDicesDefender.visibility = View.VISIBLE
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

    private fun resetAttack(){
        attackerCountry = null
        defenderCountry = null
        attacker.visibility = View.INVISIBLE
        defender.visibility = View.INVISIBLE
        attackTitle.visibility = View.INVISIBLE
        movingDicesAttacker.visibility = View.INVISIBLE
        movingDicesDefender.visibility = View.INVISIBLE
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
        val touchedCountry = viewModel.getCountryImageTouched(view, event)
        val attackerCountryLocal = attackerCountry  //variable local para evitar error: Smart cast to 'Type' is impossible, because 'variable' is a mutable property that could have been changed by this time
        if (touchedCountry == null) {
            resetAttack()
            return false
        }

        Log.i("countryColorInt", countriesOwners.get(touchedCountry))
        Log.i("currentPlayerColor", initMapData.getString("currentPlayer"))

        if (attackerCountryLocal === null) {    //elige el país desde el cual ataca
            if (countriesOwners.get(touchedCountry) != initMapData.getString("currentPlayer")) {
                errorNoOwnCountry.visibility = View.VISIBLE
                return false
            }
            attackerCountry = touchedCountry
            attackTitle.visibility = View.VISIBLE
            attacker.text = touchedCountry?.contentDescription.toString()
            attacker.visibility = View.VISIBLE
        } else if (defenderCountry === null) {
            if (countriesOwners.get(touchedCountry) == initMapData.getString("currentPlayer")) {
                errorAttackingOwnCountry.visibility = View.VISIBLE
                return false
            }
            if (!(countriesNeighbours.get(attackerCountryLocal)!!.contains(touchedCountry))) {
                errorNoNeighbourCountry.visibility = View.VISIBLE
                return false
            }

            defenderCountry = touchedCountry
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
        var data = "{" +
                "\"countries\":" +
                "{" +
                "\"brazil\":{\"owner\":\"cyan\",\"armies\": \"5\"}," +
                "\"colombia\":{\"owner\":\"magenta\",\"armies\": \"1\"}," +
                "\"chile\":{\"owner\":\"black\",\"armies\": \"1\"}," +
                "\"peru\":{\"owner\":\"green\",\"armies\": \"3\"}," +
                "\"argentina\":{\"owner\":\"cyan\",\"armies\": \"4\"}," +
                "\"uruguay\":{\"owner\":\"red\",\"armies\": \"6\"}," +
                "\"egypt\":{\"owner\":\"cyan\",\"armies\": \"2\"}," +
                "\"ethiopia\":{\"owner\":\"black\",\"armies\": \"1\"}," +
                "\"zaire\":{\"owner\":\"black\",\"armies\": \"3\"}," +
                "\"madagascar\":{\"owner\":\"yellow\",\"armies\": \"5\"}," +
                "\"southafrica\":{\"owner\":\"red\",\"armies\": \"1\"}," +
                "\"sahara\":{\"owner\":\"red\",\"armies\": \"4\"}" +
                "}," +
                "\"currentPlayer\":\"red\"," +
                "\"currentRound\":\"attack\"" +
                "}"
        return data
    }

    //    private fun mockupDataRound(): String {
    //        var data = "{" +
    //                "\"currentPlayer\":\"cyan\"" +
    //                "\"round\":\"attack\"" +
    //                "}"
    //        return data
    //    }


}
