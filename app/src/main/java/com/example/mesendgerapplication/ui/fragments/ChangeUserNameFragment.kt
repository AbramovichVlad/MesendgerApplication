package com.example.mesendgerapplication.ui.fragments

import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.database.*
import com.example.mesendgerapplication.utilities.*
import kotlinx.android.synthetic.main.fragment_change_user_name.*
import java.util.*


class ChangeUserNameFragment : ChangeBaseFragment(R.layout.fragment_change_user_name) {

    lateinit var mNewUsername: String

    override fun onResume() {
        super.onResume()
        settings_et_changeUser_name.setText(USER.username)
    }


    override fun change() {
        mNewUsername = settings_et_changeUser_name.text.toString().toLowerCase(Locale.getDefault())
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