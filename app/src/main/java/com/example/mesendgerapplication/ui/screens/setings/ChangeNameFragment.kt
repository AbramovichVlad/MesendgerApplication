package com.example.mesendgerapplication.ui.screens.setings

import android.os.Bundle
import android.view.View
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.database.USER
import com.example.mesendgerapplication.database.setFullName
import com.example.mesendgerapplication.databinding.FragmentChangeNameBinding
import com.example.mesendgerapplication.ui.screens.base.ChangeBaseFragment
import com.example.mesendgerapplication.utilities.showToast


class ChangeNameFragment : ChangeBaseFragment(R.layout.fragment_change_name) {

    private lateinit var binding: FragmentChangeNameBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChangeNameBinding.bind(view)
    }

    override fun onResume() {
        super.onResume()
        initFullNameList()
    }

    private fun initFullNameList() {
        val fullNameList = USER.fullname.split(" ")
        if (fullNameList.size > 1) {
            binding.setiingsChangeName.setText(fullNameList[0])
            binding.setiingsChangeSurname.setText(fullNameList[1])
        }
    }


    override fun change() {
        val name = binding.setiingsChangeName.text.toString()
        val surName = binding.setiingsChangeSurname.text.toString()
        if (name.isEmpty()) {
            showToast(getString(R.string.settings_toast_input_name))
        } else {
            val fullName = "$name $surName"
            setFullName(fullName)
        }
    }
}