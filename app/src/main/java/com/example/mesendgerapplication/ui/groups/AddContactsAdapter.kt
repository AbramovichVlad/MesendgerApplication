package com.example.mesendgerapplication.ui.groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mesendgerapplication.databinding.AddContactItemBinding
import com.example.mesendgerapplication.databinding.FragmentAddContactsBinding
import com.example.mesendgerapplication.databinding.MainListItemBinding
import com.example.mesendgerapplication.databinding.MessageItemFileBinding
import com.example.mesendgerapplication.models.CommonModel
import com.example.mesendgerapplication.ui.screens.single_chat.SingleChatFragment
import com.example.mesendgerapplication.utilities.downloadAndSetImage
import com.example.mesendgerapplication.utilities.replaceFragment
import com.example.mesendgerapplication.utilities.showToast
import de.hdodenhof.circleimageview.CircleImageView

class AddContactsAdapter : RecyclerView.Adapter<AddContactsAdapter.AddContactsHolder>() {

    private var listItems = mutableListOf<CommonModel>()

    class AddContactsHolder(binding: AddContactItemBinding) : RecyclerView.ViewHolder(binding.root){
        val itemName : TextView = binding.textFullName
        val itemLastMessage = binding.textLastMessage
        val itemPhoto : CircleImageView = binding.photoContact
        val itemChoise  : CircleImageView = binding.itemChoiseContact
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddContactsHolder {
        val binding = AddContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = AddContactsHolder(binding)
        holder.itemView.setOnClickListener{
           if(listItems[holder.adapterPosition].choise){
                binding.itemChoiseContact.visibility = View.INVISIBLE
               listItems[holder.adapterPosition].choise = false
               AddContactsFragment.listContacts.remove(listItems[holder.adapterPosition])
           }else{
               binding.itemChoiseContact.visibility = View.VISIBLE
               listItems[holder.adapterPosition].choise = true
               AddContactsFragment.listContacts.add(listItems[holder.adapterPosition])
           }
        }
        return holder
    }

    override fun onBindViewHolder(holder: AddContactsHolder, position: Int) {
        holder.itemName.text = listItems[position].fullname
        holder.itemLastMessage.text = listItems[position].lastMessage
        holder.itemPhoto.downloadAndSetImage(listItems[position].photoUrl)
    }

    fun updateListItem(item : CommonModel){
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }

    override fun getItemCount() = listItems.size
}