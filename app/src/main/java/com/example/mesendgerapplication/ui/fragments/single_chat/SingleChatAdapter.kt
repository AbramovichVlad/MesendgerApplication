package com.example.mesendgerapplication.ui.fragments.single_chat

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mesendgerapplication.database.CURRENT_UID
import com.example.mesendgerapplication.ui.fragments.message_recycler_view.view_holders.AppHolderFactory
import com.example.mesendgerapplication.ui.fragments.message_recycler_view.view_holders.HolderImageMessage
import com.example.mesendgerapplication.ui.fragments.message_recycler_view.view_holders.HolderTextMessage
import com.example.mesendgerapplication.ui.fragments.message_recycler_view.views.MessageView
import com.example.mesendgerapplication.utilities.asTime
import com.example.mesendgerapplication.utilities.downloadAndSetImage

class SingleChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListMessagesCache = mutableListOf<MessageView>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HolderImageMessage -> drawMessageImage(holder, position)
            is HolderTextMessage -> drawMessageText(holder, position)
            else -> {
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return mListMessagesCache[position].getTypeView()
    }

    private fun drawMessageImage(holder: HolderImageMessage, position: Int) {
        if (mListMessagesCache[position].from == CURRENT_UID) {
            holder.blockRecivingUserMessageImage.visibility = View.GONE
            holder.blockUserMessageImage.visibility = View.VISIBLE
            holder.chatUserMessageImage.downloadAndSetImage(mListMessagesCache[position].fileUrl)
            holder.chatUserMessageTimeImage.text =
                mListMessagesCache[position].timeStamp.asTime()
        } else {
            holder.blockRecivingUserMessageImage.visibility = View.VISIBLE
            holder.blockUserMessageImage.visibility = View.GONE
            holder.chatRecivingUserMessageImage.downloadAndSetImage(mListMessagesCache[position].fileUrl)
            holder.chatRecivingUserMeassgeTimeImage.text =
                mListMessagesCache[position].timeStamp.asTime()
        }
    }

    private fun drawMessageText(holder: HolderTextMessage, position: Int) {
        if (mListMessagesCache[position].from == CURRENT_UID) {
            holder.blockUserMessage.visibility = View.VISIBLE
            holder.blockRecivingUserMessage.visibility = View.GONE
            holder.chatUserMessage.text = mListMessagesCache[position].text
            holder.chatUserMessageTime.text =
                mListMessagesCache[position].timeStamp.asTime()
        } else {
            holder.blockUserMessage.visibility = View.GONE
            holder.blockRecivingUserMessage.visibility = View.VISIBLE
            holder.chatRecivingUserMessage.text = mListMessagesCache[position].text
            holder.chatRecivingUserMeassgeTime.text =
                mListMessagesCache[position].timeStamp.asTime()
        }
    }

    override fun getItemCount(): Int = mListMessagesCache.size


    fun addItemToBootm(item: MessageView, onSucces: () -> Unit) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            notifyItemInserted(mListMessagesCache.size)
        }
        onSucces()
    }

    fun addItemToTop(item: MessageView, onSucces: () -> Unit) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            mListMessagesCache.sortBy { it.timeStamp }
            notifyItemInserted(0)
        }
        onSucces()
    }
}
// private lateinit var mDifResault: DiffUtil.DiffResult
//fun addItem(item: CommonModel) {
//    val newList = mutableListOf<CommonModel>()
//    newList.addAll(mListMessagesCache)
//    if (newList.contains(item)) newList.add(item)
//    newList.sortBy { it.timeStamp.toString() }
//    mDifResault = DiffUtil.calculateDiff(DiffUtilCallbacks(mListMessagesCache, newList))
//    mDifResault.dispatchUpdatesTo(this)
//    mListMessagesCache = newList
//}