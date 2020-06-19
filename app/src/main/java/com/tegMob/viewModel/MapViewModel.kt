package com.tegMob.viewModel

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.AsyncPlayer
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonParser
import com.tegMob.connectivity.socket.MatchHandler
import com.tegMob.utils.MyViewModel
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.map_fragment.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

/*
colores de países
ejércitos de los países
tipo de actividad (incorporar|atacar...)
jugador que tiene el turno
 */
class MapViewModel : MyViewModel() {
    private lateinit var countryBackColors: Map<String, String>
    val playerColors = mapOf(
        "green" to Color.GREEN,
        "red" to Color.RED,
        "cyan" to Color.CYAN,
        "magenta" to Color.MAGENTA,
        "yellow" to Color.YELLOW,
        "black" to Color.BLACK
    )
    private lateinit var bitMapFullView: Bitmap
    private lateinit var countriesData: JSONObject
    private lateinit var currentRound: String
    private lateinit var sensorMovements: FloatArray

    /* ************ acelerómetro ******************/
    fun sensorMovements(_sensorMovements: FloatArray) {
        sensorMovements = _sensorMovements
        println("coordenadas X: "+sensorMovements[0])
        println("coordenadas Y: "+sensorMovements[1])
        println("coordenadas Z: "+sensorMovements[2])
        println("random number: "+ Random(5))
        //        invalidate()
    }
    /* ************ acelerómetro ******************/

    fun Map(view: View, windowWidth: Int, windowHeight: Int, initMapData: JSONObject) {
        countriesData = initMapData.getJSONObject("countries")
        currentRound = initMapData.getString("currentRound")
        bitMapFullView = loadBitmapFromView(myFragment.backgroundMap, windowWidth, windowHeight)
        countryBackColors = mapOf(
            "174176169" to "colombia",
            "0247255" to "peru",
            "551115" to "brazil",
            "000" to "chile",
            "1387324" to "argentina",
            "25543189" to "uruguay",
            "132084" to "sahara",
            "25400" to "egypt",
            "2520255" to "ethiopia",
            "2342550" to "zaire",
            "302540" to "southafrica",
            "971330" to "madagascar"
        )


        eventSubscriptions()
        setCurrentRoundText()
        setCurrentPlayerText(initMapData.getString("currentPlayer"))
        setCountriesData()
        //        val windowWidthHeightRelation = windowWidth.toFloat() / windowHeight.toFloat()
        //        if (windowWidthHeightRelation > 1.8F)
        //            println("formato 18:9")
        //        else
        //            println("formato 16:9")
        var widthRelation: Float = windowWidth / 800F
        var heightRelation: Float = windowHeight / 480F
        var xRelation: Float = widthRelation
        var yRelation: Float = heightRelation
        drawCountries(widthRelation, heightRelation, xRelation, yRelation)
    }

    private val onMatchStart = Emitter.Listener {
        //val chat: Message = gson.fromJson(it[0].toString(), Message::class.java)
        val countries = JsonParser().parse(it[0].toString())
        Log.d("MAP_DATA", countries.toString())
        Log.d("RECEIVE", "MATCH START EVENT ARRIVED FROM SERVER")
    }

    private fun eventSubscriptions() {
        MatchHandler.getSocket()!!.on("MATCH_START", onMatchStart)
    }

    /**
     * escribe la ronda del juego actual
     * incorporar|atacar|....
     */
    private fun setCurrentRoundText() {
        when (currentRound) {
            "attack" -> {
                myFragment.textCurrentRound.append(" ATAQUE")
            }
        }
    }

    /**
     * escribe el jugador que tiene el turno
     */
    fun setCurrentPlayerText(currentPlayer: String) {
        myFragment.textCurrentPlayer.text = "Turno de " + currentPlayer.toUpperCase()
    }

    /*    fun updateData(data: JSONArray) { //EN DESUSO
          for (i in 0 until data.length()) {
              val item = data.getJSONObject(i)
              Log.i("país", item.getString("country"))
              Log.i("color", item.getString("owner"))
              Log.i("number", item.getString("armies"))
              val countryImage = countryObjects[item.getString("country")]!!["image"] as ImageView
              val countryNumber = countryObjects[item.getString("country")]!!["number"] as TextView
              countryImage.setColorFilter(playerColors[item.getString("owner")]!!)
              countryNumber.setText(item.getString("armies"))

          }
      }*/

