package com.tegMob.viewModel

import android.os.Bundle
import androidx.core.os.bundleOf
import com.tegMob.utils.MyFragment
import com.tegMob.utils.MyViewModel
import com.tegMob.view.CreateNewGameFragment
import com.tegMob.view.GamesListFragment

class LoggedUserMainViewModel : MyViewModel(){

    var imageURI : String = ""
    var userId : String = ""
    var username : String = ""

    fun myProfileButtonClick(){
        TODO("TO DO: create MyProfile layout, view and viewModel")
        //myListener.showFragment()
    }

    fun newGameButtonClick() = buttonClick(CreateNewGameFragment())

    fun joinGameButtonClick() = buttonClick(GamesListFragment())

    override fun setDataToPass() = bundleOf(
            "imageURI" to imageURI,
            "userId" to userId,
            "user" to username
    )

}