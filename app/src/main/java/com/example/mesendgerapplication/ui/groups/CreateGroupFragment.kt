package com.example.mesendgerapplication.ui.groups

import android.os.Bundle
import android.view.View
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.databinding.FragmentCreateGroupBinding
import com.example.mesendgerapplication.models.CommonModel
import com.example.mesendgerapplication.ui.screens.base.BaseFragment
import com.example.mesendgerapplication.utilities.APP_ACTIVITY
import com.example.mesendgerapplication.utilities.hideKeyboard
import com.example.mesendgerapplication.utilities.replaceFragment

class CreateGroupFragment(private var listContacts: List<CommonModel>) :
    BaseFragment(R.layout.fragment_create_group) {

    private lateinit var binding: FragmentCreateGroupBinding
    private lateinit var mAdapter: AddContactsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateGroupBinding.bind(view)
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.create_group)
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
        initRecyclerView()
        binding.createGroupBtnComplet.setOnClickListener {  }
        binding.inputGroupName.requestFocus()
    }

    private fun initRecyclerView() {
        mAdapter = AddContactsAdapter()
        binding.recyclerViewGroup.adapter = mAdapter
        listContacts.forEach {
            mAdapter.updateListItem(it)
        }
    }
}