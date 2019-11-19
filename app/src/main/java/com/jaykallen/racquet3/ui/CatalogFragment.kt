package com.procatdt.navsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaykallen.racquet3.R
import com.jaykallen.racquet3.model.RacquetModel
import com.jaykallen.racquet3.ui.CatalogAdapter
import kotlinx.android.synthetic.main.content_main_toolbar.*
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
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("***************** Catalog Fragment *******************")
        viewModel = ViewModelProviders.of(this).get(CatalogViewModel::class.java)
        setupButtons()
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
        println("RecyclerView selected $racquetModel with id=${racquetModel.id}")
        navigateDetail(racquetModel.id)
    }

    private fun setupButtons() {
        addImage.setOnClickListener {
            navigateDetail(0L)
        }
    }

    private fun navigateDetail(id: Long) {
        println("Navigating to record $id")
        val action = CatalogFragmentDirections.actionCatalogFragmentToDetailFragment().setId(id)
        Navigation.findNavController(view!!).navigate(action)
    }

}
