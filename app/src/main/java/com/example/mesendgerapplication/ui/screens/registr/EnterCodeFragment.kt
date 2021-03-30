package com.example.mesendgerapplication.ui.screens.registr

import android.util.Log
import androidx.fragment.app.Fragment
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.database.*
import com.example.mesendgerapplication.databinding.FragmentEnterCodeBinding
import com.example.mesendgerapplication.utilities.*
import com.google.firebase.auth.PhoneAuthProvider


class EnterCodeFragment(val mPhoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {

    private lateinit var binding: FragmentEnterCodeBinding

    override fun onStart() {
        Log.d("tagEnterPhoneNumber", "onStart EnterCodeFragment")
        super.onStart()
        APP_ACTIVITY.title = mPhoneNumber
        binding.registrInputCode.addTextChangedListener(AppTextWatcher {

            val strCode = binding.registrInputCode.text.toString()
            if (strCode.length == 6) {
                verifiCode()
            }
        })
    }

    private fun verifiCode() {
        Log.d("tagEnterPhoneNumber", "authverifiCodeUser")
        val code = binding.registrInputCode.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)

        AUTH.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()
                val dateMap = mutableMapOf<String, Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = mPhoneNumber

                REF_DATABASE_ROOT
                    .child(NODE_USERS)
                    .child(uid).addListenerForSingleValueEvent(AppValueEventListener {
                        if (!it.hasChild(CHILD_USER_NAME)) {
                            dateMap[CHILD_USER_NAME] = uid
                        }
                        REF_DATABASE_ROOT
                            .child(NODE_PHONES)
                            .child(mPhoneNumber)
                            .setValue(uid)
                            .addOnFailureListener {
                                showToast(it.message.toString())
                            }
                            .addOnSuccessListener {
                                REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                                    .updateChildren(dateMap)
                                    .addOnSuccessListener {
                                        showToast("Добро пожааловат")
                                        restartActivity()
                                    }
                                    .addOnFailureListener {
                                        showToast(it.message.toString())
                                    }
                            }
                    })

            } else showToast(it.exception?.message.toString())
        }
    }
}
