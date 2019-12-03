package com.jaykallen.racquet3.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jaykallen.racquet3.R
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

    inner class RecyclerHolder(holder: View) : RecyclerView.ViewHolder(holder), View.OnClickListener {
        private lateinit var item: RacquetModel
        private val inches = arrayOf("sq in", "in", "oz")
        private val metric = arrayOf("sq cm", "cm", "g")
        private val nameText: TextView = holder.nameText
        private val headSizeText: TextView = holder.headSizeText
        private val lengthText: TextView = holder.lengthText
        private val weightText: TextView = holder.weightText
        private val balanceText: TextView = holder.balanceText
        private val notesText: TextView = holder.notesText

        init {
            holder.setOnClickListener(this)
        }

        fun bindRecyclerHolder(item: RacquetModel) {
            this.item = item
        }

        fun bindRecyclerData(item: RacquetModel) {
            val unitsArr = if (item.units == "Inches") inches else metric
            nameText.text = item.name
            headSizeText.text = "size: ${item.headSize} ${unitsArr[0]}"
            lengthText.text = "length: ${item.length} ${unitsArr[1]}"
            weightText.text = "weight: ${item.weight} ${unitsArr[2]}"
            val balance = when (item.balance) {
                "Head Light" -> "HL"
                "Head Heavy" -> "HH"
                else -> "EV"
            }
            balanceText.text = "balance: ${item.headWeight} pts $balance"
            notesText.text = item.notes
        }

        override fun onClick(v: View) {
            clickLambda(item)
        }
    }
}
