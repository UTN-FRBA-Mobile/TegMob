package com.tegMob.view

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
//import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewGroup

import com.tegMob.R
import com.tegMob.utils.MyFragment
import com.tegMob.viewModel.MapViewModel
import com.tegMob.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.map_fragment.*


class MapFragment : MyFragment() {
    private val countryBackColors = mapOf<String, String>(
        "174176169" to "Colombia", "0247255" to "Perú", "551115" to "Brasil", "000" to "Chile", "1387324" to "Argentina", "25543189" to "Uruguay", "132084" to "Sahara", "25400" to "Egipto", "2520255" to "Etiopía", "2342550" to "Zaire", "302540" to "Sudáfrica", "971330" to "Madagascar"
    )

    private var chileColors = "pink"
    private lateinit var bitMapFullView: Bitmap

    //    private lateinit var fullView: View
    private var windowHeight: Int = 0
    private var windowWidth: Int = 0
    private var densityRelation: Float = 0F
    private val displayMetrics = DisplayMetrics()

    private lateinit var viewModel: MapViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //        return inflater.inflate(R.layout.main_fragment, container, false)
        val view: View = inflater.inflate(R.layout.map_fragment, container, false)
        view.setOnTouchListener { v, event ->
            if (event.action == ACTION_DOWN) {
                screenTouched(v, event)
            }
            true
        }

        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        windowWidth = displayMetrics.widthPixels
        windowHeight = displayMetrics.heightPixels
        //        windowWidth = mapaBack.layoutParams.width
        //        windowHeight = mapaBack.layoutParams.height
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

    override fun getPassedData() {
        TODO("Not yet implemented")
    }

    override fun initViewModel() {
        viewModel = MapViewModel()
        context?.let { viewModel.init(this, listener, it) }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //        val windowWidthHeightRelation = windowWidth.toFloat() / windowHeight.toFloat()
        var widthRelation: Float = windowWidth / 800F
        var heightRelation: Float = windowHeight / 480F
        var xRelation: Float = widthRelation
        var yRelation: Float = heightRelation

        //        if (windowWidthHeightRelation > 1.8F) {
        //            widthRelation = windowWidth / 703F
        //            xRelation = windowWidth / 810F
        //        }
        //América del Sur
        //Tamaño de los países
        imageColombia.layoutParams.width = (67F * widthRelation).toInt()   //67
        imageColombia.layoutParams.height = (50F * heightRelation).toInt()
        imagePeru.layoutParams.width = (69F * widthRelation).toInt()
        imagePeru.layoutParams.height = (46F * heightRelation).toInt()
        imageBrasil.layoutParams.width = (102F * widthRelation).toInt()
        imageBrasil.layoutParams.height = (89F * heightRelation).toInt()
        imageChile.layoutParams.width = (23F * widthRelation).toInt()
        imageChile.layoutParams.height = (87F * heightRelation).toInt()
        imageArgentina.layoutParams.width = (50F * widthRelation).toInt()
        imageArgentina.layoutParams.height = (92F * heightRelation).toInt()
        imageUruguay.layoutParams.width = (52F * widthRelation).toInt()
        imageUruguay.layoutParams.height = (44F * heightRelation).toInt()

        //Ubicación de los países
        imageColombia.x = 217F * xRelation      //217
        imageColombia.y = 243F * yRelation     //243
        imagePeru.x = 224F * xRelation
        imagePeru.y = 282F * yRelation
        imageBrasil.x = 269F * xRelation    //269
        imageBrasil.y = 246F * yRelation
        imageChile.x = 238F * xRelation     //238
        imageChile.y = 321F * yRelation
        imageArgentina.x = 247F * xRelation
        imageArgentina.y = 319F * yRelation
        imageUruguay.x = 285F * xRelation   //285
        imageUruguay.y = 322F * yRelation   //322

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
        imageSahara.x = 448F * xRelation    //450
        imageSahara.y = 294F * yRelation
        imageEgipto.x = 517F * xRelation    //520
        imageEgipto.y = 289F * yRelation
        imageEtiopia.x = 518 * xRelation
        imageEtiopia.y = 317F * yRelation  //330
        imageZaire.x = 464F * xRelation
        imageZaire.y = 340F * yRelation
        imageSudafrica.x = 537F * xRelation
        imageSudafrica.y = 364F * yRelation
        imageMadagascar.x = 618F * xRelation
        imageMadagascar.y = 317F * yRelation

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
                .create()
                .show()

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
        return true
    }

    //    private fun onCountryTouched(country: ImageView) {
    //        country.setImageResource(R.drawable.chile_light_blue)
    //    }

    companion object {
        @JvmStatic
        fun newInstance() = MapFragment().apply {
            arguments = Bundle()
        }
    }
}
