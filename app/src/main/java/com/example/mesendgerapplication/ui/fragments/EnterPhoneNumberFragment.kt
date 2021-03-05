package com.example.mesendgerapplication.ui.fragments

import android.view.View
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import com.example.mesendgerapplication.MainActivity
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.activitys.RegistrActivity
import com.example.mesendgerapplication.utilities.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private lateinit var mPhoneNumber: String
    private lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onStart() {
        super.onStart()
        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credentail: PhoneAuthCredential) {
                AUTH.signInWithCredential(credentail).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast("Добро пожааловат")
                        (activity as RegistrActivity).replaceActivity(MainActivity())
                    } else showToast(it.exception?.message.toString())
                }
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mPhoneNumber, id), R.id.registrDataContainer)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
            }

        }

        registr_btn_next.setOnClickListener {
            sendCode()
        }
    }

    private fun sendCode() {
        if (registr_input_phone_number.text.toString().isEmpty()) {
            showToast(getString(R.string.registr_toast_enter_phone))
        } else {
            authUser()
          //  showProgress()
        }
    }

    private fun authUser() {
        mPhoneNumber = registr_input_phone_number.text.toString()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mPhoneNumber,
            60,
            TimeUnit.SECONDS,
            (activity as RegistrActivity),
            mCallBack
        )
    }

    private fun showProgress(){
        CoroutineScope(Dispatchers.Main).launch {
            progress_registration.visibility = View.VISIBLE
        }
//        Thread(Runnable {
//            (activity as RegistrActivity).runOnUiThread(java.lang.Runnable {
//                progress_registration.visibility = View.VISIBLE
//            })
//        }).start()
    }

}