package com.jaykallen.racquet.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.jaykallen.racquet.R
import com.jaykallen.racquet.StartApp
import com.jaykallen.racquet.managers.Helper
import com.jaykallen.racquet.managers.SharedPrefsManager
import com.jaykallen.racquet.model.RacquetModel
import com.jaykallen.racquet.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.content_main_toolbar.*
import kotlinx.android.synthetic.main.dialog_units.*
import kotlinx.android.synthetic.main.dialog_yesno.*
import kotlinx.android.synthetic.main.fragment_detail.*
import java.lang.Exception

// JK 2019-12-03: This is a very complex screen with many buttons.  Possibly switch this to Data Binding??
// When 0 is requested from Room, this indicates a new record

class DetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel
    private var recordId: Long = 0L                    // Default for new racquet
    private var racquet = RacquetModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("***************** Detail Fragment *******************")
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        getSafeArgs()
        listenData()
        setupButtons(view!!)
    }

    private fun getSafeArgs() {
        arguments?.let {
            val args = DetailFragmentArgs.fromBundle(it)
            recordId = args.id
            println("Safe Argument Received=${args.id}")
        }
    }

    private fun listenData() {
        if (recordId > 0L) {
            viewModel.getId(recordId)
            viewModel.idLiveData.observe(this, Observer { item ->
                racquet = item
                titleText.text = "Update $recordId"
                updateUi(racquet)
            })
        } else {
            titleText.text = "New"
            setUnits(SharedPrefsManager.getUnits(StartApp.applicationContext()))
            updateUi(racquet)
        }
    }

    private fun setupButtons(view: View) {
        view.findViewById<TextView>(R.id.saveText).setOnClickListener {
            onSaveClick()
        }
        view.findViewById<Button>(R.id.unitsButton).setOnClickListener {
            onUnitsClick()
        }
        view.findViewById<Button>(R.id.rulerButton).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_detailFragment_to_rulerFragment)
        }
        view.findViewById<Button>(R.id.calculateButton).setOnClickListener {
            onCalcClick()
        }
        view.findViewById<Button>(R.id.deleteButton).setOnClickListener {
            onDeleteClick()
        }
        view.findViewById<Button>(R.id.balanceButton).setOnClickListener {
            changeBalance()
        }
    }

    private fun onCalcClick() {
        if (validateFields()) {
            updateRacquet()
            viewModel.calcRacquet(racquet)
            viewModel.statLiveData.observe(this, Observer { modified ->
                racquet = modified
                updateUi(racquet)
            })
        }
    }

    private fun onSaveClick() {
        if (validateFields()) {
            updateRacquet()
            if (recordId == 0L) {
                println("Insert $racquet")
                viewModel.insert(racquet)
            } else {
                println("Update $racquet")
                racquet.id = recordId
                viewModel.update(racquet)
            }
            activity?.onBackPressed()
        }
    }

    private fun updateRacquet() {
        racquet.name = nameEdit.text.toString()
        racquet.units = unitsButton.text.toString()
        racquet.headSize = Helper.toDouble(headSizeEdit.text.toString())
        racquet.length = Helper.toDouble(lengthEdit.text.toString())
        racquet.weight = Helper.toDouble(weightEdit.text.toString())
        racquet.balance = balanceButton.text.toString()
        racquet.balancePoint = Helper.toDouble(balancePointEdit.text.toString())
        racquet.headWeight = Helper.toDouble(headWeightEdit.text.toString())
        racquet.notes = notesEdit.text.toString()
    }

    private fun updateUi(racquet: RacquetModel) {
        println("Updating UI with record $recordId: ${racquet.name}")
        nameEdit.setText(racquet.name)
        unitsButton.text = racquet.units
        lengthEdit.setText(racquet.length.toString())
        weightEdit.setText(racquet.weight.toString())
        balancePointEdit.setText(racquet.balancePoint.toString())
        headWeightEdit.setText(String.format("%.1f", racquet.headWeight))
        balanceButton.text = racquet.balance
        notesEdit.setText(racquet.notes)
    }

    private fun changeBalance() {
        when (balanceButton.text.toString()) {
            "Head Light" -> racquet.balance = "Even"
            "Even" -> racquet.balance = "Head Heavy"
            "Head Heavy" -> racquet.balance = "Head Light"
        }
        balanceButton.text = racquet.balance
    }

    private fun validateFields(): Boolean {
        var errorString = ""
        if (nameEdit.text.toString().isEmpty()) errorString = "Name cannot be blank. "
        if (lengthEdit.text.toString().toDouble() <= 0.0) errorString += "Length cannot be 0."
        if (errorString != "") Toast.makeText(activity, errorString, Toast.LENGTH_LONG).show()
        return errorString == ""
    }

    private fun onDeleteClick() {
        val dialog = Dialog(activity, R.style.DialogStyle)
        dialog.setContentView(R.layout.dialog_yesno)
        dialog.setTitle("Delete Record")
        dialog.messageText.text = "Are you sure you want to delete this record?"
        dialog.yesButton.setOnClickListener {
            viewModel.delete(racquet)
            try {
                activity?.onBackPressed()
            } catch (e: Exception) {
                println(e.message)
            }
            dialog.dismiss()
        }
        dialog.noButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun onUnitsClick() {         // This will set the units
        val dialog = Dialog(activity, R.style.DialogStyle)
        dialog.setContentView(R.layout.dialog_units)
        dialog.setTitle("Units")
        dialog.unitsTextView.text = "Please select a Unit of Measure"
        dialog.inchesButton.setOnClickListener {
            setUnits("Inches")
            dialog.dismiss()
        }
        dialog.metricButton.setOnClickListener {
            setUnits("Metric")
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setUnits(units: String) {   // Update the record, shared prefs, and the UI
        racquet.units = units
        unitsButton.text = units
        SharedPrefsManager.setUnits(StartApp.applicationContext(), units)
        if (units == "Inches") {
            headSizeUnitsText.text = "sq in"
            lengthUnitsText.text = "in"
            balancePointUnitsText.text = "in"
            weightUnitsText.text = "oz"
        } else {
            headSizeUnitsText.text = "sq cm"
            lengthUnitsText.text = "cm"
            balancePointUnitsText.text = "cm"
            weightUnitsText.text = "grams"
        }
    }

}
