package com.example.appnasapi.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.appnasapi.R
import com.example.appnasapi.bd.POD
import com.example.appnasapi.databinding.PodFavoriteItemLayoutBinding

class PODAdapter : RecyclerView.Adapter<PODAdapter.PODViewHolder>() {

    var podList: MutableList<POD> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class PODViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = PodFavoriteItemLayoutBinding.bind(item)
        fun bind(pod: POD) {
            with(binding) {
                podTitle.text = pod.title
                podDate.text = pod.date
                podImage.load(pod.url) {
                    placeholder(R.drawable.loading)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PODViewHolder =
        PODViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.pod_favorite_item_layout, parent, false)
        )

    override fun onBindViewHolder(holder: PODViewHolder, position: Int) {
        holder.bind(podList[position])
    }

    override fun getItemCount(): Int {
        return podList.size
    }

}