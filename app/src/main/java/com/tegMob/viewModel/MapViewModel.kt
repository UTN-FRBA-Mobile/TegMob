package com.tegMob.viewModel

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.tegMob.R
import com.tegMob.utils.MyViewModel
import com.tegMob.utils.constants.CountriesImagesNames
import kotlinx.android.synthetic.main.map_fragment.*
import org.json.JSONObject


class MapViewModel : MyViewModel() {
    private lateinit var countryBackColors: Map<String, ImageView>

    private val countriesImageNames = CountriesImagesNames()

    private var chileColors = R.drawable.chile_pink

    private lateinit var bitMapFullView: Bitmap


    fun Map(view: View, windowWidth: Int, windowHeight: Int, countriesData: JSONObject) {
        bitMapFullView = loadBitmapFromView(view, windowWidth, windowHeight)
        setCountriesData(countriesData)


        //        val windowWidthHeightRelation = windowWidth.toFloat() / windowHeight.toFloat()
        var widthRelation: Float = windowWidth / 800F
        var heightRelation: Float = windowHeight / 480F
        var xRelation: Float = widthRelation
        var yRelation: Float = heightRelation
        drawCountries(widthRelation, heightRelation, xRelation, yRelation)

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
    }

    private fun setCountriesData(countriesData: JSONObject) {
//        val lala="chile_green"
        val chileColorString="chile_"+countriesData.getJSONObject("chile").getString("owner")
        val resources: Resources = myContext!!.getResources()
        val lalaId=resources.getIdentifier(chileColorString,"drawable", myContext!!.packageName)

        Log.i("string lala",chileColorString)
        Log.i("el que funciona",R.drawable.chile_green.toString())
        Log.i("con la variable",lalaId.toString())

//        //        val name = "your_drawable"
//        val id: Int = R.getIdentifier(lala, "drawable", getPackageName())
//        val drawable: Drawable = getResources().getDrawable(id)

//        Log.i("color recibido de chile",R.drawable.field(lala))

//        Log.i("id de chile green",R.drawable.getIdentifier(${lala}).toString())
//        myFragment.imageChile.setImageResource(countriesImageNames.chileImageNames.getValue(countriesData.getJSONObject("chile").getString("owner")))
        myFragment.imageChile.setImageResource(resources.getIdentifier("chile_"+countriesData.getJSONObject("chile").getString("owner"),"drawable", myContext!!.packageName))
        myFragment.imageColombia.setImageResource(resources.getIdentifier("colombia_"+countriesData.getJSONObject("colombia").getString("owner"),"drawable", myContext!!.packageName))
        myFragment.imagePeru.setImageResource(resources.getIdentifier("peru"+countriesData.getJSONObject("peru").getString("owner"),"drawable", myContext!!.packageName))
        myFragment.imageBrazil.setImageResource(resources.getIdentifier("brazil_"+countriesData.getJSONObject("brazil").getString("owner"),"drawable", myContext!!.packageName))
        myFragment.imageArgentina.setImageResource(resources.getIdentifier("argentina_"+countriesData.getJSONObject("argentina").getString("owner"),"drawable", myContext!!.packageName))
        myFragment.imageUruguay.setImageResource(resources.getIdentifier("uruguay_"+countriesData.getJSONObject("uruguay").getString("owner"),"drawable", myContext!!.packageName))
        myFragment.imageMadagascar.setImageResource(resources.getIdentifier("madagascar_"+countriesData.getJSONObject("madagascar").getString("owner"),"drawable", myContext!!.packageName))
        myFragment.imageSahara.setImageResource(resources.getIdentifier("sahara_"+countriesData.getJSONObject("chile").getString("owner"),"drawable", myContext!!.packageName))
        myFragment.imageEgypt.setImageResource(resources.getIdentifier("egypt_"+countriesData.getJSONObject("chile").getString("owner"),"drawable", myContext!!.packageName))
        myFragment.imageEthiopia.setImageResource(resources.getIdentifier("ethiopia_"+countriesData.getJSONObject("chile").getString("owner"),"drawable", myContext!!.packageName))
        myFragment.imageSouthafrica.setImageResource(resources.getIdentifier("southafrica_"+countriesData.getJSONObject("chile").getString("owner"),"drawable", myContext!!.packageName))
        myFragment.imageZaire.setImageResource(resources.getIdentifier("zaire_"+countriesData.getJSONObject("chile").getString("owner"),"drawable", myContext!!.packageName))



//        myFragment.imageColombia.setImageResource(R.drawable.colombia_green)
//        myFragment.imagePeru.setImageResource(R.drawable.peru_green)
//        myFragment.imageBrazil.setImageResource(R.drawable.brazil_lightblue)
//        myFragment.imageArgentina.setImageResource(R.drawable.argentina_lightblue)
//        myFragment.imageUruguay.setImageResource(R.drawable.uruguay_red)
//        myFragment.imageMadagascar.setImageResource(R.drawable.madagascar_yellow)
//        myFragment.imageSahara.setImageResource(R.drawable.sahara_green)
//        myFragment.imageEgypt.setImageResource(R.drawable.egypt_lightblue)
//        myFragment.imageEthiopia.setImageResource(R.drawable.ethiopia_black)
//        myFragment.imageSouthafrica.setImageResource(R.drawable.southafrica_red)
//        myFragment.imageZaire.setImageResource(R.drawable.zaire_black)

        myFragment.numberChile.setText(countriesData.getJSONObject("chile").getString("armies"))
        myFragment.numberPeru.setText(countriesData.getJSONObject("peru").getString("armies"))
        myFragment.numberBrazil.setText(countriesData.getJSONObject("brazil").getString("armies"))
        myFragment.numberColombia.setText(countriesData.getJSONObject("colombia").getString("armies"))
        myFragment.numberArgentina.setText(countriesData.getJSONObject("argentina").getString("armies"))
        myFragment.numberUruguay.setText(countriesData.getJSONObject("uruguay").getString("armies"))
        myFragment.numberZaire.setText(countriesData.getJSONObject("zaire").getString("armies"))
        myFragment.numberSouthafrica.setText(countriesData.getJSONObject("southafrica").getString("armies"))
        myFragment.numberEgypt.setText(countriesData.getJSONObject("egypt").getString("armies"))
        myFragment.numberEthiopia.setText(countriesData.getJSONObject("ethiopia").getString("armies"))
        myFragment.numberSahara.setText(countriesData.getJSONObject("sahara").getString("armies"))
        myFragment.numberMadagascar.setText(countriesData.getJSONObject("madagascar").getString("armies"))

    }

