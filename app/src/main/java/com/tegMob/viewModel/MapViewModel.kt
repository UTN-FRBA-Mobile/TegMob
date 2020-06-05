package com.tegMob.viewModel

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.tegMob.utils.MyViewModel
import kotlinx.android.synthetic.main.map_fragment.*
import org.json.JSONObject


class MapViewModel : MyViewModel() {
    private lateinit var countryBackColors: Map<String, ImageView>
    private val playerColors = mapOf(
        "green" to Color.GREEN,
        "red" to Color.RED,
        "cyan" to Color.CYAN,
        "magenta" to Color.MAGENTA,
        "yellow" to Color.YELLOW,
        "black" to Color.BLACK
    )
    private lateinit var bitMapFullView: Bitmap

    fun Map(view: View, windowWidth: Int, windowHeight: Int, countriesData: JSONObject) {
        bitMapFullView = loadBitmapFromView(view, windowWidth, windowHeight)
        countryBackColors = mapOf(
            "174176169" to myFragment.imageColombia,
            "0247255" to myFragment.imagePeru,
            "551115" to myFragment.imageBrazil,
            "000" to myFragment.imageChile,
            "1387324" to myFragment.imageArgentina,
            "25543189" to myFragment.imageUruguay,
            "132084" to myFragment.imageSahara,
            "25400" to myFragment.imageEgypt,
            "2520255" to myFragment.imageEthiopia,
            "2342550" to myFragment.imageZaire,
            "302540" to myFragment.imageSouthafrica,
            "971330" to myFragment.imageMadagascar
        )
        setCountriesData(countriesData)
        //        val windowWidthHeightRelation = windowWidth.toFloat() / windowHeight.toFloat()
        var widthRelation: Float = windowWidth / 800F
        var heightRelation: Float = windowHeight / 480F
        var xRelation: Float = widthRelation
        var yRelation: Float = heightRelation
        drawCountries(widthRelation, heightRelation, xRelation, yRelation)
    }

