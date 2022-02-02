package com.example.appnasapi.ui.pod

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import coil.load
import com.cesarferreira.tempo.Tempo
import com.cesarferreira.tempo.day
import com.cesarferreira.tempo.toString
import com.example.appnasapi.MainActivity
import com.example.appnasapi.R
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.main_fragment.*

class PODFragment : Fragment() {

    private val viewModel: PODViewModel by lazy {
        ViewModelProvider(this)[PODViewModel::class.java]
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

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

        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        setBottomAppBar(view)

        //Поиск в Wiki по нажатию на custom_icon
        input_wiki_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${input_edit_wiki_text.text.toString()}")
            })
        }

        //Инициализация обработчика нажатий Chips_POD
        initChipsPOD()
    }

    private fun initChipsPOD() {
        pod_yesterday.setOnClickListener {
            viewModel.sendServerRequest(Tempo.yesterday.toString("yyyy-MM-dd"))
        }

        pod_today.setOnClickListener {
            viewModel.sendServerRequest(Tempo.now.toString("yyyy-MM-dd"))
        }

        pod_day_before_yesterday.setOnClickListener {
            viewModel.sendServerRequest(2.day.ago.toString("yyyy-MM-dd"))
        }
    }


    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))

        setHasOptionsMenu(true)

        fab.setOnClickListener {
            if (isMain) {
                isMain = false
                bottom_app_bar.navigationIcon = null
                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back_fab))
            } else {
                isMain = true
                bottom_app_bar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, tag)
                }
            }
        }


        return super.onOptionsItemSelected(item)
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