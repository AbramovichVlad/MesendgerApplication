package com.example.mesendgerapplication.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.databinding.ActivityRegistrBinding
import com.example.mesendgerapplication.ui.fragments.EnterPhoneNumberFragment
import com.example.mesendgerapplication.utilities.initFirebase
import com.example.mesendgerapplication.utilities.replaceActivity
import com.example.mesendgerapplication.utilities.replaceFragment

class RegistrActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityRegistrBinding
    private lateinit var mToolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegistrBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initFirebase()
    }

    override fun onStart() {
        super.onStart()
        mToolbar = mBinding.registrToolbar
        setSupportActionBar(mToolbar)
        title = getString(R.string.registr_title_your_phone)
        replaceFragment(EnterPhoneNumberFragment(), R.id.registrDataContainer, false)
    }
}