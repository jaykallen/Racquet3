package com.procatdt.navsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaykallen.racquet3.R
import com.jaykallen.racquet3.model.RacquetModel
import com.jaykallen.racquet3.ui.CatalogAdapter
import kotlinx.android.synthetic.main.fragment_catalog.*


class CatalogFragment : Fragment() {
    private lateinit var recyclerAdapter: CatalogAdapter
    private lateinit var viewModel: CatalogViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)
        setupButtons(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("*** Catalog Fragment ***")
        viewModel = ViewModelProviders.of(this).get(CatalogViewModel::class.java)
        observeData()
    }

    private fun observeData() {
        viewModel.allLiveData.observe(this, Observer { allItems ->
            setupRecycler(allItems)
        })
    }

    private fun setupRecycler(items: List<RacquetModel>) {
        recyclerAdapter = CatalogAdapter(items) { messageModel ->
            onRecyclerClick(messageModel)
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

    private fun onRecyclerClick(racquetModel: RacquetModel) {
        println("RecyclerView selected $racquetModel")
        Navigation.findNavController(view!!).navigate(R.id.action_catalogFragment_to_detailFragment)
    }

    private fun setupButtons(view: View) {
        view.findViewById<Button>(R.id.add_button).setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.action_catalogFragment_to_detailFragment)
        }
    }

}
