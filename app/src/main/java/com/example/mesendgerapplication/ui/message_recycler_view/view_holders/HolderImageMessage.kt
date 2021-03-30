package com.example.mesendgerapplication.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mesendgerapplication.database.CURRENT_UID
import com.example.mesendgerapplication.databinding.MessageItemImageBinding
import com.example.mesendgerapplication.ui.message_recycler_view.views.MessageView
import com.example.mesendgerapplication.utilities.asTime
import com.example.mesendgerapplication.utilities.downloadAndSetImage

class HolderImageMessage(binding: MessageItemImageBinding) : RecyclerView.ViewHolder(binding.root),
    MessageHolder {

    private val blockUserMessageImage: ConstraintLayout = binding.blockUserMessageImage
    private val chatUserMessageImage: ImageView = binding.chatUserImage
    private val chatUserMessageTimeImage: TextView = binding.chatUserMessageTimeImage

    private val blockRecivingUserMessageImage: ConstraintLayout =
        binding.blockRecivdeUserMessageImage
    private val chatRecivingUserMessageImage: ImageView = binding.chatRecivingUserImage
    private val chatRecivingUserMeassgeTimeImage: TextView = binding.chatReceivedMessageTimeImage

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockRecivingUserMessageImage.visibility = View.GONE
            blockUserMessageImage.visibility = View.VISIBLE
            chatUserMessageImage.downloadAndSetImage(view.fileUrl)
            chatUserMessageTimeImage.text =
                view.timeStamp.asTime()
        } else {
            blockRecivingUserMessageImage.visibility = View.VISIBLE
            blockUserMessageImage.visibility = View.GONE
            chatRecivingUserMessageImage.downloadAndSetImage(view.fileUrl)
            chatRecivingUserMeassgeTimeImage.text =
                view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {
    }

    override fun onDettach() {
    }

}