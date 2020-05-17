package com.tegMob.view

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.collection.ArrayMap

import com.tegMob.R
import kotlinx.android.synthetic.main.map_fragment.*
import kotlinx.android.synthetic.main.map_fragment.view.*


class MapFragment : Fragment() {
    //
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
    //color del mar: 37255155
    private val countryBackColors = mapOf<String, String>(
        "174176169" to "Colombia",
        "0247255" to "Perú",
        "551115" to "Brasil",
        "000" to "Chile",
        "1387324" to "Argentina",
        "25543189" to "Uruguay",
        "132084" to "Sahara",
        "25400" to "Egipto",
        "2520255" to "Etiopía",
        "2342550" to "Zaire",
        "302540" to "Sudáfrica",
        "971330" to "Madagascar"
    )

    private var chileColors = "pink"

    private lateinit var bitMapFullView: Bitmap

    //    private lateinit var fullView: View
    private var windowHeight: Int = 0
    private var windowWidth: Int = 0
    private var densityRelation: Float = 0F
    private val displayMetrics = DisplayMetrics()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        return inflater.inflate(R.layout.main_fragment, container, false)
        val view: View = inflater.inflate(R.layout.map_fragment, container, false)
        view.setOnTouchListener { v, event ->
            if (event.action == ACTION_DOWN) {
                //do something
                screenTouched(v, event)
            }
            true
        }

        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        windowWidth = displayMetrics.widthPixels
        windowHeight = displayMetrics.heightPixels
        densityRelation = displayMetrics.density
//        fullView=view
        bitMapFullView = loadBitmapFromView(view, windowWidth, windowHeight)

        return view
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val widthRelation: Float = windowWidth / 800F
        val heightRelation: Float = windowHeight / 480F

        //América del Sur
        //Tamaño de los países
        imageColombia.layoutParams.width = (67 * widthRelation).toInt()
        imageColombia.layoutParams.height = (50 * heightRelation).toInt()
        imagePeru.layoutParams.width = (69 * widthRelation).toInt()
        imagePeru.layoutParams.height = (46 * heightRelation).toInt()
        imageBrasil.layoutParams.width = (102 * widthRelation).toInt()
        imageBrasil.layoutParams.height = (89 * heightRelation).toInt()
        imageChile.layoutParams.width = (23 * widthRelation).toInt()
        imageChile.layoutParams.height = (87 * heightRelation).toInt()
        imageArgentina.layoutParams.width = (50 * widthRelation).toInt()
        imageArgentina.layoutParams.height = (91 * heightRelation).toInt()
        imageUruguay.layoutParams.width = (52 * widthRelation).toInt()
        imageUruguay.layoutParams.height = (44 * heightRelation).toInt()

        //Ubicación de los países
        imageColombia.x = 217F * widthRelation
        imageColombia.y = 243F * heightRelation     //243
        imagePeru.x = 224F * widthRelation
        imagePeru.y = 282F * heightRelation
        imageBrasil.x = 269F * widthRelation    //268
        imageBrasil.y = 246F * heightRelation
        imageChile.x = 238F * widthRelation     //238
        imageChile.y = 321F * heightRelation
        imageArgentina.x = 247F * widthRelation
        imageArgentina.y = 321F * widthRelation
        imageUruguay.x = 285F * widthRelation   //286
        imageUruguay.y = 322F * widthRelation   //322

//        imageChile.setOnClickListener {
//            onCountryTouched(it as ImageView)
//        }


        // África
        //Tamaño de los países
        imageSahara.layoutParams.width = (90 * widthRelation).toInt()
        imageSahara.layoutParams.height = (80 * heightRelation).toInt()
        imageEgipto.layoutParams.width = (126 * widthRelation).toInt()  //126
        imageEgipto.layoutParams.height = (58 * heightRelation).toInt() //58
        imageEtiopia.layoutParams.width = (93 * widthRelation).toInt()
        imageEtiopia.layoutParams.height = (54 * heightRelation).toInt()
        imageZaire.layoutParams.width = (126 * widthRelation).toInt()
        imageZaire.layoutParams.height = (58 * heightRelation).toInt()
        imageSudafrica.layoutParams.width = (70 * widthRelation).toInt()
        imageSudafrica.layoutParams.height = (65 * heightRelation).toInt()
        imageMadagascar.layoutParams.width = (45 * widthRelation).toInt()
        imageMadagascar.layoutParams.height = (118 * heightRelation).toInt()

        //Ubicación de los países
        imageSahara.x = 448F * widthRelation    //450
        imageSahara.y = 294F * heightRelation
        imageEgipto.x = 517F * widthRelation    //520
        imageEgipto.y = 289F * heightRelation
        imageEtiopia.x = 518 * widthRelation
        imageEtiopia.y = 317F * heightRelation  //330
        imageZaire.x = 464F * widthRelation
        imageZaire.y = 340F * heightRelation
        imageSudafrica.x = 537F * widthRelation
        imageSudafrica.y = 364F * heightRelation
        imageMadagascar.x = 618F * widthRelation
        imageMadagascar.y = 317F * heightRelation

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun screenTouched(view: View, event: MotionEvent): Boolean {

        val touchColor: Int = bitMapFullView.getPixel(event.getX().toInt(), event.getY().toInt())
        val redValue = Color.red(touchColor)
        val blueValue = Color.blue(touchColor)
        val greenValue = Color.green(touchColor)
        val countryName: String? =
            countryBackColors[redValue.toString() + blueValue.toString() + greenValue.toString()]
        if (countryName != null)
            AlertDialog.Builder(activity!!)
                .setTitle("País: " + countryName)
                .setPositiveButton("OK") { _, _ -> }
                .create().show()

        if (countryName == "Chile") {
            when (chileColors) {
                "pink" -> {
                    imageChile.setImageResource(R.drawable.chile_light_blue)
                    chileColors = "lightBlue"
                }
                "lightBlue" -> {
                    imageChile.setImageResource(R.drawable.chile_black)
                    chileColors = "black"
                }
                "black" -> {
                    imageChile.setImageResource(R.drawable.chile_green)
                    chileColors = "green"
                }
                "green" -> {
                    imageChile.setImageResource(R.drawable.chile_yellow)
                    chileColors = "yellow"
                }
                "yellow" -> {
                    imageChile.setImageResource(R.drawable.chile_red)
                    chileColors = "red"
                }
                "red" -> {
                    imageChile.setImageResource(R.drawable.chile_pink)
                    chileColors = "pink"
                }
            }

        }
//        AlertDialog.Builder(activity!!)
//            .setTitle(redValue.toString()+blueValue.toString()+greenValue.toString())
//            .setPositiveButton("OK") { _, _ -> }
//            .create().show()

        return true
    }

    private fun onCountryTouched(country: ImageView) {
        country.setImageResource(R.drawable.chile_light_blue)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MapFragment()
    }
}
