package com.example.appnasapi.ui.pod


import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.transition.*
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.load
import com.cesarferreira.tempo.Tempo
import com.cesarferreira.tempo.day
import com.cesarferreira.tempo.toString
import com.example.appnasapi.*
import com.example.appnasapi.bd.POD
import com.example.appnasapi.bd.PODBDViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.fragment_pod.*

const val ARG_DAY_SLIDER = "ARG_DAY_SLIDER"

class PODFragment : Fragment() {
    private var isExpanded = false

    private val viewModel: PODViewModel by lazy {
        ViewModelProvider(this)[PODViewModel::class.java]
    }

    private val podBDViewModel: PODBDViewModel by viewModels {
        PODBDViewModel.PODViewModelFactory((activity?.application as App).repository)
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

        initExpandedPOD()
    }

    private fun initExpandedPOD() {
        image_pod_view.setOnClickListener {
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(
                fragment_main_pod, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )
            if (isExpanded) is_favorite.visibility = View.GONE
            else is_favorite.visibility = View.VISIBLE

            val params: ViewGroup.LayoutParams = image_pod_view.layoutParams
            params.height =
                if (isExpanded) ViewGroup.LayoutParams.MATCH_PARENT
                else ViewGroup.LayoutParams.WRAP_CONTENT
            image_pod_view.layoutParams = params
            image_pod_view.scaleType =
                if (isExpanded) ImageView.ScaleType.CENTER_CROP
                else ImageView.ScaleType.FIT_CENTER
        }
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
                image_pod_view_loading.visibility = View.VISIBLE
                image_pod_view.visibility = View.VISIBLE

                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                val title = serverResponseData.title
                val explanation = serverResponseData.explanation

                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    image_pod_view.load(url) {
                        lifecycle(this@PODFragment)
                        placeholder(R.drawable.loading)
                        error(R.drawable.ic_load_error_vector)
                    }
                }

                initToggleFavorite(data)

                if (title.isNullOrEmpty() && explanation.isNullOrEmpty()) {
                    toast("Fact is empty")
                } else {
                    bottom_sheet_description_header.text = title
                    bottom_sheet_description.text = explanation
                }
            }
            is PODData.Loading -> {
                image_pod_view_loading.visibility = View.VISIBLE
                image_pod_view.visibility = View.GONE
            }
            is PODData.Error -> {
                toast(data.error.message)
            }
        }
    }

    private fun initToggleFavorite(data: PODData.Success) {
        val pod =
            POD(data.serverResponseData.date.toString(),
                data.serverResponseData.title.toString(),
                data.serverResponseData.url.toString(),
            )

        podBDViewModel.allPods.observe(this, { podList ->
            podList?.let {
                val pods = podList
                is_favorite.isChecked = pods.contains(pod)
            }
        })

        is_favorite.setOnClickListener {
            if (is_favorite.isChecked) {
                podBDViewModel.insert(pod)
            } else {
                podBDViewModel.delete(pod)
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