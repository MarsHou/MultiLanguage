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


    /**
     * Initial system locale.
     */
    fun initSystemLocale(locale: Locale? = null) {
        systemLocale = locale
            ?: if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LocaleList.getDefault().get(0)
            } else {
                Locale.getDefault()
            }
    }

    /**
     * Configuration changed of application listener.
     */
    fun onConfigurationChangedOfApplication(config: Configuration) {
        initSystemLocale(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.locales.get(0)
            } else {
                config.locale
            }
        )
    }

    /**
     * Obtain current locale.
     */
    fun currentLocale(): Locale {
        return currentLocale ?: if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault().get(0)
        } else {
            Locale.getDefault()
        }
    }

    /**
     * When open activity be setting language.
     */
    fun setLanguageForCreateActivity(context: Context) {
        if (LANGUAGE_SYSTEM == getLanguageForSp(context)) {
            currentLocale = systemLocale
            return
        }
        setLanguage(context, getLanguageForSp(context))
    }


    /**
     * Manual select language.
     * @param language (#LanguageManager.LANGUAGE_SYSTEM or LanguageManager.LANGUAGE_XXX)
     */
    fun selectLanguage(context: Context, language: String): Boolean {
        if (language == getLanguageForSp(context)) {
            return false
        }
        setLanguage(context, language)
        setLanguageForSP(context, language)
        return true
    }

    /*
     * Setting language.
     */
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

    /*
     *  Setting language for SharedPreferences
     */
    private fun setLanguageForSP(context: Context, language: String) {
        val sharedPreferences = getCurrentSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString(LANGUAGE_KEY, language)
        editor.apply()
    }

    /*
     *  Obtain language for SharedPreferences
     */
    fun getLanguageForSp(context: Context): String {
        return getCurrentSharedPreferences(context).getString(LANGUAGE_KEY, LANGUAGE_SYSTEM)
            ?: LANGUAGE_SYSTEM
    }

    private fun getCurrentSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(LANGUAGE_FILE, MODE_PRIVATE)
    }

}