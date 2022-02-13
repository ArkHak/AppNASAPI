package com.example.appnasapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.appnasapi.databinding.MainActivityBinding
import androidx.navigation.ui.setupWithNavController
import com.example.appnasapi.ui.settings.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val binding: MainActivityBinding by lazy { MainActivityBinding.inflate(layoutInflater) }

    private val buttonNavigationPanel: BottomNavigationView by lazy { binding.bottomNavigationPanel }

    private val navigationController by lazy { findNavController(R.id.navigation_fragment_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getAppTheme(R.style.Theme_AppNASAPI))
        setContentView(binding.root)

        buttonNavigationPanel.setupWithNavController(navigationController)

        //setBottomNavPanel()


    }

    private fun setBottomNavPanel() {
        buttonNavigationPanel.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.fragment_settings -> {
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener true
        }
    }


    private fun getAppTheme(codeStyle: Int): Int {
        return codeStyleToStyleId(getCodeStyle(codeStyle))
    }

    private fun getCodeStyle(codeStyle: Int): Int {
        val sharedPref = getSharedPreferences(
            NameSharedPreference,
            MODE_PRIVATE
        )
        return sharedPref.getInt(APP_THEME_CODE, codeStyle)
    }

    private fun codeStyleToStyleId(codeStyle: Int): Int {
        return when (codeStyle) {
            CODE_DEF_THEME -> R.style.Theme_AppNASAPI
            CODE_RED_THEME -> R.style.RedTheme
            CODE_GREEN_THEME -> R.style.GreenTheme
            else -> R.style.Theme_AppNASAPI
        }
    }
}
