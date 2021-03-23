package com.example.mesendgerapplication.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mesendgerapplication.database.CURRENT_UID
import com.example.mesendgerapplication.ui.message_recycler_view.views.MessageView
import com.example.mesendgerapplication.utilities.asTime
import com.example.mesendgerapplication.utilities.downloadAndSetImage
import kotlinx.android.synthetic.main.message_item.view.*

class HolderImageMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {

    private val blockUserMessageImage: ConstraintLayout = view.block_user_message_image
    private  val chatUserMessageImage: ImageView = view.chat_user_image
    private val chatUserMessageTimeImage: TextView = view.chat_user_message_time_image

    private  val blockRecivingUserMessageImage: ConstraintLayout = view.block_recivde_user_message_image
    private val chatRecivingUserMessageImage: ImageView = view.chat_reciving_user_image
    private val chatRecivingUserMeassgeTimeImage: TextView = view.chat_received_message_time_image

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