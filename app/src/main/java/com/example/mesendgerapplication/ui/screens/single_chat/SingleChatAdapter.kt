package com.example.mesendgerapplication.ui.screens.single_chat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mesendgerapplication.ui.message_recycler_view.view_holders.*
import com.example.mesendgerapplication.ui.message_recycler_view.views.MessageView

class SingleChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListMessagesCache = mutableListOf<MessageView>()

    private val mListHolder = mutableListOf<MessageHolder>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MessageHolder).drawMessage(mListMessagesCache[position])
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolder).onAttach(mListMessagesCache[holder.adapterPosition])
        mListHolder.add((holder as MessageHolder))
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolder).onDettach()
        mListHolder.remove((holder as MessageHolder))
        super.onViewDetachedFromWindow(holder)
    }


    override fun getItemViewType(position: Int): Int {
        return mListMessagesCache[position].getTypeView()
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

    fun destroy() {
       mListHolder.forEach {
           it.onDettach()
       }
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