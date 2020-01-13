package com.mars.multilanguage.utils

import android.app.Application
import android.content.res.Configuration
import android.os.Build

/**
 * Created by Mars on 2020-01-13 14:23.
 */
class MLApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        LanguageManager.initSystemLocale()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LanguageManager.onConfigurationChangedOfApplication(newConfig)
    }
}