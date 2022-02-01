package com.example.appnasapi.ui.pod

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import coil.load
import com.example.appnasapi.R
import kotlinx.android.synthetic.main.main_fragment.*

class PODFragment : Fragment() {

    private val viewModel: PODViewModel by lazy {
        ViewModelProvider(this)[PODViewModel::class.java]
    }

    companion object {
        fun newInstance() = PODFragment()
        private var isMain = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getData()
            .observe(viewLifecycleOwner, Observer<PODData> { renderData(it) })
    }

    private fun renderData(data: PODData) {
        when (data) {
            is PODData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url

                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    image_pod_view.load(url) {
                        lifecycle(this@PODFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                }
            }
            is PODData.Loading -> {

            }
            is PODData.Error -> {
                toast(data.error.message)
            }
        }
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

}