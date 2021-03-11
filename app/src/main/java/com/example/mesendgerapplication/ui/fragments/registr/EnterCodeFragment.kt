package com.example.mesendgerapplication.ui.fragments.registr

import android.util.Log
import androidx.fragment.app.Fragment
import com.example.mesendgerapplication.MainActivity
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.utilities.*
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*


class EnterCodeFragment(val mPhoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        Log.d("tagEnterPhoneNumber","onStart EnterCodeFragment")
        super.onStart()
        APP_ACTIVITY.title = mPhoneNumber
        registr_input_code.addTextChangedListener(AppTextWatcher {

            val strCode = registr_input_code.text.toString()
            if (strCode.length == 6) {
                verifiCode()
            }
        })
    }

    private fun verifiCode() {
        Log.d("tagEnterPhoneNumber","authverifiCodeUser")
        val code = registr_input_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)

        AUTH.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()
                val dateMap = mutableMapOf<String, Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = mPhoneNumber
                dateMap[CHILD_USER_NAME] = uid
                REF_DATABASE_ROOT.child(NODE_PHONES).child(mPhoneNumber).setValue(uid)
                    .addOnFailureListener {
                        showToast(it.message.toString())
                    }
                    .addOnSuccessListener {
                        REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                            .addOnSuccessListener {
                                showToast("Добро пожааловат")
                                restartActivity()
                            }
                            .addOnFailureListener {
                                showToast(it.message.toString())
                            }
                    }
            } else showToast(it.exception?.message.toString())
        }
    }

}