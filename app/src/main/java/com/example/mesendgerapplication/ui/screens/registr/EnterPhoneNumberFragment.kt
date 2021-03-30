package com.example.mesendgerapplication.ui.screens.registr

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.database.AUTH
import com.example.mesendgerapplication.databinding.FragmentEnterPhoneNumberBinding
import com.example.mesendgerapplication.utilities.APP_ACTIVITY
import com.example.mesendgerapplication.utilities.replaceFragment
import com.example.mesendgerapplication.utilities.restartActivity
import com.example.mesendgerapplication.utilities.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private lateinit var mPhoneNumber: String
    private lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var binding: FragmentEnterPhoneNumberBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEnterPhoneNumberBinding.bind(view)
    }

    override fun onStart() {
        super.onStart()
        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credentail: PhoneAuthCredential) {
                AUTH.signInWithCredential(credentail).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("tagEnterPhoneNumber", "onVerificationCompleted isSuccessful ")
                        showToast("Добро пожааловат")
                        restartActivity()
                    } else {
                        showToast(it.exception?.message.toString())
                        Log.d("tagEnterPhoneNumber", "onVerificationCompleted else ")
                    }
                }
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mPhoneNumber, id))
                Log.d("tagEnterPhoneNumber", "onCodeSent")
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
                Log.d("tagEnterPhoneNumber", "onVerificationFailed")
            }

        }

        binding.registrBtnNext.setOnClickListener {
            sendCode()
        }
    }

    private fun sendCode() {
        if (binding.registrInputPhoneNumber.text.toString().isEmpty()) {
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
        mPhoneNumber = binding.registrInputPhoneNumber.text.toString()
        val options = PhoneAuthOptions.newBuilder(AUTH)
            .setPhoneNumber(mPhoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(APP_ACTIVITY) // Activity (for callback binding)
            .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun showProgress() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.progressRegistration.visibility = View.VISIBLE
        }
    }

}