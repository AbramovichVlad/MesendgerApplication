package com.example.mesendgerapplication.ui.fragments.single_chat

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.MotionEvent
import android.view.View
import android.widget.AbsListView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.database.*
import com.example.mesendgerapplication.models.CommonModel
import com.example.mesendgerapplication.models.UserModel
import com.example.mesendgerapplication.ui.fragments.BaseFragment
import com.example.mesendgerapplication.ui.fragments.message_recycler_view.views.AppViewFactory
import com.example.mesendgerapplication.utilities.*
import com.google.firebase.database.DatabaseReference
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_info.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mListnerInfoToolbar: AppValueEventListener
    private lateinit var mRecivingUser: UserModel
    private lateinit var mToolbarInfo: View
    private lateinit var mRefUser: DatabaseReference
    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListner: AppChildEventListener
    private var mCountMessages = 10
    private var mIsScrolling = false
    private var mSmoothScrollToPosition = true
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mLinerLayout: LinearLayoutManager
    private lateinit var mAppVoiceRecorder: AppVoiceRecorder

    override fun onResume() {
        super.onResume()
        initFields()
        initToolbar()
        initRecycleView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initFields() {
        mAppVoiceRecorder = AppVoiceRecorder()
        mSwipeRefreshLayout = chat_swipe_refresh
        mLinerLayout = LinearLayoutManager(this.context)
        chat_input_message.addTextChangedListener(AppTextWatcher {
            val strText = chat_input_message.text.toString()
            if (strText.isEmpty() || strText == "Запись") {
                chat_btn_send_message.visibility = View.GONE
                chat_btn_attach.visibility = View.VISIBLE
                chat_btn_voice.visibility = View.VISIBLE
            } else {
                chat_btn_send_message.visibility = View.VISIBLE
                chat_btn_attach.visibility = View.GONE
                chat_btn_voice.visibility = View.GONE
            }
        })
        chat_btn_attach.setOnClickListener { attachFile() }
        CoroutineScope(Dispatchers.IO).launch {
            chat_btn_voice.setOnTouchListener { view, event ->
                if (checkPremision(RECORD_AUDIO)) {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        chat_input_message.setText("Запись")
                        chat_btn_voice.setColorFilter(ContextCompat.getColor(APP_ACTIVITY, R.color.primary))
                        val mesagesKey = getMessageKey(contact.id)
                        mAppVoiceRecorder.startRecord(mesagesKey)
                    } else if (event.action == MotionEvent.ACTION_UP) {
                        //stop record
                        chat_input_message.setText("")
                        chat_btn_voice.colorFilter = null
                        mAppVoiceRecorder.stopRecord{ file, messageKey ->
                            uploadFileToStorage(Uri.fromFile(file), messageKey, contact.id, TYPE_MESSAGE_VOICE)
                            mSmoothScrollToPosition = true
                        }
                    }
                }
                true
            }
        }
    }

    private fun attachFile() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(250, 250)
            .start(APP_ACTIVITY, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&
            resultCode == Activity.RESULT_OK &&
            data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            val mesagesKey = getMessageKey(contact.id)
            uploadFileToStorage(uri, mesagesKey, contact.id, TYPE_MESSAGE_IMAGE)
            mSmoothScrollToPosition = true
        }
    }




    private fun initRecycleView() {
        mRecyclerView = chat_recycler_view
        mAdapter = SingleChatAdapter()
        mRefMessages = REF_DATABASE_ROOT
            .child(NODE_MESSAGE)
            .child(CURRENT_UID)
            .child(contact.id)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.layoutManager = mLinerLayout

        mMessagesListner = AppChildEventListener {
            val message = it.getCommonModel()
            if (mSmoothScrollToPosition) {
                mAdapter.addItemToBootm(AppViewFactory.getView(message)) {
                    mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
                }
            } else {
                mAdapter.addItemToTop(AppViewFactory.getView(message)) {
                    mSwipeRefreshLayout.isRefreshing = false
                }
            }
        }

        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListner)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0 && mLinerLayout.findFirstVisibleItemPosition() <= 3) updateData()
            }
        })
        mSwipeRefreshLayout.setOnRefreshListener { updateData() }
    }

    private fun updateData() {
        mSmoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages += 10
        mRefMessages.removeEventListener(mMessagesListner)
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListner)
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
            if (message.isEmpty()) {
                showToast(getString(R.string.toast_input_message))
            } else {
                sendMessage(message, contact.id, TYPE_TEXT) {
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

    override fun onDestroy() {
        super.onDestroy()
        mAppVoiceRecorder.releseReorder()
    }
}