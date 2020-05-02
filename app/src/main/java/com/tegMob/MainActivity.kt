package com.tegMob

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tegMob.utils.FragmentUtil
import com.tegMob.view.InitialFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FragmentUtil.loadFirstFragment(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

}
