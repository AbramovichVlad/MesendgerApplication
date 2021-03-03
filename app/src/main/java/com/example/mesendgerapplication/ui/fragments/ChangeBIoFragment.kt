package com.example.mesendgerapplication.ui.fragments

import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.utilities.*
import kotlinx.android.synthetic.main.fragment_change_bio.*

class ChangeBIoFragment : ChangeBaseFragment(R.layout.fragment_change_bio) {


    override fun onResume() {
        super.onResume()
        settings_et_change_bio.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio = settings_et_change_bio.text.toString()
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHOLD_BIO).setValue(newBio)
            .addOnCompleteListener {
                showToast(getString(R.string.toast_data_update))
                USER.bio = newBio
                fragmentManager?.popBackStack()
            }
    }

}