package com.example.mesendgerapplication.ui.screens.setings

import android.os.Bundle
import android.view.View
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.database.USER
import com.example.mesendgerapplication.database.setBioToDataBase
import com.example.mesendgerapplication.databinding.FragmentChangeBioBinding
import com.example.mesendgerapplication.ui.screens.base.ChangeBaseFragment

class ChangeBIoFragment : ChangeBaseFragment(R.layout.fragment_change_bio) {

    private lateinit var binding: FragmentChangeBioBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChangeBioBinding.bind(view)
    }


    override fun onResume() {
        super.onResume()
        binding.settingsEtChangeBio.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio = binding.settingsEtChangeBio.text.toString()
        setBioToDataBase(newBio)
    }
}