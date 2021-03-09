package com.example.mesendgerapplication.utilities

import android.net.Uri
import android.provider.ContactsContract
import com.example.mesendgerapplication.models.CommonModel
import com.example.mesendgerapplication.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var USER: UserModel
lateinit var CURRENT_UID: String

lateinit var REF_STORAGE_ROT: StorageReference

const val NODE_USERS = "users"
const val NODE_USERNAMES = "usernames"
const val NODE_PHONES = "phones"
const val NODE_PHONES_CONTACTS = "phones_contacts"

const val FOLDER_PROFILE_IMAGE = "profile_image"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USER_NAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHOLD_BIO = "bio"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_STATE = "state"

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

inline fun putImageToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
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

fun initContacts() {
    if (checkPremision(READ_CONTACTS)) {
        val arryContacts = arrayListOf<CommonModel>()
        val cursor = APP_ACTIVITY.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            while (it.moveToNext()) {
                val fullName =
                    it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phone =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val newModel = CommonModel()
                newModel.fullname = fullName
                newModel.phone = phone.replace(Regex("[\\s, -,\\p{P}]"), "")
                arryContacts.add(newModel)

            }
        }
        cursor?.close()
        updatePhonesToDatabase(arryContacts)
    }
}

fun updatePhonesToDatabase(arryContacts: ArrayList<CommonModel>) {
    REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(AppValueEventListener {
        it.children.forEach { dataSnaphot ->
            arryContacts.forEach { contact ->
                if (dataSnaphot.key == contact.phone) {
                    REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS)
                        .child(CURRENT_UID)
                        .child(dataSnaphot.value.toString())
                        .child(CHILD_ID)
                        .setValue(dataSnaphot.value.toString())
                        .addOnFailureListener {
                            showToast(it.message.toString())
                        }
                }
            }
        }
    })
}


fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): UserModel =
    this.getValue(UserModel::class.java) ?: UserModel()

