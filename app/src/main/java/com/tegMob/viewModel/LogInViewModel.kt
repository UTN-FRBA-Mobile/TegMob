package com.tegMob.viewModel

import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import com.tegMob.utils.MyViewModel
import com.tegMob.view.InitialFragment
import com.tegMob.view.LoggedUserMainFragment
import com.tegMob.view.SignUpFragment
import kotlinx.android.synthetic.main.initial_fragment.*

class LogInViewModel : MyViewModel() {
    private var username = ""
    private var password = ""

    fun loginButtonClick(){
        if (myFragment.completedFields()) {

            username = (myFragment as InitialFragment).username.text.toString()
            password = (myFragment as InitialFragment).password.text.toString()

            val loggedUserOK = LoggedUserMainFragment()
            loggedUserOK.arguments = setDataToPass()
            myListener?.showFragment(loggedUserOK)

        } else {
            Toast.makeText(myContext, "Please fill empty fields", Toast.LENGTH_SHORT).show()
        }
    }

    fun signUpButtonClick(){
        myListener?.showFragment(SignUpFragment())
    }

    //data to be passed to LoggedUserMainFragment
    override fun setDataToPass(): Bundle = bundleOf(
        "userId" to 7,
        "user" to username,
        "pass" to password
    )
}