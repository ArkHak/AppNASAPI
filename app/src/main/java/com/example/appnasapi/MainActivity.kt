package com.example.appnasapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.appnasapi.databinding.MainActivityBinding
import com.example.appnasapi.ui.pod.PODFragment
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    private val binding: MainActivityBinding by lazy { MainActivityBinding.inflate(layoutInflater) }

    private val buttonNavigationPanel: BottomAppBar by lazy { binding.bottomAppBarNavigation }

    private val navigationController by lazy { findNavController(R.id.navigation_fragment_container) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        buttonNavigationPanel.setupWithNavController(navigationController)
        setBottomAppBar()

    }

    private fun setBottomAppBar() {
        setSupportActionBar(binding.bottomAppBarNavigation)

        fab.setOnClickListener {
            if (PODFragment.isMain) {
                PODFragment.isMain = false
                bottom_app_bar_navigation.navigationIcon = null
                bottom_app_bar_navigation.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                bottom_app_bar_navigation.replaceMenu(R.menu.menu_bottom_bar_other_screen)
                fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_back_fab
                    )
                )
            } else {
                PODFragment.isMain = true
                bottom_app_bar_navigation.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                bottom_app_bar_navigation.replaceMenu(R.menu.menu_button_bar_navigation)
                fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_plus_fab
                    )
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_button_bar_navigation, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_settings -> {
                navigationController.navigate(R.id.settingsFragment)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
