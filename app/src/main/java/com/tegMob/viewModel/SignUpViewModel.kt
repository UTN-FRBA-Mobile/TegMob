package com.tegMob.viewModel

import android.os.Bundle
import android.widget.Toast
import com.tegMob.connectivity.ClientBuilder
import com.tegMob.connectivity.UsersRouter
import com.tegMob.models.User
import com.tegMob.utils.MyViewModel
import com.tegMob.view.InitialFragment
import kotlinx.android.synthetic.main.sign_up_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : MyViewModel() {
    var firstName : String = ""
    var lastName : String = ""
    var email : String = ""
    var userNameSignUp : String = ""
    var passwordSignUp : String = ""

    fun signUpFinishButton(){
        if (myFragment.completedFields()) {
            val newUser = getUser()

            val request = ClientBuilder.UsersClientBuilder.buildService(UsersRouter::class.java)

            val  call = request.createUser(newUser)

            call.enqueue(object : Callback<Unit>{
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        print("user created")
                        Toast.makeText(myContext, "User creation successful", Toast.LENGTH_LONG).show()
                        myListener?.showFragment(InitialFragment())
                    }
                }

                override fun onFailure(call: Call<Unit>, error: Throwable) {
                Toast.makeText(myContext, "User creation failed", Toast.LENGTH_SHORT).show()
                }
            })

                } else {
                Toast.makeText(myContext, "Por favor, complete los campos", Toast.LENGTH_SHORT).show()
            }
    }

    override fun setDataToPass(): Bundle {
        TODO("Not yet implemented")
    }

    private fun getUser() = User (
        username = myFragment.usernameSignUp.toString(),
        password = myFragment.passwordSignUp.toString(),
        firstname = myFragment.firstName.toString(),
        lastname = myFragment.lastName.toString()
        )
}
