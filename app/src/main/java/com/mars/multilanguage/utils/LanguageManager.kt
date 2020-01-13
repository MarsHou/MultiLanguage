package com.mars.multilanguage.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.*

/**
 * Created by Mars on 2020-01-09 16:36.
 */
object LanguageManager {
    private const val LANGUAGE_FILE = "language"
    private const val LANGUAGE_KEY = "language_key"

    const val LANGUAGE_SYSTEM = "language_system"
    const val LANGUAGE_ENGLISH = "language_english"
    const val LANGUAGE_CHINESE = "language_chinese"

    private lateinit var systemLocale: Locale
    private var currentLocale: Locale? = null


    fun initSystemLocale(locale: Locale? = null) {
        systemLocale = locale
            ?: if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LocaleList.getDefault().get(0)
            } else {
                Locale.getDefault()
            }
    }

    fun onConfigurationChangedOfApplication(config: Configuration) {
        initSystemLocale(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.locales.get(0)
            } else {
                config.locale
            }
        )
    }

    fun currentLocale(): Locale {
        return currentLocale ?: if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault().get(0)
        } else {
            Locale.getDefault()
        }
    }

    fun setLanguageForCreateActivity(context: Context) {
        if (LANGUAGE_SYSTEM == getLanguageForSp(context)) {
            currentLocale = systemLocale
            return
        }
        setLanguage(context, getLanguageForSp(context))
    }


    fun selectLanguage(context: Context, language: String): Boolean {
        if (language == getLanguageForSp(context)) {
            return false
        }
        setLanguage(context, language)
        setLanguageForSP(context, language)
        return true
    }

    private fun setLanguage(context: Context, language: String) {

        val configuration = context.resources.configuration
        val locale = when (language) {
            LANGUAGE_CHINESE -> {
                Locale.CHINESE
            }
            LANGUAGE_ENGLISH -> {
                Locale.ENGLISH
            }
            else -> {
                systemLocale
            }
        }
        currentLocale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
            context.applicationContext.createConfigurationContext(configuration)
            Locale.setDefault(locale)
        } else {
            configuration.locale = locale
        }
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }

    private fun setLanguageForSP(context: Context, language: String) {
        val sharedPreferences = getCurrentSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString(LANGUAGE_KEY, language)
        editor.apply()
    }

    fun getLanguageForSp(context: Context): String {
        return getCurrentSharedPreferences(context).getString(LANGUAGE_KEY, LANGUAGE_SYSTEM)
            ?: LANGUAGE_SYSTEM
    }

    private fun getCurrentSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(LANGUAGE_FILE, MODE_PRIVATE)
    }

}