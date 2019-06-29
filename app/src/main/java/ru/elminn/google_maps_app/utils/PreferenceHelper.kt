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
    fun putToken(token: String) {
        mPreferences.edit().apply {
            putString("TOKEN", token)
            apply()
        }
    }

    fun getToken() = mPreferences.getString("TOKEN", "")
}