package com.example.mesendgerapplication.ui.message_recycler_view.view_holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.databinding.*
import com.example.mesendgerapplication.ui.message_recycler_view.views.MessageView

class AppHolderFactory {

    companion object {
        fun getHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                MessageView.MESSAGE_IMAGE -> {
                    val binding = MessageItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    HolderImageMessage(binding)
                }
                MessageView.MESSAGE_VOICE -> {
                    val binding = MessageItemVoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    HolderVoiceMessage(binding)
                }
                MessageView.MESSAGE_FILE-> {
                    val binding = MessageItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    HolderFileMessage(binding)
                }
                else -> {
                    val binding = MessageItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    HolderTextMessage(binding)
                }
            }
        }
    }
}