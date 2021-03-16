package com.example.mesendgerapplication.utilities

import android.media.MediaRecorder
import java.io.File

class AppVoiceRecorder {
    private val mMediadRecorder = MediaRecorder()
    private lateinit var mFile: File
    private lateinit var mMessageKey: String

    fun startRecord(messageKey: String) {
        try {
            mMessageKey = messageKey
            crateFileForRecord()
            prepeaMediaRecorder()
            mMediadRecorder.start()
        } catch (e: Exception) {
            showToast(e.message.toString())
        }

    }

    private fun prepeaMediaRecorder() {
        mMediadRecorder.apply {
            reset()
            setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            setOutputFile(mFile.absolutePath)
            prepare()
        }
    }

    private fun crateFileForRecord() {
        mFile = File(APP_ACTIVITY.filesDir, mMessageKey)
        mFile.createNewFile()
    }

    fun stopRecord(onSucces: (file: File, messageKey: String) -> Unit) {
        try {
            mMediadRecorder.stop()
            onSucces(mFile, mMessageKey)
        } catch (e: java.lang.Exception) {
            showToast(e.message.toString())
            mFile.delete()
        }
    }

    fun releseReorder() {
        try {
            mMediadRecorder.release()
        } catch (e: java.lang.Exception) {
            showToast(e.message.toString())
        }
    }
}