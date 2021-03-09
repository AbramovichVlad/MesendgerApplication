package com.example.mesendgerapplication.ui.fragments

import android.view.View
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.models.CommonModel
import com.example.mesendgerapplication.models.UserModel
import com.example.mesendgerapplication.utilities.*
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.contact_item.view.*
import kotlinx.android.synthetic.main.toolbar_info.view.*
import kotlinx.android.synthetic.main.toolbar_info.view.toolbar_contact_chat_fullName
import kotlinx.android.synthetic.main.toolbar_info.view.toolbar_contact_chat_status


class SingleChatFragment( private val contact: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mListnerInfoToolbar : AppValueEventListener
    private lateinit var mRecivingUser : UserModel
    private lateinit var mToolbarInfo : View
    private lateinit var mRefUser : DatabaseReference

    override fun onResume() {
        super.onResume()
        mToolbarInfo =  APP_ACTIVITY.mToolbar.toolbar_info
        mToolbarInfo.visibility = View.VISIBLE
        mListnerInfoToolbar = AppValueEventListener{
            mRecivingUser = it.getUserModel()
            initInfoToolbar()
        }
        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mRefUser.addValueEventListener(mListnerInfoToolbar)
    }

    private fun initInfoToolbar() {
       mToolbarInfo.toolbar_chat_image.downloadAndSetImage(mRecivingUser.photoUrl)
        mToolbarInfo.toolbar_contact_chat_fullName.text = mRecivingUser.fullname
        mToolbarInfo.toolbar_contact_chat_status.text = mRecivingUser.state
    }

    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        mRefUser.removeEventListener(mListnerInfoToolbar)
    }
}