package com.tegMob.viewModel

import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import com.tegMob.connectivity.TegMobClient
import com.tegMob.connectivity.routers.UsersRouter
import com.tegMob.connectivity.dtos.UserDTOs
import com.tegMob.utils.MyViewModel
import com.tegMob.view.InitialFragment
import com.tegMob.view.LoggedUserMainFragment
import com.tegMob.view.SignUpFragment
import kotlinx.android.synthetic.main.initial_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInViewModel : MyViewModel() {
    private var username = ""
    private var password = ""
    private val usersClient = TegMobClient.buildService(UsersRouter::class.java)


    fun loginButtonClick(){
        if (myFragment.completedFields()) {

            username = (myFragment as InitialFragment).username.text.toString()
            password = (myFragment as InitialFragment).password.text.toString()


            // logIn()
            val loggedUserOK = LoggedUserMainFragment()
            loggedUserOK.arguments = setDataToPass()
            myListener?.showFragment(loggedUserOK)



        } else {
            Toast.makeText(myContext, "Please fill empty fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun logIn() {
        val logInData = UserDTOs.UserLogin(username, password)
        val call = usersClient.loginUser(logInData)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful){
                    val loggedUserOK = LoggedUserMainFragment()
                    loggedUserOK.arguments = setDataToPass()
                    myListener?.showFragment(loggedUserOK)
                }
            }
            override fun onFailure(call: Call<Unit>, error: Throwable) {
                Toast.makeText(myContext, "Bad username or password", Toast.LENGTH_SHORT).show()
            }
        })
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