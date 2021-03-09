package com.example.mesendgerapplication.ui.fragments

import android.R.attr.phoneNumber
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mesendgerapplication.MainActivity
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.activitys.RegistrActivity
import com.example.mesendgerapplication.utilities.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
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
                        Log.d("tagEnterPhoneNumber", "onVerificationCompleted isSuccessful ")
                        showToast("Добро пожааловат")
                        (activity as RegistrActivity).replaceActivity(MainActivity())
                    } else {
                        showToast(it.exception?.message.toString())
                        Log.d("tagEnterPhoneNumber", "onVerificationCompleted else ")
                    }
                }
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mPhoneNumber, id), R.id.registr_data_container)
                Log.d("tagEnterPhoneNumber", "onCodeSent")
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
                Log.d("tagEnterPhoneNumber", "onVerificationFailed")
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
            Log.d("tagEnterPhoneNumber", "sendCode")
            authUser()
          //  showProgress()
        }
    }

    private fun authUser() {
        /* Инициализация */
        Log.d("tagEnterPhoneNumber", "authUser")
//        mPhoneNumber = registr_input_phone_number.text.toString()
//        Log.d("tagEnterPhoneNumber", mPhoneNumber)
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//            mPhoneNumber,
//            60,
//            TimeUnit.SECONDS,
//            APP_ACTIVITY,
//            mCallBack
//        )
        mPhoneNumber = registr_input_phone_number.text.toString()
        val options = PhoneAuthOptions.newBuilder(AUTH)
            .setPhoneNumber(mPhoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(APP_ACTIVITY) // Activity (for callback binding)
            .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
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