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
    private var userid = ""
    private val usersClient = TegMobClient.buildService(UsersRouter::class.java)


    fun loginButtonClick(){
        if (myFragment.completedFields()) {
            username = (myFragment as InitialFragment).username.text.toString()
            password = (myFragment as InitialFragment).password.text.toString()
            logIn()
        } else {
            Toast.makeText(myContext, "Please fill empty fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun logIn() {
        val logInData = UserDTOs.UserLogin(username, password)
        val call = usersClient.loginUser(logInData)
        call.enqueue(object : Callback<UserDTOs.LoggedUserResponseDTO> {
            override fun onResponse(call: Call<UserDTOs.LoggedUserResponseDTO>, response: Response<UserDTOs.LoggedUserResponseDTO>) {
                if (response.isSuccessful && response.code() == 200){
                    userid = response.body()!!.id
                    val loggedUserOK = LoggedUserMainFragment()
                    loggedUserOK.arguments = setDataToPass()
                    myListener?.showFragment(loggedUserOK)
                } else {
                    Toast.makeText(myContext, "El usuario o contraseña es incorrecto/a", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserDTOs.LoggedUserResponseDTO>, error: Throwable) {
                Toast.makeText(myContext, "El usuario o contraseña es incorrecto/a", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun signUpButtonClick(){
        myListener?.showFragment(SignUpFragment())
    }

    //data to be passed to LoggedUserMainFragment
    override fun setDataToPass(): Bundle = bundleOf(
        "userId" to userid,
        "user" to username,
        "pass" to password
    )
}