package com.example.appnasapi.ui.favorites

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.appnasapi.R
import com.example.appnasapi.bd.POD
import com.example.appnasapi.databinding.PodFavoriteItemLayoutBinding
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.pod_favorite_item_layout.view.*

class PODAdapter(private val dragListener: OnStartDragListener) :
    RecyclerView.Adapter<PODAdapter.PODViewHolder>(), ItemTouchHelperAdapter {

    var podList: MutableList<POD> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listenerRemovePODAtList: removePODAtList? = null

    inner class PODViewHolder(item: View) : RecyclerView.ViewHolder(item),
        ItemTouchHelperViewHolder {
        private val binding = PodFavoriteItemLayoutBinding.bind(item)
        fun bind(pod: POD) {
            with(binding) {
                podTitle.text = pod.title
                podDate.text = pod.date
                podDescription.text = pod.explanation
                podImage.load(pod.url) {
                    placeholder(R.drawable.loading)
                }
            }
            itemView.moveItemPODDown.setOnClickListener { moveDown() }
            itemView.moveItemPODUp.setOnClickListener { moveUp() }
            itemView.dragHandlePOD.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this)
                }
                false
            }

            itemView.setOnClickListener {
                if (binding.podDescription.visibility == GONE) {
                    binding.podDescription.visibility = VISIBLE
                } else {
                    binding.podDescription.visibility = GONE
                }
                notifyItemChanged(layoutPosition)
            }

        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 0 }?.also { currentPosition ->
                podList.removeAt(currentPosition).apply {
                    podList.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < podList.size - 1 }?.also { currentPosition ->
                podList.removeAt(currentPosition).apply {
                    podList.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        //TODO скорректировать
        override fun onItemSelected() {
            val podCard = itemView.findViewById<MaterialCardView>(R.id.podItem)
            podCard.alpha  = 0.6f
        }

        override fun onItemClear() {
            val podCard = itemView.findViewById<MaterialCardView>(R.id.podItem)
            podCard.alpha  = 1f
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

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        podList.removeAt(fromPosition).apply {
            podList.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        //listenerRemovePODAtList?.podRemove(podList[position])
        podList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun interface removePODAtList {
        fun podRemove(pod: POD)
    }

}

interface OnStartDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}