    fun drawCountries(widthRelation: Float, heightRelation: Float, xRelation: Float, yRelation: Float) {
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
        val countryImage: ImageView = countryBackColors[redValue.toString() + blueValue.toString() + greenValue.toString()]!!

        if (countryImage == myFragment.imageChile) {
            //            Log.i("current_drawable", countryImage.drawable.current.toString())
            when (chileColors) {
                R.drawable.chile_pink -> {
                    myFragment.imageChile.setImageResource(R.drawable.chile_lightblue)
                    chileColors = R.drawable.chile_lightblue
                }
                R.drawable.chile_lightblue -> {
                    myFragment.imageChile.setImageResource(R.drawable.chile_black)
                    chileColors = R.drawable.chile_black
                }
                R.drawable.chile_black -> {
                    myFragment.imageChile.setImageResource(R.drawable.chile_green)
                    chileColors = R.drawable.chile_green
                }
                R.drawable.chile_green -> {
                    myFragment.imageChile.setImageResource(R.drawable.chile_yellow)
                    chileColors = R.drawable.chile_yellow
                }
                R.drawable.chile_yellow -> {
                    myFragment.imageChile.setImageResource(R.drawable.chile_red)
                    chileColors = R.drawable.chile_red
                }
                R.drawable.chile_red -> {
                    myFragment.imageChile.setImageResource(R.drawable.chile_pink)
                    chileColors = R.drawable.chile_pink
                }
            }

        }
        return true
    }

    override fun setDataToPass(): Bundle {
        TODO("Not yet implemented")
    }
}
