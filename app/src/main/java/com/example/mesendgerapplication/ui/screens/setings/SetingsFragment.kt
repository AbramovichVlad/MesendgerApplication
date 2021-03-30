package com.example.mesendgerapplication.ui.screens.setings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.example.mesendgerapplication.R
import com.example.mesendgerapplication.database.*
import com.example.mesendgerapplication.databinding.FragmentSetingsBinding
import com.example.mesendgerapplication.ui.screens.base.BaseFragment
import com.example.mesendgerapplication.utilities.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class SetingsFragment : BaseFragment(R.layout.fragment_setings) {

    private lateinit var binding:  FragmentSetingsBinding

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetingsBinding.bind(view)
    }

    private fun initFields() {
        binding.tvSetingUsername.text = USER.fullname
        binding.tvSettingsBio.text = USER.bio
        binding.tvSettingsPhoneNumber.text = USER.phone
        binding.tvSettingsFullName.text = USER.username
        binding.tvSetingUserStatus.text = USER.state
        binding.settingsBtnChangeLogin.setOnClickListener {
            replaceFragment(ChangeUserNameFragment())
        }
        binding.settingsBtnChangeBio.setOnClickListener {
            replaceFragment(ChangeBIoFragment())
        }
        binding.settingsBtnChangePhoneNumber.setOnClickListener {
            changePhotoUser()
        }
        binding.setingUserPhoto.downloadAndSetImage(USER.photoUrl)
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
                        binding.setingUserPhoto.downloadAndSetImage(it)
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