package com.jaykallen.racquet3


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaykallen.racquet3.model.RulerModel
import com.jaykallen.racquet3.ui.RulerAdapter
import kotlinx.android.synthetic.main.content_info_toolbar.*
import kotlinx.android.synthetic.main.fragment_ruler.*


class RulerFragment : Fragment() {
    private lateinit var recyclerAdapter: RulerAdapter
    private val fractionArray = arrayOf("1/16", "1/8", "3/16", "1/4", "5/16", "3/8", "7/16", "1/2", "9/16", "5/8", "11/16", "3/4", "13/16", "7/8", "15/16")
    private val decimalArray = arrayOf(.0625, .125, .1875, .25, .3125, .375, .4375, .5, .5625, .625, .6875, .75, .8125, .875, .9375)
    private val millimeterArray = arrayOf(1.5875, 3.175, 4.7625, 6.35, 7.9375, 9.525, 11.1125, 12.7, 14.2875, 15.875, 17.4625, 19.05, 20.6375, 22.225, 23.8125)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ruler, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("***************** Ruler Fragment *******************")
        setupRecycler(rulerArrayList())
        setupToolbar()
        setupButtons(view!!)
    }

    private fun setupToolbar() {
        titleText.text = "Ruler Guide"
    }

    private fun setupButtons(view: View) {
        view.findViewById<TextView>(R.id.backText).setOnClickListener {
//            view.finish()         // todo how to finish a fragment??
        }
    }

    private fun rulerArrayList(): ArrayList<RulerModel> {
        val segments = ArrayList<RulerModel>()
        for (i in 0..14) {
            segments.add(RulerModel(i + 1, fractionArray[i], decimalArray[i], millimeterArray[i]))
        }
        return segments
    }

    private fun setupRecycler(items: List<RulerModel>) {
        recyclerAdapter = RulerAdapter(items) { segment ->
            onRecyclerClick(segment)
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = recyclerAdapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun onRecyclerClick(segment: RulerModel) {
        println("RecyclerView selected row=${segment.row}")
    }
}
