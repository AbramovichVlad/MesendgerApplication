package com.example.mesendgerapplication.ui.screens.setings

import android.os.Bundle
import android.view.View
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.database.*
import com.example.mesendgerapplication.databinding.FragmentChangeUserNameBinding
import com.example.mesendgerapplication.ui.screens.base.ChangeBaseFragment
import com.example.mesendgerapplication.utilities.AppValueEventListener
import com.example.mesendgerapplication.utilities.showToast
import java.util.*


class ChangeUserNameFragment : ChangeBaseFragment(R.layout.fragment_change_user_name) {

    lateinit var mNewUsername: String
    private lateinit var binding: FragmentChangeUserNameBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChangeUserNameBinding.bind(view)
    }

    override fun onResume() {
        super.onResume()
        binding.settingsEtChangeUserName.setText(USER.username)
    }


    override fun change() {
        mNewUsername =
            binding.settingsEtChangeUserName.text.toString().toLowerCase(Locale.getDefault())
        if (mNewUsername.isEmpty()) {
            showToast("Поле пустое")
        } else {
            REF_DATABASE_ROOT.child(
                NODE_USERS
            ).addListenerForSingleValueEvent(AppValueEventListener {
                if (it.hasChild(mNewUsername)) {
                    showToast("Такой пользователь уже существует")
                } else {
                    changeUsername()
                }
            })
        }
    }

    private fun changeUsername() {

        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername).setValue(CURRENT_UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUsername(mNewUsername)
                }
            }
    }
}