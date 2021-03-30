package com.example.mesendgerapplication.ui.screens.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.database.*
import com.example.mesendgerapplication.databinding.ContactItemBinding
import com.example.mesendgerapplication.databinding.FragmentContactsBinding
import com.example.mesendgerapplication.models.CommonModel
import com.example.mesendgerapplication.ui.screens.base.BaseFragment
import com.example.mesendgerapplication.ui.screens.single_chat.SingleChatFragment
import com.example.mesendgerapplication.utilities.APP_ACTIVITY
import com.example.mesendgerapplication.utilities.AppValueEventListener
import com.example.mesendgerapplication.utilities.downloadAndSetImage
import com.example.mesendgerapplication.utilities.replaceFragment
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var mRefContacts: DatabaseReference
    private lateinit var mRefUsers: DatabaseReference
    private lateinit var mRefUsersListner: AppValueEventListener
    private var mapListner = hashMapOf<DatabaseReference, AppValueEventListener>()
    private lateinit var binding: FragmentContactsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentContactsBinding.bind(view)
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.fragment_title_contacts)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = binding.contactsRecyclerView
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefContacts, CommonModel::class.java)
            .build()

        mAdapter = object : FirebaseRecyclerAdapter<CommonModel, ContactsHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val binding =
                    ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ContactsHolder(binding)
            }

            override fun onBindViewHolder(
                holder: ContactsHolder,
                position: Int,
                model: CommonModel
            ) {
                mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)
                mRefUsersListner = AppValueEventListener {
                    val contact = it.getCommonModel()

                    if (contact.fullname.isEmpty()) holder.name.text = model.fullname
                    else holder.name.text = contact.fullname

                    holder.status.text = contact.state
                    holder.photo.downloadAndSetImage(contact.photoUrl)
                    holder.itemView.setOnClickListener {
                        replaceFragment(SingleChatFragment(model))
                    }
                }
                mRefUsers.addValueEventListener(mRefUsersListner)
                mapListner[mRefUsers] = mRefUsersListner
            }
        }
        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
        mapListner.forEach {
            it.key.removeEventListener(it.value)
        }
    }

    class ContactsHolder(binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.toolbarContactChatFullName
        val status: TextView = binding.toolbarContactChatStatus
        val photo: CircleImageView = binding.contactPhoto
    }
}
