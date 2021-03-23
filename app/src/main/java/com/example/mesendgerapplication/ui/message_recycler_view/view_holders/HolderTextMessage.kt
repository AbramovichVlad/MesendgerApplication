package com.example.mesendgerapplication.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mesendgerapplication.database.CURRENT_UID
import com.example.mesendgerapplication.ui.message_recycler_view.views.MessageView
import com.example.mesendgerapplication.utilities.asTime
import kotlinx.android.synthetic.main.message_item.view.*

class HolderTextMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {

    private val blockUserMessage: ConstraintLayout = view.block_user_message
    private val chatUserMessage: TextView = view.chat_user_message
    private val chatUserMessageTime: TextView = view.chat_user_message_time

    private val blockRecivingUserMessage: ConstraintLayout = view.block_received_message
    private val chatRecivingUserMessage: TextView = view.chat_received_message
    private val chatRecivingUserMeassgeTime: TextView = view.chat_received_message_time

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