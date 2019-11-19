package com.jaykallen.racquet3.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jaykallen.racquet3.R
import com.jaykallen.racquet3.StartApp
import com.jaykallen.racquet3.model.RacquetModel
import kotlinx.android.synthetic.main.recycler_racquet.view.*


class CatalogAdapter(
    private val itemList: List<RacquetModel>, var clickLambda: (RacquetModel) -> Unit
) : RecyclerView.Adapter<CatalogAdapter.RecyclerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_racquet, parent, false)
        return RecyclerHolder(view)
    }

    override fun onBindViewHolder(recyclerholder: RecyclerHolder, position: Int) {
        val item: RacquetModel = itemList[position]
        recyclerholder.bindRecyclerHolder(item)
        recyclerholder.bindRecyclerData(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class RecyclerHolder(holder: View) : RecyclerView.ViewHolder(holder),
        View.OnClickListener {
        private lateinit var item: RacquetModel
        private val nameText: TextView = holder.nameText
        private val weightText: TextView = holder.weightText

        init {
            holder.setOnClickListener(this)
        }

        fun bindRecyclerHolder(item: RacquetModel) {
            this.item = item
            val color: Int = when {
                item.balance != null -> R.color.colorBallGreen
                else -> R.color.colorYellow
            }
            itemView.setBackgroundColor(ContextCompat.getColor(StartApp.applicationContext(), color))
        }

        fun bindRecyclerData(item: RacquetModel) {
            nameText.text = item.name
            weightText.text = "${item.weight}"
        }

        override fun onClick(v: View) {
            clickLambda(item)
        }
    }
}
