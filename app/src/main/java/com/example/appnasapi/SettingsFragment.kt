package com.example.appnasapi

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appnasapi.databinding.FragmentSettingsBinding

const val NameSharedPreference = "SAVE_SP_THEME"
const val APP_THEME_CODE = "APP_THEME"
const val CODE_DEF_THEME = 0
const val CODE_RED_THEME = 1
const val CODE_GREEN_THEME = 2

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        installCheckRB()
        initRadioGroup()

    }


    private fun installCheckRB() {
        when (getCodeStyleFromSharedPref()) {
            CODE_DEF_THEME -> binding.radiobuttonDefTheme.isChecked = true
            CODE_RED_THEME -> binding.radiobuttonRedTheme.isChecked = true
            CODE_GREEN_THEME -> binding.radiobuttonGreenTheme.isChecked = true
        }
    }

    private fun initRadioGroup() {
        binding.radioGroupTheme.setOnCheckedChangeListener { _, checkedId: Int ->
            when (checkedId) {
                R.id.radiobutton_def_theme -> {
                    setCodeStyleSharedPref(CODE_DEF_THEME)
                    requireActivity().apply {
                        setTheme(R.style.Theme_AppNASAPI)
                        recreate()
                    }
                }
                R.id.radiobutton_red_theme -> {
                    setCodeStyleSharedPref(CODE_RED_THEME)
                    requireActivity().apply {
                        setTheme(R.style.RedTheme)
                        recreate()
                    }
                }
                R.id.radiobutton_green_theme -> {
                    setCodeStyleSharedPref(CODE_GREEN_THEME)
                    requireActivity().apply {
                        setTheme(R.style.GreenTheme)
                        recreate()
                    }
                }
            }
        }
    }

    private fun getCodeStyleFromSharedPref(): Int {
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(
            NameSharedPreference,
            Context.MODE_PRIVATE
        )
        return sharedPref.getInt(APP_THEME_CODE, CODE_DEF_THEME)
    }

    private fun setCodeStyleSharedPref(codeStyle: Int) {
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(
            NameSharedPreference,
            Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putInt(APP_THEME_CODE, codeStyle)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}