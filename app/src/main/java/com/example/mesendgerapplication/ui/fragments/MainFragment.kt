package com.example.mesendgerapplication.ui.fragments

import androidx.fragment.app.Fragment
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.utilities.APP_ACTIVITY
import com.example.mesendgerapplication.utilities.hideKeyboard


class MainFragment : Fragment(R.layout.fragment_chats) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Messanger"
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
    }


}