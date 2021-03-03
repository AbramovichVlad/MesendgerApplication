package com.example.mesendgerapplication.ui.fragments

import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.utilities.*
import kotlinx.android.synthetic.main.fragment_change_name.*


class ChangeNameFragment : ChangeBaseFragment(R.layout.fragment_change_name) {

    override fun onResume() {
        super.onResume()
       initFullNameList()
    }

    private fun initFullNameList(){
        val fullNameList  = USER.fullname.split(" ")
        if(fullNameList.size > 1){
            setiings_change_name.setText(fullNameList[0])
            setiings_change_surname.setText(fullNameList[1])
        }
    }


    override fun change() {
             val name = setiings_change_name.text.toString()
        val surName = setiings_change_surname.text.toString()
        if(name.isNullOrEmpty()){
            showToast(getString(R.string.settings_toast_input_name))
        }
        else{
            val fullName = "$name $surName"
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FULLNAME)
                .setValue(fullName).addOnCompleteListener {
                    if(it.isSuccessful){
                        showToast(getString(R.string.toast_data_update))
                        USER.fullname = fullName
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                        fragmentManager?.popBackStack()
                    }
                }

        }
    }
}