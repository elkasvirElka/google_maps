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
        const val password = "PASSWORD"
        const val number = "NUMBER"

        private var mInstance: PreferenceHelper? = null

        fun getInstance(): PreferenceHelper {
            if (mInstance == null) {
                mInstance = PreferenceHelper()
            }
            return mInstance as PreferenceHelper
        }
    }

    fun putString(key: String, value: String) {
        mPreferences.edit().apply {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String) = mPreferences.getString(key, "")

    fun clearAllString() {
        mPreferences.edit().apply {
            putString(number, "")
            putString(password, "")
            apply()
        }
    }

}