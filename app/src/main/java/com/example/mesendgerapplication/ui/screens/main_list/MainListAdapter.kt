package com.example.mesendgerapplication.ui.screens.main_list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mesendgerapplication.databinding.MainListItemBinding
import com.example.mesendgerapplication.databinding.MessageItemFileBinding
import com.example.mesendgerapplication.models.CommonModel
import com.example.mesendgerapplication.ui.screens.single_chat.SingleChatFragment
import com.example.mesendgerapplication.utilities.downloadAndSetImage
import com.example.mesendgerapplication.utilities.replaceFragment

class MainListAdapter : RecyclerView.Adapter<MainListAdapter.MainListHolder>() {

    private var listItems = mutableListOf<CommonModel>()

    class MainListHolder(binding: MainListItemBinding) : RecyclerView.ViewHolder(binding.root){
        val itemName : TextView = binding.mainListFullName
        val itemLastMessage = binding.mainListLastMessage
        val itemPhoto : ImageView = binding.mainListPhoto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListHolder {
        val binding = MainListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = MainListHolder(binding)
        holder.itemView.setOnClickListener{
            replaceFragment(SingleChatFragment(listItems[holder.adapterPosition]))
        }
        return holder
    }

    override fun onBindViewHolder(holder: MainListHolder, position: Int) {
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