package com.example.mesendgerapplication.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.models.CommonModel
import com.example.mesendgerapplication.utilities.CURRENT_UID
import com.example.mesendgerapplication.utilities.asTime
import kotlinx.android.synthetic.main.message_item.view.*

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var mListMessagesCache = mutableListOf<CommonModel>()
    private lateinit var mDifResault: DiffUtil.DiffResult

    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blockUserMessage: ConstraintLayout = view.block_user_message
        val chatUserMessage: TextView = view.chat_user_message
        val chatUserMessageTime: TextView = view.chat_user_message_time

        val blockRecivingUserMessage: ConstraintLayout = view.block_received_message
        val chatRecivingUserMessage: TextView = view.chat_received_message
        val chatRecivingUserMeassgeTime: TextView = view.chat_received_message_time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return SingleChatHolder(view)
    }

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        if (mListMessagesCache[position].from == CURRENT_UID) {
            holder.blockUserMessage.visibility = View.VISIBLE
            holder.blockRecivingUserMessage.visibility = View.GONE
            holder.chatUserMessage.text = mListMessagesCache[position].text
            holder.chatUserMessageTime.text =
                mListMessagesCache[position].timeStamp.toString().asTime()
        } else {
            holder.blockUserMessage.visibility = View.GONE
            holder.blockRecivingUserMessage.visibility = View.VISIBLE
            holder.chatRecivingUserMessage.text = mListMessagesCache[position].text
            holder.chatRecivingUserMeassgeTime.text =
                mListMessagesCache[position].timeStamp.toString().asTime()
        }
    }

    override fun getItemCount(): Int = mListMessagesCache.size


    fun addItemToBootm(item: CommonModel, onSucces: () -> Unit) {
            if (!mListMessagesCache.contains(item)) {
                mListMessagesCache.add(item)
                notifyItemInserted(mListMessagesCache.size)
            }
        onSucces()
    }

    fun addItemToTop(item: CommonModel, onSucces: () -> Unit) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            mListMessagesCache.sortBy { it.timeStamp.toString() }
            notifyItemInserted(0)
        }
        onSucces()
    }
}

//fun addItem(item: CommonModel) {
//    val newList = mutableListOf<CommonModel>()
//    newList.addAll(mListMessagesCache)
//    if (newList.contains(item)) newList.add(item)
//    newList.sortBy { it.timeStamp.toString() }
//    mDifResault = DiffUtil.calculateDiff(DiffUtilCallbacks(mListMessagesCache, newList))
//    mDifResault.dispatchUpdatesTo(this)
//    mListMessagesCache = newList
//}