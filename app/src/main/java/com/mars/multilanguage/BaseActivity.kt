package com.mars.multilanguage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mars.multilanguage.utils.LanguageManager

/**
 * Created by Mars on 2020-01-13 14:57.
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.setLanguageForCreateActivity(this)
    }

}