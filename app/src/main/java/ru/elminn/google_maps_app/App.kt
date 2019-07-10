package ru.elminn.google_maps_app

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import java.util.*
import android.R
import android.preference.PreferenceManager
import android.content.SharedPreferences



class App : Application() {

    private var locale: Locale? = null

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (locale != null)
        {
            newConfig.locale = locale
            Locale.setDefault(locale)
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }
    override fun onCreate() {
        super.onCreate()

        val settings = PreferenceManager.getDefaultSharedPreferences(this)

        val config = baseContext.resources.configuration

        val lang = "en"//settings.getString(getString(R.string.pref_locale), "")
        if ("" != lang && config.locale.language != lang) {
            locale = Locale(lang)
            Locale.setDefault(locale)
            config.locale = locale
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        }
    }
}