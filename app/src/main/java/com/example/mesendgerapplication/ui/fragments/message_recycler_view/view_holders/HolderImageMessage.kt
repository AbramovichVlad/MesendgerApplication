package com.example.mesendgerapplication.ui.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_item.view.*

class HolderImageMessage ( view : View) : RecyclerView.ViewHolder(view) {

    val blockUserMessageImage : ConstraintLayout = view.block_user_message_image
    val chatUserMessageImage : ImageView = view.chat_user_image
    val chatUserMessageTimeImage : TextView = view.chat_user_message_time_image

    val blockRecivingUserMessageImage : ConstraintLayout = view.block_recivde_user_message_image
    val chatRecivingUserMessageImage : ImageView = view.chat_reciving_user_image
    val chatRecivingUserMeassgeTimeImage : TextView = view.chat_received_message_time_image

}