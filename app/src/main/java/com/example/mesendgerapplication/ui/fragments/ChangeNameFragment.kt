package com.example.mesendgerapplication.ui.fragments

import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.database.USER
import com.example.mesendgerapplication.database.setFullName
import com.example.mesendgerapplication.utilities.*
import kotlinx.android.synthetic.main.fragment_change_name.*


class ChangeNameFragment : ChangeBaseFragment(R.layout.fragment_change_name) {

    override fun onResume() {
        super.onResume()
        initFullNameList()
    }

    private fun initFullNameList() {
        val fullNameList = USER.fullname.split(" ")
        if (fullNameList.size > 1) {
            setiings_change_name.setText(fullNameList[0])
            setiings_change_surname.setText(fullNameList[1])
        }
    }


    override fun change() {
        val name = setiings_change_name.text.toString()
        val surName = setiings_change_surname.text.toString()
        if (name.isEmpty()) {
            showToast(getString(R.string.settings_toast_input_name))
        } else {
            val fullName = "$name $surName"
            setFullName(fullName)
        }
    }
}