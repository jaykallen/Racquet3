package com.jaykallen.racquet3.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jaykallen.racquet3.R
import com.jaykallen.racquet3.model.RulerModel
import kotlinx.android.synthetic.main.recycler_ruler.view.*


class RulerAdapter(
    private val itemList: List<RulerModel>, var clickLambda: (RulerModel) -> Unit
) : RecyclerView.Adapter<RulerAdapter.RecyclerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_ruler, parent, false)
        return RecyclerHolder(view)
    }

    override fun onBindViewHolder(recyclerholder: RecyclerHolder, position: Int) {
        val item: RulerModel = itemList[position]
        recyclerholder.bindRecyclerHolder(item)
        recyclerholder.bindRecyclerData(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class RecyclerHolder(holder: View) : RecyclerView.ViewHolder(holder),
        View.OnClickListener {
        private lateinit var item: RulerModel
        private val fractionText: TextView = holder.fractionText
        private val decimalText: TextView = holder.decimalText
        private val millimeterText: TextView = holder.millimeterText

        init {
            holder.setOnClickListener(this)
        }

        fun bindRecyclerHolder(item: RulerModel) {
            this.item = item
        }

        fun bindRecyclerData(item: RulerModel) {
            fractionText.text = item.fraction
            decimalText.text = "${item.decimal}"
            millimeterText.text = "${item.millimeter}"
        }

        override fun onClick(v: View) {
            clickLambda(item)
        }
    }
}