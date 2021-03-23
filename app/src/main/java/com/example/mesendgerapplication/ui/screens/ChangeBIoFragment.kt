package com.example.mesendgerapplication.ui.screens

import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.database.USER
import com.example.mesendgerapplication.database.setBioToDataBase
import kotlinx.android.synthetic.main.fragment_change_bio.*

class ChangeBIoFragment : ChangeBaseFragment(R.layout.fragment_change_bio) {


    override fun onResume() {
        super.onResume()
        settings_et_change_bio.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio = settings_et_change_bio.text.toString()
        setBioToDataBase(newBio)
    }
}