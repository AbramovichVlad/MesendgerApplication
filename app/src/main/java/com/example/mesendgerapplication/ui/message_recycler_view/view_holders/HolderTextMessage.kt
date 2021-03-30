package com.example.mesendgerapplication.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mesendgerapplication.database.CURRENT_UID
import com.example.mesendgerapplication.databinding.MessageItemTextBinding
import com.example.mesendgerapplication.ui.message_recycler_view.views.MessageView
import com.example.mesendgerapplication.utilities.asTime

class HolderTextMessage(binding: MessageItemTextBinding) : RecyclerView.ViewHolder(binding.root),
    MessageHolder {

    private val blockUserMessage: ConstraintLayout = binding.blockUserMessage
    private val chatUserMessage: TextView = binding.chatUserMessage
    private val chatUserMessageTime: TextView = binding.chatUserMessageTime

    private val blockRecivingUserMessage: ConstraintLayout = binding.blockReceivedMessage
    private val chatRecivingUserMessage: TextView = binding.chatReceivedMessage
    private val chatRecivingUserMeassgeTime: TextView = binding.chatReceivedMessageTime
    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockUserMessage.visibility = View.VISIBLE
            blockRecivingUserMessage.visibility = View.GONE
            chatUserMessage.text = view.text
            chatUserMessageTime.text =
                view.timeStamp.asTime()
        } else {
            blockUserMessage.visibility = View.GONE
            blockRecivingUserMessage.visibility = View.VISIBLE
            chatRecivingUserMessage.text = view.text
            chatRecivingUserMeassgeTime.text =
                view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {
    }

    override fun onDettach() {
    }
}