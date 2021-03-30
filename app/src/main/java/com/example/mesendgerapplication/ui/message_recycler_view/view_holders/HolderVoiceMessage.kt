package com.example.mesendgerapplication.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mesendgerapplication.database.CURRENT_UID
import com.example.mesendgerapplication.databinding.MessageItemVoiceBinding
import com.example.mesendgerapplication.ui.message_recycler_view.views.MessageView
import com.example.mesendgerapplication.utilities.AppVoicePlayer
import com.example.mesendgerapplication.utilities.asTime

class HolderVoiceMessage(binding: MessageItemVoiceBinding) : RecyclerView.ViewHolder(binding.root),
    MessageHolder {

    private val mAppVoicePlayer = AppVoicePlayer()

    private val blockUserMessageVoice: ConstraintLayout = binding.blockUserMessageVoice
    private val chatUserMessageTimeVoice: TextView = binding.chatUserMessageTimeVoice
    private val blockRecivingUserMessageVoice: ConstraintLayout =
        binding.blockRecivingUserMessageVoice
    private val chatRecivingUserMeassgeTimeVoice: TextView =
        binding.chatRecivingUserMessageTimeVoice

    private val chatUserBtnPlay: ImageView = binding.chatUserBtnPlay
    private val chatUserBtnStop: ImageView = binding.chatUserBtnStop
    private val chatRecivingUserBtnPlay: ImageView = binding.chatRecivingUserBtnPlay
    private val chatRecivingUserBtnStop: ImageView = binding.chatRecivingUserBtnStop

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockRecivingUserMessageVoice.visibility = View.GONE
            blockUserMessageVoice.visibility = View.VISIBLE
            chatUserMessageTimeVoice.text =
                view.timeStamp.asTime()
        } else {
            blockRecivingUserMessageVoice.visibility = View.VISIBLE
            blockUserMessageVoice.visibility = View.GONE
            chatRecivingUserMeassgeTimeVoice.text =
                view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {
        mAppVoicePlayer.init()
        if (view.from == CURRENT_UID) {
            chatUserBtnPlay.setOnClickListener {
                chatUserBtnPlay.visibility = View.GONE
                chatUserBtnStop.visibility = View.VISIBLE
                chatUserBtnStop.setOnClickListener {
                    stop {
                        chatUserBtnStop.setOnClickListener(null)
                        chatUserBtnPlay.visibility = View.VISIBLE
                        chatUserBtnStop.visibility = View.GONE
                    }
                }
                play(view) {
                    chatUserBtnPlay.visibility = View.VISIBLE
                    chatUserBtnStop.visibility = View.GONE

                }
            }
        } else {
            chatRecivingUserBtnPlay.setOnClickListener {
                chatRecivingUserBtnPlay.visibility = View.GONE
                chatRecivingUserBtnStop.visibility = View.VISIBLE
                chatRecivingUserBtnStop.setOnClickListener {
                    stop {
                        chatRecivingUserBtnStop.setOnClickListener(null)
                        chatRecivingUserBtnPlay.visibility = View.VISIBLE
                        chatRecivingUserBtnStop.visibility = View.GONE
                    }
                }
                play(view) {
                    chatRecivingUserBtnPlay.visibility = View.VISIBLE
                    chatRecivingUserBtnStop.visibility = View.GONE
                }
            }
        }
    }

    private fun stop(function: () -> Unit) {
        mAppVoicePlayer.stop {
            function()
        }
    }

    private fun play(view: MessageView, function: () -> Unit) {
        mAppVoicePlayer.play(view.id, view.fileUrl) {
            function()
        }
    }

    override fun onDettach() {
        chatUserBtnPlay.setOnClickListener(null)
        chatRecivingUserBtnPlay.setOnClickListener(null)
        mAppVoicePlayer.release()
    }

}