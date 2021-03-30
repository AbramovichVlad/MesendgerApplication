package com.example.mesendgerapplication.ui.screens.base

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.mesendgerapplication.MainActivity
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.utilities.APP_ACTIVITY

open class ChangeBaseFragment(layout: Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change_name -> change()
        }
        return true
    }

    open fun change() {

    }

}