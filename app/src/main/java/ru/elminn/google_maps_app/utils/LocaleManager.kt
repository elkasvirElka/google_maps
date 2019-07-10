package ru.elminn.google_maps_app.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

class LocaleManager {
    companion object{
        fun updateResources(context: Context, language: String): Context {
            var context = context
            val locale = Locale(language)
            Locale.setDefault(locale)

            val res = context.getResources()
            val config = Configuration(res.getConfiguration())
            if (Build.VERSION.SDK_INT >= 17) {
                config.setLocale(locale)
                context = context.createConfigurationContext(config)
            } else {
                config.locale = locale
                res.updateConfiguration(config, res.getDisplayMetrics())
            }
            return context
        }
    }
}