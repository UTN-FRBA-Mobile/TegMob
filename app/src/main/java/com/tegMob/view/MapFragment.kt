package com.tegMob.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.tegMob.R
import com.tegMob.utils.MyFragment
import com.tegMob.viewModel.MapViewModel
import org.json.JSONObject


class MapFragment : MyFragment() {
    private lateinit var viewModel: MapViewModel
    private lateinit var countriesData: JSONObject

    private var windowHeight: Int = 0
    private var windowWidth: Int = 0
    private val displayMetrics = DisplayMetrics()

    companion object {
        fun newInstance() = MapFragment().apply {
            arguments = Bundle()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initCountries()

        view.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                viewModel.screenTouched(v, event)
            }
            true
        }

        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        windowWidth = displayMetrics.widthPixels
        windowHeight = displayMetrics.heightPixels

        viewModel.run { Map(view, windowWidth, windowHeight, countriesData) }
        //        windowWidth = mapaBack.layoutParams.width
        //        windowHeight = mapaBack.layoutParams.height


        //        if (windowWidthHeightRelation > 1.8F) {
        //            widthRelation = windowWidth / 703F
        //            xRelation = windowWidth / 810F
        //        }


    }

    /**
     * Busca la data inicial de los países, jugadores que posee y cantidad de ejércitos de cada uno de ellos
     */
    private fun initCountries() {
        //llama al servicio del backend
        val receivedData: String = mockupDataCountries()
        val receivedObj = JSONObject(receivedData)
        countriesData = receivedObj.getJSONObject("countries")
    }

    override fun getPassedData() {
        TODO("Not yet implemented")
    }

    override fun initViewModel() {
        viewModel = MapViewModel()
        context?.let { viewModel.init(this, listener, it) }
    }



    private fun mockupDataCountries(): String {
        var data = "{\"countries\":{" +
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
                "}}"



        return data
    }


}
