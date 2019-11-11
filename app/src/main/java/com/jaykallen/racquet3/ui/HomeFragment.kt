package com.jaykallen.racquet3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jaykallen.racquet3.room.RoomInstance
import com.jaykallen.racquet3.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*

// JK 2019-11-11: Attempt to use Room db in Sandbox environment to put into Manage Right later.

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        var view = binding.root
        setupButtons(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.viewModel = homeViewModel
        setupSubscriber()
        setupRoom()
    }

    private fun setupRoom() {
        RoomInstance.getInstance(StartApp.applicationContext())
    }

    private fun setupButtons(view: View) {
        view.findViewById<Button>(R.id.info_button).setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_infoFragment)
        }
        view.findViewById<Button>(R.id.settings_button).setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_settingsFragment)
        }
    }

    private fun setupSubscriber() {
        homeViewModel.storyLiveData.observe(this, Observer { subscriber ->
            actionText.text = subscriber
        })
    }

    override fun onPause() {
        super.onPause()
        homeViewModel.killDataRefresher()
    }

}
