package com.tegMob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tegMob.view.InitialFragment

class MainActivity : AppCompatActivity() {
    private val fragmentManager = supportFragmentManager
    private val inicioFragment = InitialFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, inicioFragment)
        fragmentTransaction.commit()
    }
}
