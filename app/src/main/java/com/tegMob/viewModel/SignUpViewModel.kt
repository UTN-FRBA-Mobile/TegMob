package com.tegMob.viewModel

import android.os.Bundle
import android.widget.Toast
import com.tegMob.utils.MyViewModel
import com.tegMob.view.InitialFragment

class SignUpViewModel : MyViewModel() {
    var firstName : String = ""
    var lastName : String = ""
    var email : String = ""
    var userNameSignUp : String = ""
    var passwordSignUp : String = ""

    fun signUpFinishButton(){
        if (myFragment.completedFields()) {
            myListener?.showFragment(InitialFragment())
        } else {
            Toast.makeText(myContext, "Por favor, complete los campos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun setDataToPass(): Bundle {
        TODO("Not yet implemented")
    }
}
