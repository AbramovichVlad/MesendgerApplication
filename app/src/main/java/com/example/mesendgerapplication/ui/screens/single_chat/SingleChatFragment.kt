package com.example.mesendgerapplication.ui.screens.single_chat

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.AbsListView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.database.*
import com.example.mesendgerapplication.databinding.FragmentSingleChatBinding
import com.example.mesendgerapplication.models.CommonModel
import com.example.mesendgerapplication.models.UserModel
import com.example.mesendgerapplication.ui.message_recycler_view.views.AppViewFactory
import com.example.mesendgerapplication.ui.screens.BaseFragment
import com.example.mesendgerapplication.ui.screens.main_list.MainListFragment
import com.example.mesendgerapplication.ui.screens.setings.ChangeNameFragment
import com.example.mesendgerapplication.utilities.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DatabaseReference
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var binding: FragmentSingleChatBinding

    private lateinit var mListnerInfoToolbar: AppValueEventListener
    private lateinit var mRecivingUser: UserModel
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
    private lateinit var mBottomBehavior: BottomSheetBehavior<*>

    override fun onResume() {
        super.onResume()
        initFields()
        initToolbar()
        initRecycleView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSingleChatBinding.bind(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initFields() {
        setHasOptionsMenu(true)
        mBottomBehavior = BottomSheetBehavior.from(binding.choiseUploadInclude.bottomSheetChoise)
        mBottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        mAppVoiceRecorder = AppVoiceRecorder()
        mSwipeRefreshLayout = binding.chatSwipeRefresh
        mLinerLayout = LinearLayoutManager(this.context)
        binding.chatInputMessage.addTextChangedListener(AppTextWatcher {
            val strText = binding.chatInputMessage.text.toString()
            if (strText.isEmpty() || strText == "Запись") {
                binding.chatBtnSendMessage.visibility = View.GONE
                binding.chatBtnAttach.visibility = View.VISIBLE
                binding.chatBtnVoice.visibility = View.VISIBLE
            } else {
                binding.chatBtnSendMessage.visibility = View.VISIBLE
                binding.chatBtnAttach.visibility = View.GONE
                binding.chatBtnVoice.visibility = View.GONE
            }
        })
        binding.chatBtnAttach.setOnClickListener { attach() }
        CoroutineScope(Dispatchers.IO).launch {
            binding.chatBtnVoice.setOnTouchListener { view, event ->
                if (checkPremision(RECORD_AUDIO)) {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        binding.chatInputMessage.setText("Запись")
                        binding.chatBtnVoice.setColorFilter(
                            ContextCompat.getColor(
                                APP_ACTIVITY,
                                R.color.primary
                            )
                        )
                        val mesagesKey = getMessageKey(contact.id)
                        mAppVoiceRecorder.startRecord(mesagesKey)
                    } else if (event.action == MotionEvent.ACTION_UP) {
                        //stop record
                        binding.chatInputMessage.setText("")
                        binding.chatBtnVoice.colorFilter = null
                        mAppVoiceRecorder.stopRecord { file, messageKey ->
                            uploadFileToStorage(
                                Uri.fromFile(file),
                                messageKey,
                                contact.id,
                                TYPE_MESSAGE_VOICE
                            )
                            mSmoothScrollToPosition = true
                        }
                    }
                }
                true
            }
        }
    }

    private fun attach() {
        mBottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.choiseUploadInclude.btnAttachImage.setOnClickListener { attachImage() }
        binding.choiseUploadInclude.btnAttachFile.setOnClickListener { attachFile() }
    }

    private fun attachFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    private fun attachImage() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(250, 250)
            .start(APP_ACTIVITY, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val uri = CropImage.getActivityResult(data).uri
                    val mesagesKey = getMessageKey(contact.id)
                    uploadFileToStorage(uri, mesagesKey, contact.id, TYPE_MESSAGE_IMAGE)
                    mSmoothScrollToPosition = true
                }
                PICK_FILE_REQUEST_CODE -> {
                    val uri = data.data
                    val mesagesKey = getMessageKey(contact.id)
                    val fileNmae = getFileNameFromUri(uri!!)
                    uploadFileToStorage(uri, mesagesKey, contact.id, TYPE_MESSAGE_FILE, fileNmae)
                    mSmoothScrollToPosition = true
                }
            }
        }
    }

    private fun initRecycleView() {
        mRecyclerView = binding.chatRecyclerView
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
        binding.toolBarInclude.blockToolbarInfo.visibility = View.VISIBLE
        mListnerInfoToolbar = AppValueEventListener {
            mRecivingUser = it.getUserModel()
            initInfoToolbar()
        }
        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mRefUser.addValueEventListener(mListnerInfoToolbar)
        binding.chatBtnSendMessage.setOnClickListener {
            val message = binding.chatInputMessage.text.toString()
            if (message.isEmpty()) {
                showToast(getString(R.string.toast_input_message))
            } else {
                sendMessage(message, contact.id, TYPE_TEXT) {
                    saveToMainList(contact.id, TYPE_CHAT)
                    binding.chatInputMessage.setText("")
                }
            }
        }
    }

    private fun initInfoToolbar() {
        if (mRecivingUser.fullname.isEmpty()) {
            binding.toolBarInclude.toolbarContactChatFullName.text = contact.fullname
        } else {
            binding.toolBarInclude.toolbarContactChatFullName.text = mRecivingUser.fullname
        }
        binding.toolBarInclude.toolbarChatImage.downloadAndSetImage(mRecivingUser.photoUrl)
        binding.toolBarInclude.toolbarContactChatStatus.text = mRecivingUser.state
    }

    override fun onPause() {
        super.onPause()
        binding.toolBarInclude.blockToolbarInfo.visibility = View.GONE
        mRefUser.removeEventListener(mListnerInfoToolbar)
        mRefMessages.removeEventListener(mMessagesListner)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        activity?.menuInflater?.inflate(R.menu.single_chat_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        R.id.menu_clear_chat -> clearChat(contact.id){
            showToast(getString(R.string.chat_clear_toast))
            replaceFragment(MainListFragment())
        }
            R.id.menu_delete_chat -> deleteChat(contact.id){
                showToast(getString(R.string.chat_delet_toast))
                replaceFragment(MainListFragment())
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mAppVoiceRecorder.releseReorder()
        mAdapter.destroy()
    }
}