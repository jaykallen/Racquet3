package com.jaykallen.racquet3

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.core.view.GestureDetectorCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.jaykallen.racquet3.room.RoomyDatabase
import com.jaykallen.racquet3.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*

// JK 2019-11-11: Attempt to use Room db in Sandbox environment to put into Manage Right later.

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("***************** Home Fragment *******************")
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.viewModel = homeViewModel
        setupButtons(view!!)
        launchStory()
    }

    private fun setupButtons(view: View) {
        view.findViewById<Button>(R.id.catalogButton).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_catalogFragment)
        }
        view.findViewById<Button>(R.id.guideButton).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_guideFragment)
        }
        view.findViewById<Button>(R.id.measureButton).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_measureFragment)
        }
        view.findViewById<Button>(R.id.rulerButton).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_rulerFragment)
        }
    }

    private fun launchStory() {
        homeViewModel.loadDataRefresher()
        homeViewModel.storyLiveData.observe(this, Observer { story ->
            storyText.text = story
        })
    }

    override fun onPause() {
        super.onPause()
        homeViewModel.killDataRefresher()
    }

}
