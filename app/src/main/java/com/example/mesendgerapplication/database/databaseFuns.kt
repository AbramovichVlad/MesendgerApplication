package com.example.mesendgerapplication.database

import android.net.Uri
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.models.CommonModel
import com.example.mesendgerapplication.models.UserModel
import com.example.mesendgerapplication.utilities.APP_ACTIVITY
import com.example.mesendgerapplication.utilities.AppValueEventListener
import com.example.mesendgerapplication.utilities.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

fun sendMessageAsFile(
    recivingUserId: String,
    fileUrl: String,
    mesagesKey: String,
    typeMessage: String
) {
    val refDialogUser = "$NODE_MESSAGE/$CURRENT_UID/$recivingUserId"
    val refDialogRecivingUser = "$NODE_MESSAGE/$recivingUserId/$CURRENT_UID"

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = typeMessage
    mapMessage[CHILD_ID] = mesagesKey
    mapMessage[CHILD_TIME_STAMP] = ServerValue.TIMESTAMP
    mapMessage[CHILD_FILE_URL] = fileUrl

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$mesagesKey"] = mapMessage
    mapDialog["$refDialogRecivingUser/$mesagesKey"] = mapMessage
    REF_DATABASE_ROOT.updateChildren(mapDialog)
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = UserModel()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    REF_STORAGE_ROT = FirebaseStorage.getInstance().reference
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl.addOnSuccessListener {
        function(it.toString())
    }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun putUrlToDataBase(url: String, crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(
        CHILD_PHOTO_URL
    ).setValue(url).addOnSuccessListener {
        function()
    }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun putFileToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri).addOnSuccessListener {
        function()
    }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun initUser(crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .addListenerForSingleValueEvent(AppValueEventListener {
            USER = it.getValue(UserModel::class.java) ?: UserModel()
            if (USER.username.isEmpty()) {
                USER.username = CURRENT_UID
            }
            function()
        })
}

fun updatePhonesToDatabase(arryContacts: ArrayList<CommonModel>) {
    if (AUTH.currentUser != null) {
        REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(AppValueEventListener {
            it.children.forEach { dataSnaphot ->
                arryContacts.forEach { contact ->
                    if (dataSnaphot.key == contact.phone) {
                        REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS)
                            .child(CURRENT_UID)
                            .child(dataSnaphot.value.toString())
                            .child(CHILD_ID)
                            .setValue(dataSnaphot.value.toString())
                            .addOnFailureListener { exeption ->
                                showToast(exeption.message.toString())
                            }
                        REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS)
                            .child(CURRENT_UID)
                            .child(dataSnaphot.value.toString())
                            .child(CHILD_FULLNAME).setValue(contact.fullname)
                            .addOnFailureListener { exeption ->
                                showToast(exeption.message.toString())
                            }
                    }
                }
            }
        })
    }
}

fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): UserModel =
    this.getValue(UserModel::class.java) ?: UserModel()

fun sendMessage(message: String, recivingUserId: String, typeText: String, function: () -> Unit) {
    val refDialogUser = "$NODE_MESSAGE/$CURRENT_UID/$recivingUserId"
    val refDialogRecivingUser = "$NODE_MESSAGE/$recivingUserId/$CURRENT_UID"
    val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = typeText
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_ID] = messageKey.toString()
    mapMessage[CHILD_TIME_STAMP] = ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogRecivingUser/$messageKey"] = mapMessage
    REF_DATABASE_ROOT.updateChildren(mapDialog)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun updateCurrentUsername(newUserName: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_USER_NAME)
        .setValue(newUserName)
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
            deleteOldUsername(newUserName)
        }.addOnFailureListener { showToast(it.message.toString()) }
}

private fun deleteOldUsername(newUserName: String) {
    REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.username).removeValue()
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
            APP_ACTIVITY.supportFragmentManager.popBackStack()
            USER.username = newUserName
        }.addOnFailureListener { showToast(it.message.toString()) }
}

fun setBioToDataBase(newBio: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHOLD_BIO).setValue(newBio)
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
            USER.bio = newBio
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }.addOnFailureListener { showToast(it.message.toString()) }
}

fun setFullName(fullName: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FULLNAME)
        .setValue(fullName).addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
            USER.fullname = fullName
            APP_ACTIVITY.mAppDrawer.updateHeader()
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }.addOnFailureListener { showToast(it.message.toString()) }
}

fun getMessageKey(id: String) = REF_DATABASE_ROOT.child(NODE_MESSAGE)
    .child(CURRENT_UID)
    .child(id).push().key.toString()

fun uploadFileToStorage(uri: Uri, messageKey: String, recivingId: String, typeMessage: String) {
    val path = REF_STORAGE_ROT
        .child(FOLDER_FILES)
        .child(messageKey)

    putFileToStorage(uri, path) {
        getUrlFromStorage(path) {
            sendMessageAsFile(recivingId, it, messageKey, typeMessage)
        }
    }
}

fun getFileFromStorage(mFile: File, fileUrl: String, function: () -> Unit) {
    val path = REF_STORAGE_ROT.storage.getReferenceFromUrl(fileUrl)
    path.getFile(mFile)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}
