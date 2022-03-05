package com.example.appnasapi.ui.utils

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appnasapi.databinding.FragmentUtilsBinding

class UtilFragment : Fragment() {

    private var _binding: FragmentUtilsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUtilsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchWiki()

    }

    private fun searchWiki() {
        //Поиск в Wiki по нажатию на custom_icon
        binding.inputWikiLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditWikiText.text.toString()}")
            })
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}