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
import com.example.mesendgerapplication.databinding.MessageItemFileBinding
import com.example.mesendgerapplication.ui.message_recycler_view.views.MessageView
import com.example.mesendgerapplication.utilities.*
import java.io.File

class HolderFileMessage(binding: MessageItemFileBinding) : RecyclerView.ViewHolder(binding.root),
    MessageHolder {

    private val blockUserMessagefile: ConstraintLayout = binding.blockUserMessageFile
    private val chatUserMessageTimefile: TextView = binding.chatUserMessageTimeFile

    private val blockRecivingUserMessagefile: ConstraintLayout =
        binding.blockRecivingUserMessageFile
    private val chatRecivingUserMeassgeTimefile: TextView =
        binding.chatRecivingUserMessageTimeFile

    private val chatUserFileName: TextView = binding.chatUserFileName
    private val chatUserBtnDownload: ImageView = binding.chatUserBtnDownload
    private val chatUserProgressBar: ProgressBar = binding.chatUserProgressBar

    private val chatRecivingUserFileName: TextView = binding.chatRecivingUserFileName
    private val chatRecivingUserBtnDownload: ImageView = binding.chatRecivingUserBtnDownload
    private val chatRecivingUserProgressBar: ProgressBar = binding.chatRecivingUserProgressBar


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
            APP_ACTIVITY.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
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