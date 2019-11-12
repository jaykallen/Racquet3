package com.procatdt.navsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaykallen.racquet3.R
import com.jaykallen.racquet3.model.RacquetModel
import com.jaykallen.racquet3.room.RoomMgr
import com.jaykallen.racquet3.ui.CatalogAdapter
import kotlinx.android.synthetic.main.fragment_catalog.*


class CatalogFragment : Fragment() {
    private lateinit var recyclerAdapter: CatalogAdapter
    private lateinit var viewModel: CatalogViewModel
    private lateinit var racquets: ArrayList<RacquetModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)
        println("*** Catalog Fragment ***")
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CatalogViewModel::class.java)

        getList()
    }

    private fun getList() {
        val roomMgr = RoomMgr.getAll()
        roomMgr.racquetsLiveData.observe(this, Observer { racquets ->
            println ("Retrieved ${racquets.size} records")
            if (racquets.size > 0) {
                setupRecycler(racquets)
                var results = ""
                for (i in 0 until racquets.size) {
                    println ("${racquets[i].id}) ${racquets[i].name} ${racquets[i].balancePoint} ${racquets[i].headSize} ${racquets[i].length}")
                }
            }
        })
        roomMgr.execute()
    }

    private fun setupRecycler(items: ArrayList<RacquetModel>) {
        recyclerAdapter = CatalogAdapter(items) { messageModel ->
            onRecyclerClick(messageModel)
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = recyclerAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
    }

    private fun onRecyclerClick(racquetModel: RacquetModel) {
        println("RecyclerView selected $racquetModel")
        Navigation.findNavController(view!!).navigate(R.id.action_catalogFragment_to_detailFragment)
    }

}
