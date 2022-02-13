package com.example.appnasapi.ui.pod

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import coil.load
import com.cesarferreira.tempo.Tempo
import com.cesarferreira.tempo.day
import com.cesarferreira.tempo.toString
import com.example.appnasapi.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.fragment_pod.*

const val ARG_DAY_SLIDER = "ARG_DAY_SLIDER"

class PODFragment : Fragment() {

    private val viewModel: PODViewModel by lazy {
        ViewModelProvider(this)[PODViewModel::class.java]
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_pod, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getData()
            .observe(viewLifecycleOwner, Observer<PODData> { renderData(it) })

        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))

        //Инициализация слайдера
        initSliderPOD()
    }

    private fun initSliderPOD() {
        arguments?.takeIf { it.containsKey(ARG_DAY_SLIDER) }?.apply {
            when (getString(ARG_DAY_SLIDER)) {
                TWO_DAY_AGO -> {
                    viewModel.sendServerRequest(2.day.ago.toString(DATE_FORMAT))
                }
                YESTERDAY -> {
                    viewModel.sendServerRequest(Tempo.yesterday.toString(DATE_FORMAT))
                }
                NOW -> {
                    viewModel.sendServerRequest(Tempo.now.toString(DATE_FORMAT))
                }
            }
        }
    }


    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun renderData(data: PODData) {
        when (data) {
            is PODData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                val title = serverResponseData.title
                val explanation = serverResponseData.explanation

                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    image_pod_view.load(url) {
                        lifecycle(this@PODFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                }

                if (title.isNullOrEmpty() && explanation.isNullOrEmpty()) {
                    toast("Fact is empty")
                } else {
                    bottom_sheet_description_header.text = title
                    bottom_sheet_description.text = explanation
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