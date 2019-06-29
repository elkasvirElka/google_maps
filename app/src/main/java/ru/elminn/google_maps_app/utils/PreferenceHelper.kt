package ru.elminn.google_maps_app.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper private constructor() {

    private lateinit var mPreferences: SharedPreferences

    fun init(context: Context) {
        mPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }

    companion object {
        const val KEY_REGISTRATION_FINISHED_SUCCESSFULLY = "registration_finished_successfully"

        private var mInstance: PreferenceHelper? = null

        fun getInstance(): PreferenceHelper {
            if (mInstance == null) {
                mInstance = PreferenceHelper()
            }
            return mInstance as PreferenceHelper
        }
    }
    fun putPassword(password: String) {
        mPreferences.edit().apply {
            putString("password", password)
            apply()
        }
    }

    fun getPassword() = mPreferences.getString("password", "")
}