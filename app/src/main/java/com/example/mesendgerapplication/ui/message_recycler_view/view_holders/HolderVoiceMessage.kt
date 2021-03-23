package com.example.mesendgerapplication.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mesendgerapplication.database.CURRENT_UID
import com.example.mesendgerapplication.ui.message_recycler_view.views.MessageView
import com.example.mesendgerapplication.utilities.AppVoicePlayer
import com.example.mesendgerapplication.utilities.asTime
import kotlinx.android.synthetic.main.message_item_voice.view.*

class HolderVoiceMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {

    private val mAppVoicePlayer = AppVoicePlayer()

    private val blockUserMessageVoice: ConstraintLayout = view.block_user_message_voice
    private val chatUserMessageTimeVoice: TextView = view.chat_user_message_time_voice
    private val blockRecivingUserMessageVoice: ConstraintLayout =
        view.block_reciving_user_message_voice
    private val chatRecivingUserMeassgeTimeVoice: TextView =
        view.chat_reciving_user_message_time_voice

    private val chatUserBtnPlay: ImageView = view.chat_user_btn_play
    private val chatUserBtnStop: ImageView = view.chat_user_btn_stop
    private val chatRecivingUserBtnPlay: ImageView = view.chat_reciving_user_btn_play
    private val chatRecivingUserBtnStop: ImageView = view.chat_reciving_user_btn_stop

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