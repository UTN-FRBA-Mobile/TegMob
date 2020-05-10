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
        "2520255" to "Etipoía",
        "2342550" to "Zaire",
        "302540" to "Sudáfrica",
        "971330" to "Madagascar"
    )

    private lateinit var bitMapFullView: Bitmap
//    private lateinit var fullView: View
    private var windowHeight: Int=0
    private var windowWidth: Int=0
    private var densityRelation:Float = 0F
    private val displayMetrics = DisplayMetrics()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
//        return inflater.inflate(R.layout.main_fragment, container, false)
        val view: View = inflater.inflate(R.layout.map_fragment, container, false)
        view.setOnTouchListener { v, event ->
            if (event.action == ACTION_DOWN) {
                //do something
                screenTouched(v,event)
            }
            true
        }

        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        windowWidth = displayMetrics.widthPixels
        windowHeight = displayMetrics.heightPixels
        densityRelation = displayMetrics.density
//        fullView=view
        bitMapFullView=loadBitmapFromView(view,windowWidth,windowHeight)

        return view
    }

    private fun loadBitmapFromView(v: View, width: Int, height: Int): Bitmap {
        val b = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
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

        val widthRelation:Float= windowWidth/800F
        val heightRelation:Float= windowHeight/480F

        //América del Sur
        imageColombia.layoutParams.width=(70*widthRelation).toInt()
        imageColombia.layoutParams.height=(47*heightRelation).toInt()
        imagePeru.layoutParams.width=(63*widthRelation).toInt()
        imagePeru.layoutParams.height=(42*heightRelation).toInt()
        imageBrasil.layoutParams.width=(106*widthRelation).toInt()
        imageBrasil.layoutParams.height=(87*heightRelation).toInt()
        imageChile.layoutParams.width=(20*widthRelation).toInt()
        imageChile.layoutParams.height=(90*heightRelation).toInt()
        imageArgentina.layoutParams.width=(50*widthRelation).toInt()
        imageArgentina.layoutParams.height=(90*heightRelation).toInt()
        imageUruguay.layoutParams.width=(49*widthRelation).toInt()
        imageUruguay.layoutParams.height=(44*heightRelation).toInt()

        imageChile.setOnClickListener {
            onCountryTouched(it as ImageView)
        }
//
//        imageColombiaBack.layoutParams.width=imageColombia.layoutParams.width
//        imageColombiaBack.layoutParams.height=imageColombia.layoutParams.height
//        imagePeruBack.layoutParams.width=imagePeru.layoutParams.width
//        imagePeruBack.layoutParams.height=imagePeru.layoutParams.height
//        imageBrasilBack.layoutParams.width=imageBrasil.layoutParams.width
//        imageBrasilBack.layoutParams.height=imageBrasil.layoutParams.height
//        imageChileBack.layoutParams.width=imageChile.layoutParams.width
//        imageChileBack.layoutParams.height=imageChile.layoutParams.height
//        imageArgentinaBack.layoutParams.width=imageArgentina.layoutParams.width
//        imageArgentinaBack.layoutParams.height=imageArgentina.layoutParams.height
//        imageUruguayBack.layoutParams.width=imageUruguay.layoutParams.width
//        imageUruguayBack.layoutParams.height=imageUruguay.layoutParams.height

        imageColombia.x=220F*widthRelation
        imageColombia.y=245F*heightRelation
        imagePeru.x=225F*widthRelation
        imagePeru.y=285F*heightRelation
        imageBrasil.x=270F*widthRelation
        imageBrasil.y=250F*heightRelation
        imageChile.x=235F*widthRelation
        imageChile.y=320F*heightRelation
        imageArgentina.x=245F*widthRelation
        imageArgentina.y=imageChile.y
        imageUruguay.x=285F*widthRelation
        imageUruguay.y=imageChile.y

//        imageColombiaBack.x=imageColombia.x
//        imageColombiaBack.y=imageColombia.y
//        imagePeruBack.x=imagePeru.x
//        imagePeruBack.y=imagePeru.y
//        imageBrasilBack.x=imageBrasil.x
//        imageBrasilBack.y=imageBrasil.y
//        imageChileBack.x=imageChile.x
//        imageChileBack.y=imageChile.y
//        imageArgentinaBack.x=imageArgentina.x
//        imageArgentinaBack.y=imageArgentina.y
//        imageUruguayBack.x=imageUruguay.x
//        imageUruguayBack.y=imageUruguay.y

        // África
        imageSahara.layoutParams.width=(100*widthRelation).toInt()
        imageSahara.layoutParams.height=(75*heightRelation).toInt()
        imageEgipto.layoutParams.width=(140*widthRelation).toInt()
        imageEgipto.layoutParams.height=(70*heightRelation).toInt()
        imageEtiopia.layoutParams.width=(120*widthRelation).toInt()
        imageEtiopia.layoutParams.height=(50*heightRelation).toInt()
        imageZaire.layoutParams.width=(100*widthRelation).toInt()
        imageZaire.layoutParams.height=(59*heightRelation).toInt()
        imageSudafrica.layoutParams.width=(64*widthRelation).toInt()
        imageSudafrica.layoutParams.height=(69*heightRelation).toInt()

        imageSahara.x=440F*widthRelation
        imageSahara.y=295F*heightRelation
//        imageSahara.setOnClickListener {
//            onCountryTouched(it as ImageView)
//        }
        imageEgipto.x=505F*widthRelation
        imageEgipto.y=290F*heightRelation
//        imageEgipto.setOnClickListener {
//            onCountryTouched(it)
//        }
        imageEtiopia.x=505*widthRelation
        imageEtiopia.y=320F*heightRelation
//        imageEtiopia.setOnClickListener {
//            onCountryTouched(it)
//        }
        imageZaire.x=475F*widthRelation
        imageZaire.y=340F*heightRelation
//        imageZaire.setOnClickListener {
//            onCountryTouched(it)
//        }
        imageSudafrica.x=540F*widthRelation
        imageSudafrica.y=360F*heightRelation
//        imageSudafrica.setOnClickListener {
//            onCountryTouched(it)
//        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun screenTouched(view: View,event: MotionEvent): Boolean {

        val touchColor: Int = bitMapFullView.getPixel(event.getX().toInt(), event.getY().toInt())
        val redValue = Color.red(touchColor)
        val blueValue = Color.blue(touchColor)
        val greenValue = Color.green(touchColor)
        val countryName:String?=countryBackColors[redValue.toString()+blueValue.toString()+greenValue.toString()]
        if(countryName!=null)
            AlertDialog.Builder(activity!!)
                .setTitle("País: "+countryName)
                .setPositiveButton("OK") { _, _ -> }
                .create().show()
//        AlertDialog.Builder(activity!!)
//            .setTitle(redValue.toString()+blueValue.toString()+greenValue.toString())
//            .setPositiveButton("OK") { _, _ -> }
//            .create().show()

        return true
    }

    private fun onCountryTouched(country:ImageView){
        country.setImageResource(R.drawable.chile_light_blue)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MapFragment()
    }
}
