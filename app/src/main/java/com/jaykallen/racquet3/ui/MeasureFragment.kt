package com.jaykallen.racquet3.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.jaykallen.racquet3.R
import kotlinx.android.synthetic.main.content_main_toolbar.*

/**
 * A simple [Fragment] subclass.
 */
class MeasureFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measure, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("***************** Measure Fragment *******************")
        setupToolbar()
        setupButtons(view!!)
    }

    private fun setupToolbar() {
        titleText.text = "How to Measure"
    }

    private fun setupButtons(view: View) {
        view.findViewById<Button>(R.id.rulerButton).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_measureFragment_to_rulerFragment)
        }
    }

}
