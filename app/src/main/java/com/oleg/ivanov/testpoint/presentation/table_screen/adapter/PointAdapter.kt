package com.oleg.ivanov.testpoint.presentation.table_screen.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oleg.ivanov.testpoint.R
import com.oleg.ivanov.testpoint.repository.model.PointModel

/**
 * Адаптер для отображения точек
 */
class PointAdapter(
    private val dataSource: List<PointModel>,
) : RecyclerView.Adapter<PointAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MessageViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_point, viewGroup, false)
        return MessageViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: MessageViewHolder, i: Int) {
        dataSource[i].let { point ->
            viewHolder.textViewPoint.text = "x=${point.x} y=${point.y}"

        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    class MessageViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        internal var textViewPoint: TextView = mView.findViewById(R.id.textViewPoint)
    }
}
