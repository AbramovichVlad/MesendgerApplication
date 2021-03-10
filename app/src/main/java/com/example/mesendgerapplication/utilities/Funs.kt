package com.example.mesendgerapplication.utilities

import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.models.CommonModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

fun showToast(mesage: String) {
    if (!mesage.isEmpty()) {
        Toast.makeText(APP_ACTIVITY, mesage, Toast.LENGTH_SHORT).show()
    }
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, idView: Int, stackVal: Boolean = true) {
    if (stackVal) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(idView, fragment)
            .commit()
    } else {
        supportFragmentManager
            .beginTransaction()
            .replace(idView, fragment)
            .commit()
    }
}

fun Fragment.replaceFragment(fragment: Fragment, idView: Int) {
    this.fragmentManager
        ?.beginTransaction()
        ?.addToBackStack(null)
        ?.replace(idView, fragment)
        ?.commit()
}

fun hideKeyboard() {
    val imm: InputMethodManager =
        APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken, 0)
}

fun ImageView.downloadAndSetImage(photoUrl: String) {
    Picasso.get()
        .load(photoUrl)
        .fit()
        .placeholder(R.drawable.default_photo)
        .into(this)
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

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}