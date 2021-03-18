package com.example.mesendgerapplication.ui.fragments.message_recycler_view.views

import com.example.mesendgerapplication.models.CommonModel
import com.example.mesendgerapplication.utilities.TYPE_MESSAGE_IMAGE

class AppViewFactory {

    companion object{
        fun getView(messageItem : CommonModel) : MessageView{
            return when(messageItem.type){
                TYPE_MESSAGE_IMAGE -> ViewImageMessage(
                    messageItem.id,
                    messageItem.from,
                    messageItem.timeStamp.toString(),
                    messageItem.fileUrl
                )
                else -> ViewTextMessage(
                    messageItem.id,
                    messageItem.from,
                    messageItem.timeStamp.toString(),
                    messageItem.fileUrl,
                    messageItem.text
                )
            }
        }
    }
}