    /**
     * setea el color y la cantidad de ejércitos que tiene cada país
     */

    private fun setCountriesData() {
        //América del sur y Africa
        val countryTexts = listOf("chile", "brazil", "uruguay", "argentina", "colombia", "peru", "sahara", "zaire", "madagascar", "ethiopia", "southafrica", "egypt")

        myFragment.getCountryImages().zip(countryTexts).forEach {
                (img, text) ->
            img.setColorFilter(playerColors[countriesData.getJSONObject(text).getString("owner")]!!)
        }

        myFragment.getCountryNumbers().zip(countryTexts).forEach {
            (numb , text) ->
            numb.text = countriesData.getJSONObject(text).getString("armies")
        }

        //África
        //        unwrappedDrawable = AppCompatResources.getDrawable(myContext!!, R.drawable.sahara_white)
        //        wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        //        DrawableCompat.setTint(wrappedDrawable, playerColors[countriesData.getJSONObject("sahara").getString("owner")]!!)
        //        myFragment.imageSahara.setImageResource(R.drawable.sahara_white)
    }

    /**
     * define el tamaño y la ubicación de cada país
     */
    private fun drawCountries(widthRelation: Float, heightRelation: Float, xRelation: Float, yRelation: Float) {
        //América del Sur
        //Tamaño de los países
        myFragment.imageColombia.layoutParams.width = (67F * widthRelation).toInt()   //67
        myFragment.imageColombia.layoutParams.height = (50F * heightRelation).toInt()
        myFragment.imagePeru.layoutParams.width = (69F * widthRelation).toInt()
        myFragment.imagePeru.layoutParams.height = (46F * heightRelation).toInt()
        myFragment.imageBrazil.layoutParams.width = (102F * widthRelation).toInt()
        myFragment.imageBrazil.layoutParams.height = (89F * heightRelation).toInt()
        myFragment.imageChile.layoutParams.width = (23F * widthRelation).toInt()
        myFragment.imageChile.layoutParams.height = (87F * heightRelation).toInt()
        myFragment.imageArgentina.layoutParams.width = (50F * widthRelation).toInt()
        myFragment.imageArgentina.layoutParams.height = (92F * heightRelation).toInt()
        myFragment.imageUruguay.layoutParams.width = (52F * widthRelation).toInt()
        myFragment.imageUruguay.layoutParams.height = (44F * heightRelation).toInt()
        //Ubicación de los países
        myFragment.imageColombia.x = 217F * xRelation      //217
        myFragment.imageColombia.y = 243F * yRelation     //243
        myFragment.numberColombia.x = myFragment.imageColombia.x + (myFragment.imageColombia.layoutParams.width / 2) - (myFragment.numberColombia.layoutParams.width / 2)
        myFragment.numberColombia.y = myFragment.imageColombia.y + (myFragment.imageColombia.layoutParams.height / 2) - (myFragment.numberColombia.layoutParams.height / 2)
        myFragment.textNameColombia.x = myFragment.numberColombia.x - 40
        myFragment.textNameColombia.y = myFragment.numberColombia.y - 30
        myFragment.imagePeru.x = 224F * xRelation
        myFragment.imagePeru.y = 282F * yRelation
        myFragment.numberPeru.x = myFragment.imagePeru.x + (myFragment.imagePeru.layoutParams.width / 2) - (myFragment.numberPeru.layoutParams.width / 2)
        myFragment.numberPeru.y = myFragment.imagePeru.y + (myFragment.imagePeru.layoutParams.height / 2) - (myFragment.numberPeru.layoutParams.height / 2)
        myFragment.textNamePeru.x = myFragment.numberPeru.x - 40
        myFragment.textNamePeru.y = myFragment.numberPeru.y - 30
        myFragment.imageBrazil.x = 269F * xRelation    //269
        myFragment.imageBrazil.y = 246F * yRelation
        myFragment.numberBrazil.x = myFragment.imageBrazil.x + (myFragment.imageBrazil.layoutParams.width / 2) - (myFragment.numberBrazil.layoutParams.width / 2)
        myFragment.numberBrazil.y = myFragment.imageBrazil.y + (myFragment.imageBrazil.layoutParams.height / 2) - (myFragment.numberBrazil.layoutParams.height / 2)
        myFragment.textNameBrazil.x = myFragment.numberBrazil.x - 10
        myFragment.textNameBrazil.y = myFragment.numberBrazil.y + 30
        myFragment.imageChile.x = 238F * xRelation     //238
        myFragment.imageChile.y = 321F * yRelation
        myFragment.numberChile.x = myFragment.imageChile.x + (myFragment.imageChile.layoutParams.width / 2) - (myFragment.numberChile.layoutParams.width / 2)
        myFragment.numberChile.y = myFragment.imageChile.y + (myFragment.imageChile.layoutParams.height / 2) - (myFragment.numberChile.layoutParams.height / 2)
        myFragment.textNameChile.x = myFragment.numberChile.x - 40
        myFragment.textNameChile.y = myFragment.numberChile.y - 30
        myFragment.imageArgentina.x = 247F * xRelation
        myFragment.imageArgentina.y = 319F * yRelation
        myFragment.numberArgentina.x = myFragment.imageArgentina.x + (myFragment.imageArgentina.layoutParams.width / 2) - (myFragment.numberArgentina.layoutParams.width / 2)
        myFragment.numberArgentina.y = myFragment.imageArgentina.y + (myFragment.imageArgentina.layoutParams.height / 2) - (myFragment.numberArgentina.layoutParams.height / 2)
        myFragment.textNameArgentina.x = myFragment.numberArgentina.x - 20
        myFragment.textNameArgentina.y = myFragment.numberArgentina.y + 30
        myFragment.imageUruguay.x = 285F * xRelation   //285
        myFragment.imageUruguay.y = 322F * yRelation   //322
        myFragment.numberUruguay.x = myFragment.imageUruguay.x + (myFragment.imageUruguay.layoutParams.width / 2) - (myFragment.numberUruguay.layoutParams.width / 2)
        myFragment.numberUruguay.y = myFragment.imageUruguay.y + (myFragment.imageUruguay.layoutParams.height / 2) - (myFragment.numberUruguay.layoutParams.height / 2)
        myFragment.textNameUruguay.x = myFragment.numberUruguay.x + 30
        myFragment.textNameUruguay.y = myFragment.numberUruguay.y + 10


        // África
        //Tamaño de los países
        myFragment.imageSahara.layoutParams.width = (90 * widthRelation).toInt()
        myFragment.imageSahara.layoutParams.height = (80 * heightRelation).toInt()
        myFragment.imageEgypt.layoutParams.width = (126 * widthRelation).toInt()  //126
        myFragment.imageEgypt.layoutParams.height = (58 * heightRelation).toInt() //58
        myFragment.imageEthiopia.layoutParams.width = (93 * widthRelation).toInt()
        myFragment.imageEthiopia.layoutParams.height = (54 * heightRelation).toInt()
        myFragment.imageZaire.layoutParams.width = (126 * widthRelation).toInt()
        myFragment.imageZaire.layoutParams.height = (58 * heightRelation).toInt()
        myFragment.imageSouthafrica.layoutParams.width = (70 * widthRelation).toInt()
        myFragment.imageSouthafrica.layoutParams.height = (65 * heightRelation).toInt()
        myFragment.imageMadagascar.layoutParams.width = (45 * widthRelation).toInt()
        myFragment.imageMadagascar.layoutParams.height = (118 * heightRelation).toInt()

        //Ubicación de los países
        myFragment.imageSahara.x = 448F * xRelation    //450
        myFragment.imageSahara.y = 294F * yRelation
        myFragment.numberSahara.x = myFragment.imageSahara.x + (myFragment.imageSahara.layoutParams.width / 2) - (myFragment.numberSahara.layoutParams.width / 2)
        myFragment.numberSahara.y = myFragment.imageSahara.y + (myFragment.imageSahara.layoutParams.height / 2) - (myFragment.numberSahara.layoutParams.height / 2)
        myFragment.textNameSahara.x = myFragment.numberSahara.x - 30
        myFragment.textNameSahara.y = myFragment.numberSahara.y - 30
        myFragment.imageEgypt.x = 517F * xRelation    //520
        myFragment.imageEgypt.y = 289F * yRelation
        myFragment.numberEgypt.x = myFragment.imageEgypt.x + (myFragment.imageEgypt.layoutParams.width / 2) - (myFragment.numberEgypt.layoutParams.width / 2)
        myFragment.numberEgypt.y = myFragment.imageEgypt.y + (myFragment.imageEgypt.layoutParams.height / 2) - (myFragment.numberEgypt.layoutParams.height / 2)
        myFragment.textNameEgypt.x = myFragment.numberEgypt.x - 20
        myFragment.textNameEgypt.y = myFragment.numberEgypt.y - 30
        myFragment.imageEthiopia.x = 518 * xRelation
        myFragment.imageEthiopia.y = 317F * yRelation  //330
        myFragment.numberEthiopia.x = myFragment.imageEthiopia.x + (myFragment.imageEthiopia.layoutParams.width / 2) - (myFragment.numberEthiopia.layoutParams.width / 2)
        myFragment.numberEthiopia.y = myFragment.imageEthiopia.y + (myFragment.imageEthiopia.layoutParams.height / 2) - (myFragment.numberEthiopia.layoutParams.height / 2)
        myFragment.textNameEthiopia.x = myFragment.numberEthiopia.x + 30
        myFragment.textNameEthiopia.y = myFragment.numberEthiopia.y + 15
        myFragment.imageZaire.x = 464F * xRelation
        myFragment.imageZaire.y = 340F * yRelation
        myFragment.numberZaire.x = myFragment.imageZaire.x + (myFragment.imageZaire.layoutParams.width / 2) - (myFragment.numberZaire.layoutParams.width / 2)
        myFragment.numberZaire.y = myFragment.imageZaire.y + (myFragment.imageZaire.layoutParams.height / 2) - (myFragment.numberZaire.layoutParams.height / 2)
        myFragment.textNameZaire.x = myFragment.numberZaire.x - 40
        myFragment.textNameZaire.y = myFragment.numberZaire.y + 30
        myFragment.imageSouthafrica.x = 537F * xRelation
        myFragment.imageSouthafrica.y = 364F * yRelation
        myFragment.numberSouthafrica.x = myFragment.imageSouthafrica.x + (myFragment.imageSouthafrica.layoutParams.width / 2) - (myFragment.numberSouthafrica.layoutParams.width / 2)
        myFragment.numberSouthafrica.y = myFragment.imageSouthafrica.y + (myFragment.imageSouthafrica.layoutParams.height / 2) - (myFragment.numberSouthafrica.layoutParams.height / 2)
        myFragment.textNameSouthafrica.x = myFragment.numberSouthafrica.x - 40
        myFragment.textNameSouthafrica.y = myFragment.numberSouthafrica.y + 30
        myFragment.imageMadagascar.x = 618F * xRelation
        myFragment.imageMadagascar.y = 317F * yRelation
        myFragment.numberMadagascar.x = myFragment.imageMadagascar.x + (myFragment.imageMadagascar.layoutParams.width / 2) - (myFragment.numberMadagascar.layoutParams.width / 2)
        myFragment.numberMadagascar.y = myFragment.imageMadagascar.y + (myFragment.imageMadagascar.layoutParams.height / 2) - (myFragment.numberMadagascar.layoutParams.height / 2)
        myFragment.textNameMadagascar.x = myFragment.numberMadagascar.x - 40
        myFragment.textNameMadagascar.y = myFragment.numberMadagascar.y - 30

    }

    /**
     * crea el bitmap para usar el color de fondo para detectar qué país se toco
     */
    private fun loadBitmapFromView(v: View, width: Int, height: Int): Bitmap {
        val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.layout(0, 0, width, height)
        //Get the view’s background
        val bgDrawable = v
        if (bgDrawable != null) //has background drawable, then draw it on the canvas
            bgDrawable.draw(c)
        else  //does not have background drawable, then draw white background on the canvas
            c.drawColor(Color.WHITE)
        v.draw(c)
        return b
    }

    fun getCountryImageTouched(view: View, event: MotionEvent): String? {
        val touchColor: Int = bitMapFullView.getPixel(event.x.toInt(), event.y.toInt())

        val redValue = Color.red(touchColor)
        val blueValue = Color.blue(touchColor)
        val greenValue = Color.green(touchColor)
        return countryBackColors[redValue.toString() + blueValue.toString() + greenValue.toString()]
    }


    override fun setDataToPass(): Bundle {
        TODO("Not yet implemented")
    }

}
