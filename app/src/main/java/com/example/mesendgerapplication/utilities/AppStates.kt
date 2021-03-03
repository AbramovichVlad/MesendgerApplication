package com.example.mesendgerapplication.utilities

enum class AppStates(val state : String) {
    ONLINE("в сети"),
    OFFLINE("был недавно"),
    TYPING("печатает");

    companion object {
        fun updateState(appStates: AppStates){
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_STATE)
                .setValue(appStates.state)
                .addOnFailureListener { showToast(it.message.toString()) }
                .addOnSuccessListener { USER.state = appStates.state}
        }
    }
}