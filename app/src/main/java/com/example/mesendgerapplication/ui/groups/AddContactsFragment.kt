package com.example.mesendgerapplication.ui.groups

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.database.*
import com.example.mesendgerapplication.databinding.FragmentAddContactsBinding
import com.example.mesendgerapplication.databinding.FragmentMainListBinding
import com.example.mesendgerapplication.models.CommonModel
import com.example.mesendgerapplication.ui.screens.main_list.MainListAdapter
import com.example.mesendgerapplication.utilities.APP_ACTIVITY
import com.example.mesendgerapplication.utilities.AppValueEventListener
import com.example.mesendgerapplication.utilities.hideKeyboard
import com.example.mesendgerapplication.utilities.replaceFragment


class AddContactsFragment : Fragment(R.layout.fragment_add_contacts) {

    private lateinit var binding: FragmentAddContactsBinding
    private lateinit var mAdapter: AddContactsAdapter
    private val mRefMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGE).child(CURRENT_UID)
    private var mListItem = listOf<CommonModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddContactsBinding.bind(view)
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.add_users_in_group)
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
        initRecyclerView()
        binding.addContactsBtn.setOnClickListener {
            replaceFragment(CreateGroupFragment(listContacts))
        }
    }

    private fun initRecyclerView() {
        mAdapter = AddContactsAdapter()

        mRefMainList.addListenerForSingleValueEvent(AppValueEventListener {dataSnaphsot ->
            mListItem = dataSnaphsot.children.map { dataSnaphsot.getCommonModel() }
            mListItem.forEach { model ->

                mRefUsers.child(model.id).addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->
                    val newModel = dataSnapshot2.getCommonModel()

                    mRefMessages.child(model.id).limitToLast(1)
                        .addListenerForSingleValueEvent(AppValueEventListener {
                            val tempList = it.children.map { it.getCommonModel() }

                            if(tempList.isEmpty()) newModel.lastMessage = "Сообщений нет"
                            else newModel.lastMessage = tempList[0].text

                            if(newModel.fullname.isEmpty()) newModel.fullname = newModel.phone
                            mAdapter.updateListItem(newModel)
                        })
                })
            }
        })
        binding.recyclerView.adapter = mAdapter
    }

    companion object{
        val listContacts = mutableListOf<CommonModel>()
    }


}