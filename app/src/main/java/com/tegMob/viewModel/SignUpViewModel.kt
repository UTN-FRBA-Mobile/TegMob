package com.tegMob.viewModel

import android.os.Bundle
import android.widget.Toast
import com.tegMob.connectivity.TegMobClient
import com.tegMob.connectivity.routers.UsersRouter
import com.tegMob.connectivity.dtos.UserDTOs
import com.tegMob.utils.MyViewModel
import com.tegMob.view.InitialFragment
import kotlinx.android.synthetic.main.sign_up_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : MyViewModel() {
    private var firstName = ""
    private var lastName = ""
    private var userNameSignUp = ""
    private var passwordSignUp = ""
    private val usersClient = TegMobClient.buildService(UsersRouter::class.java)

    fun signUpFinishButton(){
        if (myFragment.completedFields()) {

            val newUser = getNewUserData()

            val  call = usersClient.createUser(newUser)

            call.enqueue(object : Callback<Unit>{
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful && response.code() == 200) {
                        Toast.makeText(myContext, "User creation successful", Toast.LENGTH_LONG).show()
                        myListener?.showFragment(InitialFragment())
                    } else {
                        Toast.makeText(myContext, "User creation failed", Toast.LENGTH_SHORT).show()
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

    fun saveData(){
        firstName = myFragment.firstName?.text.toString()
        lastName = myFragment.lastName?.text.toString()
        userNameSignUp = myFragment.usernameSignUp?.text.toString()
        passwordSignUp = myFragment.passwordSignUp?.text.toString()
    }

    fun loadData(){
        myFragment.firstName.setText(firstName)
        myFragment.lastName.setText(lastName)
        myFragment.usernameSignUp.setText(userNameSignUp)
    }
    private fun getNewUserData() = UserDTOs.NewUser (
        username = myFragment.usernameSignUp.text.toString(),
        password = myFragment.passwordSignUp.text.toString(),
        firstName = myFragment.firstName.text.toString(),
        lastName = myFragment.lastName.text.toString()
        )
}