    private fun setCountriesData(countriesData: JSONObject) {
        //América del sur
        myFragment.imageChile.setColorFilter(playerColors[countriesData.getJSONObject("chile").getString("owner")]!!)
        myFragment.imageBrazil.setColorFilter(playerColors[countriesData.getJSONObject("brazil").getString("owner")]!!)
        myFragment.imageUruguay.setColorFilter(playerColors[countriesData.getJSONObject("uruguay").getString("owner")]!!)
        myFragment.imageArgentina.setColorFilter(playerColors[countriesData.getJSONObject("argentina").getString("owner")]!!)
        myFragment.imageColombia.setColorFilter(playerColors[countriesData.getJSONObject("colombia").getString("owner")]!!)
        myFragment.imagePeru.setColorFilter(playerColors[countriesData.getJSONObject("peru").getString("owner")]!!)
        myFragment.numberChile.setText(countriesData.getJSONObject("chile").getString("armies"))
        myFragment.numberPeru.setText(countriesData.getJSONObject("peru").getString("armies"))
        myFragment.numberBrazil.setText(countriesData.getJSONObject("brazil").getString("armies"))
        myFragment.numberColombia.setText(countriesData.getJSONObject("colombia").getString("armies"))
        myFragment.numberArgentina.setText(countriesData.getJSONObject("argentina").getString("armies"))
        myFragment.numberUruguay.setText(countriesData.getJSONObject("uruguay").getString("armies"))

        //África
        //        unwrappedDrawable = AppCompatResources.getDrawable(myContext!!, R.drawable.sahara_white)
//        wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
//        DrawableCompat.setTint(wrappedDrawable, playerColors[countriesData.getJSONObject("sahara").getString("owner")]!!)
//        myFragment.imageSahara.setImageResource(R.drawable.sahara_white)
        myFragment.imageSahara.setColorFilter(playerColors[countriesData.getJSONObject("sahara").getString("owner")]!!)
        myFragment.imageZaire.setColorFilter(playerColors[countriesData.getJSONObject("zaire").getString("owner")]!!)
        myFragment.imageMadagascar.setColorFilter(playerColors[countriesData.getJSONObject("madagascar").getString("owner")]!!)
        myFragment.imageEthiopia.setColorFilter(playerColors[countriesData.getJSONObject("ethiopia").getString("owner")]!!)
        myFragment.imageSouthafrica.setColorFilter(playerColors[countriesData.getJSONObject("southafrica").getString("owner")]!!)
        myFragment.imageEgypt.setColorFilter(playerColors[countriesData.getJSONObject("egypt").getString("owner")]!!)
        myFragment.numberZaire.setText(countriesData.getJSONObject("zaire").getString("armies"))
        myFragment.numberSouthafrica.setText(countriesData.getJSONObject("southafrica").getString("armies"))
        myFragment.numberEgypt.setText(countriesData.getJSONObject("egypt").getString("armies"))
        myFragment.numberEthiopia.setText(countriesData.getJSONObject("ethiopia").getString("armies"))
        myFragment.numberSahara.setText(countriesData.getJSONObject("sahara").getString("armies"))
        myFragment.numberMadagascar.setText(countriesData.getJSONObject("madagascar").getString("armies"))

    }

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
        myFragment.imagePeru.x = 224F * xRelation
        myFragment.imagePeru.y = 282F * yRelation
        myFragment.numberPeru.x = myFragment.imagePeru.x + (myFragment.imagePeru.layoutParams.width / 2) - (myFragment.numberPeru.layoutParams.width / 2)
        myFragment.numberPeru.y = myFragment.imagePeru.y + (myFragment.imagePeru.layoutParams.height / 2) - (myFragment.numberPeru.layoutParams.height / 2)
        myFragment.imageBrazil.x = 269F * xRelation    //269
        myFragment.imageBrazil.y = 246F * yRelation
        myFragment.numberBrazil.x = myFragment.imageBrazil.x + (myFragment.imageBrazil.layoutParams.width / 2) - (myFragment.numberBrazil.layoutParams.width / 2)
        myFragment.numberBrazil.y = myFragment.imageBrazil.y + (myFragment.imageBrazil.layoutParams.height / 2) - (myFragment.numberBrazil.layoutParams.height / 2)
        myFragment.imageChile.x = 238F * xRelation     //238
        myFragment.imageChile.y = 321F * yRelation
        myFragment.numberChile.x = myFragment.imageChile.x + (myFragment.imageChile.layoutParams.width / 2) - (myFragment.numberChile.layoutParams.width / 2)
        myFragment.numberChile.y = myFragment.imageChile.y + (myFragment.imageChile.layoutParams.height / 2) - (myFragment.numberChile.layoutParams.height / 2)
        myFragment.imageArgentina.x = 247F * xRelation
        myFragment.imageArgentina.y = 319F * yRelation
        myFragment.numberArgentina.x = myFragment.imageArgentina.x + (myFragment.imageArgentina.layoutParams.width / 2) - (myFragment.numberArgentina.layoutParams.width / 2)
        myFragment.numberArgentina.y = myFragment.imageArgentina.y + (myFragment.imageArgentina.layoutParams.height / 2) - (myFragment.numberArgentina.layoutParams.height / 2)
        myFragment.imageUruguay.x = 285F * xRelation   //285
        myFragment.imageUruguay.y = 322F * yRelation   //322
        myFragment.numberUruguay.x = myFragment.imageUruguay.x + (myFragment.imageUruguay.layoutParams.width / 2) - (myFragment.numberUruguay.layoutParams.width / 2)
        myFragment.numberUruguay.y = myFragment.imageUruguay.y + (myFragment.imageUruguay.layoutParams.height / 2) - (myFragment.numberUruguay.layoutParams.height / 2)

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
        myFragment.imageEgypt.x = 517F * xRelation    //520
        myFragment.imageEgypt.y = 289F * yRelation
        myFragment.numberEgypt.x = myFragment.imageEgypt.x + (myFragment.imageEgypt.layoutParams.width / 2) - (myFragment.numberEgypt.layoutParams.width / 2)
        myFragment.numberEgypt.y = myFragment.imageEgypt.y + (myFragment.imageEgypt.layoutParams.height / 2) - (myFragment.numberEgypt.layoutParams.height / 2)
        myFragment.imageEthiopia.x = 518 * xRelation
        myFragment.imageEthiopia.y = 317F * yRelation  //330
        myFragment.numberEthiopia.x = myFragment.imageEthiopia.x + (myFragment.imageEthiopia.layoutParams.width / 2) - (myFragment.numberEthiopia.layoutParams.width / 2)
        myFragment.numberEthiopia.y = myFragment.imageEthiopia.y + (myFragment.imageEthiopia.layoutParams.height / 2) - (myFragment.numberEthiopia.layoutParams.height / 2)
        myFragment.imageZaire.x = 464F * xRelation
        myFragment.imageZaire.y = 340F * yRelation
        myFragment.numberZaire.x = myFragment.imageZaire.x + (myFragment.imageZaire.layoutParams.width / 2) - (myFragment.numberZaire.layoutParams.width / 2)
        myFragment.numberZaire.y = myFragment.imageZaire.y + (myFragment.imageZaire.layoutParams.height / 2) - (myFragment.numberZaire.layoutParams.height / 2)
        myFragment.imageSouthafrica.x = 537F * xRelation
        myFragment.imageSouthafrica.y = 364F * yRelation
        myFragment.numberSouthafrica.x = myFragment.imageSouthafrica.x + (myFragment.imageSouthafrica.layoutParams.width / 2) - (myFragment.numberSouthafrica.layoutParams.width / 2)
        myFragment.numberSouthafrica.y = myFragment.imageSouthafrica.y + (myFragment.imageSouthafrica.layoutParams.height / 2) - (myFragment.numberSouthafrica.layoutParams.height / 2)
        myFragment.imageMadagascar.x = 618F * xRelation
        myFragment.imageMadagascar.y = 317F * yRelation
        myFragment.numberMadagascar.x = myFragment.imageMadagascar.x + (myFragment.imageMadagascar.layoutParams.width / 2) - (myFragment.numberMadagascar.layoutParams.width / 2)
        myFragment.numberMadagascar.y = myFragment.imageMadagascar.y + (myFragment.imageMadagascar.layoutParams.height / 2) - (myFragment.numberMadagascar.layoutParams.height / 2)

    }

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

    fun screenTouched(view: View, event: MotionEvent): Boolean {

        val touchColor: Int = bitMapFullView.getPixel(event.getX().toInt(), event.getY().toInt())
        val redValue = Color.red(touchColor)
        val blueValue = Color.blue(touchColor)
        val greenValue = Color.green(touchColor)
        val countryImage: ImageView? = countryBackColors[redValue.toString() + blueValue.toString() + greenValue]
        Log.i("país clickeado", countryImage?.contentDescription.toString())

        return true
    }



    override fun setDataToPass(): Bundle {
        TODO("Not yet implemented")
    }

}
