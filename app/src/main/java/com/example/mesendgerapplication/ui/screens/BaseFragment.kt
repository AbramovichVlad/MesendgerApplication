package com.example.mesendgerapplication.ui.screens

import androidx.fragment.app.Fragment
import com.example.mesendgerapplication.utilities.APP_ACTIVITY
import com.example.mesendgerapplication.utilities.hideKeyboard


open class BaseFragment( layout : Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
        hideKeyboard()
    }

}