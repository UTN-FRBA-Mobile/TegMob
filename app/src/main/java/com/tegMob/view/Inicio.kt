package com.tegMob.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tegMob.R
import com.tegMob.viewModel.InicioViewModel


class Inicio : Fragment() {
    private lateinit var viewModel: InicioViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.inicio_fragment, container, false)
    }

}
