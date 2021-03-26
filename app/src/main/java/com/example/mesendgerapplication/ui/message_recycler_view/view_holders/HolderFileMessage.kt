package com.example.mesendgerapplication.ui.message_recycler_view.view_holders

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mesendgerapplication.database.CURRENT_UID
import com.example.mesendgerapplication.database.getFileFromStorage
import com.example.mesendgerapplication.ui.message_recycler_view.views.MessageView
import com.example.mesendgerapplication.utilities.WRITE_FILE
import com.example.mesendgerapplication.utilities.asTime
import com.example.mesendgerapplication.utilities.checkPremision
import com.example.mesendgerapplication.utilities.showToast
import kotlinx.android.synthetic.main.message_item_file.view.*
import java.io.File

class HolderFileMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {

    private val blockUserMessagefile: ConstraintLayout = view.block_user_message_file
    private val chatUserMessageTimefile: TextView = view.chat_user_message_time_file

    private val blockRecivingUserMessagefile: ConstraintLayout =
        view.block_reciving_user_message_file
    private val chatRecivingUserMeassgeTimefile: TextView =
        view.chat_reciving_user_message_time_file

    private val chatUserFileName: TextView = view.chat_user_file_name
    private val chatUserBtnDownload: ImageView = view.chat_user_btn_download
    private val chatUserProgressBar: ProgressBar = view.chat_user_progress_bar

    private val chatRecivingUserFileName: TextView = view.chat_reciving_user_file_name
    private val chatRecivingUserBtnDownload: ImageView = view.chat_reciving_user_btn_download
    private val chatRecivingUserProgressBar: ProgressBar = view.chat_reciving_user_progress_bar


    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockRecivingUserMessagefile.visibility = View.GONE
            blockUserMessagefile.visibility = View.VISIBLE
            chatUserMessageTimefile.text =
                view.timeStamp.asTime()
            chatUserFileName.text = view.text
        } else {
            blockRecivingUserMessagefile.visibility = View.VISIBLE
            blockUserMessagefile.visibility = View.GONE
            chatRecivingUserMeassgeTimefile.text =
                view.timeStamp.asTime()
            chatRecivingUserFileName.text = view.text
        }
    }

    override fun onAttach(view: MessageView) {
        if (view.from == CURRENT_UID) chatUserBtnDownload.setOnClickListener { clickToBtnFile(view) }
        else chatRecivingUserBtnDownload.setOnClickListener { clickToBtnFile(view) }
    }

    private fun clickToBtnFile(view: MessageView) {
        if (view.from == CURRENT_UID) {
            chatUserBtnDownload.visibility = View.INVISIBLE
            chatUserProgressBar.visibility = View.VISIBLE
        } else {
            chatRecivingUserBtnDownload.visibility = View.INVISIBLE
            chatRecivingUserProgressBar.visibility = View.VISIBLE
        }
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            view.text
        )
        try {
            if (checkPremision(WRITE_FILE)) {
                file.createNewFile()
                getFileFromStorage(file, view.fileUrl) {
                    if (view.from == CURRENT_UID) {
                        chatUserBtnDownload.visibility = View.VISIBLE
                        chatUserProgressBar.visibility = View.INVISIBLE
                    } else {
                        chatRecivingUserBtnDownload.visibility = View.VISIBLE
                        chatRecivingUserProgressBar.visibility = View.INVISIBLE
                    }
                }
            }
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }


    override fun onDettach() {
        chatUserBtnDownload.setOnClickListener(null)
        chatRecivingUserBtnDownload.setOnClickListener(null)
    }

}