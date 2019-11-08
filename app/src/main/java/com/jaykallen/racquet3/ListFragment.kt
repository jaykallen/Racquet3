package com.procatdt.navsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jaykallen.racquet3.R


class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        println("*** Login Fragment ***")
        setupButtons(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
    }

    private fun setupButtons(view: View) {
//        view.findViewById<Button>(R.id.passwordButton).setOnClickListener {
////            val enteredPassword = "jetpackrox"
////            if (viewModel.validatePassword(enteredPassword)) {
////                passwordText.text = "Password: <validated>"
////            } else {
////                passwordText.text = "Password: Incorrect"
////            }
//        }
//        view.findViewById<Button>(R.id.loginButton).setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment)
//        }
    }

}
