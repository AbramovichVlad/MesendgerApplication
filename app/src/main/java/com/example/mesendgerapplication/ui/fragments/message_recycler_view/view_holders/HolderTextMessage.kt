package com.example.mesendgerapplication.ui.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_item.view.*

class HolderTextMessage (view : View) : RecyclerView.ViewHolder(view) {

    val blockUserMessage: ConstraintLayout = view.block_user_message
    val chatUserMessage: TextView = view.chat_user_message
    val chatUserMessageTime: TextView = view.chat_user_message_time

    val blockRecivingUserMessage: ConstraintLayout = view.block_received_message
    val chatRecivingUserMessage: TextView = view.chat_received_message
    val chatRecivingUserMeassgeTime: TextView = view.chat_received_message_time
}