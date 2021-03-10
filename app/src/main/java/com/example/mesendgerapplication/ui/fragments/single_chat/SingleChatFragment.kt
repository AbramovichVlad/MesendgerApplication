package com.example.mesendgerapplication.ui.fragments.single_chat

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.models.CommonModel
import com.example.mesendgerapplication.models.UserModel
import com.example.mesendgerapplication.ui.fragments.BaseFragment
import com.example.mesendgerapplication.utilities.*
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_info.view.*


class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mListnerInfoToolbar: AppValueEventListener
    private lateinit var mRecivingUser: UserModel
    private lateinit var mToolbarInfo: View
    private lateinit var mRefUser: DatabaseReference
    private lateinit var mRefMessages : DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListner : AppValueEventListener
    private  var mListMessages = emptyList<CommonModel>()

    override fun onResume() {
        super.onResume()
        initToolbar()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = chat_recycler_view
        mAdapter = SingleChatAdapter()
        mRefMessages = REF_DATABASE_ROOT
            .child(NODE_MESSAGE)
            .child(CURRENT_UID)
            .child(contact.id)
        mRecyclerView.adapter = mAdapter
        mMessagesListner = AppValueEventListener { dataSnapshot ->
            mListMessages = dataSnapshot.children.map { it.getCommonModel() }
            mAdapter.setList(mListMessages)
            mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
        }
        mRefMessages.addValueEventListener(mMessagesListner)
    }

    private fun initToolbar() {
        mToolbarInfo = APP_ACTIVITY.mToolbar.toolbar_info
        mToolbarInfo.visibility = View.VISIBLE
        mListnerInfoToolbar = AppValueEventListener {
            mRecivingUser = it.getUserModel()
            initInfoToolbar()
        }
        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mRefUser.addValueEventListener(mListnerInfoToolbar)
        chat_btn_send_message.setOnClickListener {
            val message = chat_input_message.text.toString()
            if(message.isEmpty()){
                showToast(getString(R.string.toast_input_message))
            } else {
                sendMessage(message, contact.id, TYPE_TEXT){
                    chat_input_message.setText("")
                }
            }
        }
    }

    private fun initInfoToolbar() {
        if (mRecivingUser.fullname.isEmpty()) {
            mToolbarInfo.toolbar_contact_chat_fullName.text = contact.fullname
        } else {
            mToolbarInfo.toolbar_contact_chat_fullName.text = mRecivingUser.fullname
        }
        mToolbarInfo.toolbar_chat_image.downloadAndSetImage(mRecivingUser.photoUrl)
        mToolbarInfo.toolbar_contact_chat_status.text = mRecivingUser.state
    }

    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        mRefUser.removeEventListener(mListnerInfoToolbar)
        mRefMessages.removeEventListener(mMessagesListner)
    }
}