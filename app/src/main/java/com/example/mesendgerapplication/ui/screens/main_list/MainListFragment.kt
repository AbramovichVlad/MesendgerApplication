package com.example.mesendgerapplication.ui.screens.main_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.database.*
import com.example.mesendgerapplication.databinding.FragmentMainListBinding
import com.example.mesendgerapplication.models.CommonModel
import com.example.mesendgerapplication.utilities.APP_ACTIVITY
import com.example.mesendgerapplication.utilities.AppValueEventListener
import com.example.mesendgerapplication.utilities.hideKeyboard


class MainListFragment : Fragment(R.layout.fragment_main_list) {

    private lateinit var binding: FragmentMainListBinding
    private lateinit var mAdapter: MainListAdapter
    private val mRefMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGE).child(CURRENT_UID)
    private var mListItem = listOf<CommonModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainListBinding.bind(view)
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Messanger"
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = MainListAdapter()

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


}