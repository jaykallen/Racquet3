package com.jaykallen.racquet3.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.jaykallen.racquet3.R
import com.jaykallen.racquet3.StartApp
import com.jaykallen.racquet3.databinding.FragmentHomeBinding
import com.jaykallen.racquet3.managers.SharedPrefsManager
import com.jaykallen.racquet3.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.content_main_toolbar.*
import kotlinx.android.synthetic.main.dialog_units.*
import kotlinx.android.synthetic.main.dialog_yesno.*
import kotlinx.android.synthetic.main.fragment_home.*


// JK 2019-11-11: Attempt to use Room db in Sandbox environment to put into Manage Right later.

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("***************** Home Fragment *******************")
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.viewModel = homeViewModel
        setupButtons(view!!)
        setupToolbar()
        launchStory()
    }

    private fun setupToolbar() {
        titleText.text = "In Development"
        addImage.visibility = View.INVISIBLE
        val units = SharedPrefsManager.getUnits(StartApp.applicationContext())
        unitsButton.text = "Change Units from $units"
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
        view.findViewById<Button>(R.id.unitsButton).setOnClickListener {
            dialogUnits()
        }
        view.findViewById<Button>(R.id.clearButton).setOnClickListener {
            dialogClearAll()
        }
    }

    private fun dialogUnits() {
        val dialog = Dialog(activity, R.style.DialogStyle)
        dialog.setContentView(R.layout.dialog_units)
        dialog.setTitle("Units")
        dialog.unitsTextView.text = "Please select a Unit of Measure"
        dialog.inchesButton.setOnClickListener {
            SharedPrefsManager.setUnits(StartApp.applicationContext(), "Inches")
            setUnits()
            dialog.dismiss()
        }
        dialog.metricButton.setOnClickListener {
            SharedPrefsManager.setUnits(StartApp.applicationContext(), "Metric")
            setUnits()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setUnits() {
        if (SharedPrefsManager.getUnits(StartApp.applicationContext()) == "Inches") {
            println("Units set to Inches")
            SharedPrefsManager.setUnits(StartApp.applicationContext(), "Inches")
            unitsButton.text = "Change Units from Inches"
        } else {
            println("Units set to Metric")
            SharedPrefsManager.setUnits(StartApp.applicationContext(), "Metric")
            unitsButton.text = "Change Units from Metric"
        }
    }

    private fun dialogClearAll() {
        val dialog = Dialog(activity, R.style.DialogStyle)
        dialog.setContentView(R.layout.dialog_yesno)
        dialog.setTitle("Delete All")
        dialog.messageText.text = "Are you sure you want to delete all items?"
        dialog.yesButton.setOnClickListener {
            println("Delete all items!")
            homeViewModel.deleteAll()
            dialog.dismiss()
        }
        dialog.noButton.setOnClickListener {
            println("Cancel delete")
            dialog.dismiss()
        }
        dialog.show()
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
