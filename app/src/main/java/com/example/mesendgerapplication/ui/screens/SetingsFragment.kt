package com.example.mesendgerapplication.ui.screens

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.database.*
import com.example.mesendgerapplication.utilities.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_setings.*


class SetingsFragment : BaseFragment(R.layout.fragment_setings) {

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        tv_seting_username.text = USER.fullname
        tv_settings_bio.text = USER.bio
        tv_settings_phone_number.text = USER.phone
        tv_settings_full_name.text = USER.username
        tv_seting_user_status.text = USER.state
        settings_btn_change_login.setOnClickListener {
            replaceFragment(ChangeUserNameFragment())
        }
        settings_btn_change_bio.setOnClickListener {
            replaceFragment(ChangeBIoFragment())
        }
        settings_change_photo.setOnClickListener {
            changePhotoUser()
        }
        seting_user_photo.downloadAndSetImage(USER.photoUrl)
    }

    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(250, 250)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_action_menu_exit -> {
                AppStates.updateState(AppStates.OFFLINE)
                AUTH.signOut()
                restartActivity()
            }
            R.id._settings_action_menu_change_name -> {
                replaceFragment(ChangeNameFragment())
            }

        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&
            resultCode == RESULT_OK &&
            data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROT.child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)
            putFileToStorage(uri, path) {
                getUrlFromStorage(path) {
                    putUrlToDataBase(it) {
                        seting_user_photo.downloadAndSetImage(it)
                        showToast(getString(R.string.toast_data_update))
                        USER.photoUrl = it
                        APP_ACTIVITY.mAppDrawer.enableDrawer()
                            APP_ACTIVITY.mAppDrawer.updateHeader()

                    }
                }
            }
        }
    }
}