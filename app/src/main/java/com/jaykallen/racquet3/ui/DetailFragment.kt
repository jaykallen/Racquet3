package com.jaykallen.racquet3.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jaykallen.racquet3.R
import com.jaykallen.racquet3.StartApp
import com.jaykallen.racquet3.managers.SharedPrefsManager
import com.jaykallen.racquet3.model.RacquetModel
import com.jaykallen.racquet3.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.content_main_toolbar.*
import kotlinx.android.synthetic.main.dialog_units.*
import kotlinx.android.synthetic.main.dialog_yesno.*
import kotlinx.android.synthetic.main.fragment_detail.*

// JK 2019-12-03: This is a very complex screen with many buttons
// todo setup conversion for metric

class DetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel
    private var mUnits: String = "Inches"
    private var mBalance: String = "Head Light"
    private var recordId: Long = -1L                    // Default for new racquet
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
        if (recordId >= 0L) {
            viewModel.getId(recordId)
            viewModel.idLiveData.observe(this, Observer { item ->
                racquet = item
                titleText.text = "Update"
            })
        } else {
            titleText.text = "New Racquet"
        }
        updateUi(racquet)
    }

    private fun setupButtons(view: View) {
        view.findViewById<TextView>(R.id.saveText).setOnClickListener {
            onSaveClick()
        }
        view.findViewById<Button>(R.id.unitsButton).setOnClickListener {
            onUnitsClick()
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
        updateRacquet()
        viewModel.calcRacquet(racquet)
        viewModel.statLiveData.observe(this, Observer { modified ->
            racquet = modified
            updateUi(racquet)
        })
    }

    private fun updateUi(racquet: RacquetModel) {
        println("Updating UI with record $recordId: ${racquet.name}")
        nameEdit.setText(racquet.name)
        lengthEdit.setText(racquet.length.toString())
        weightEdit.setText(racquet.weight.toString())
        balancePointEdit.setText(racquet.balancePoint.toString())
        headWeightEdit.setText(racquet.headWeight.toString())   // todo format double to only 1 digit
        balanceButton.text = racquet.balance
        notesEdit.setText(racquet.notes)
    }


    private fun changeBalance() {
        when (mBalance) {
            "Head Light" -> mBalance = "Even"
            "Even" -> mBalance = "Head Heavy"
            "Head Heavy" -> mBalance = "Head Light"
        }
        racquet.balance = mBalance
        balanceButton.text = mBalance
    }

    private fun updateRacquet() {
        // todo add field validation
        racquet.name = nameEdit.text.toString()
        racquet.units = mUnits
        racquet.headSize = headSizeEdit.text.toString().toDouble()
        racquet.length = lengthEdit.text.toString().toDouble()
        racquet.weight = weightEdit.text.toString().toDouble()
        racquet.balance = mBalance
        racquet.balancePoint = balancePointEdit.text.toString().toDouble()
        racquet.headWeight = headWeightEdit.text.toString().toDouble()
        racquet.notes = notesEdit.text.toString()
    }

    private fun onSaveClick() {
        updateRacquet()
        if (recordId == -1L) {
            println("Add racquet " + nameEdit.text.toString())
            viewModel.insert(racquet)
        } else {
            println("Update racquet " + nameEdit.text.toString())
            racquet.id = recordId
            viewModel.update(racquet)
        }
    }

    private fun onDeleteClick() {
        val dialog = Dialog(activity, R.style.DialogStyle)
        dialog.setContentView(R.layout.dialog_yesno)
        dialog.setTitle("Delete Record")
        dialog.messageText.text = "Are you sure you want to delete this record?"
        dialog.yesButton.setOnClickListener {
            viewModel.delete(racquet)
            // todo exit the screen. how?
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
        SharedPrefsManager.setUnits(StartApp.applicationContext(), units)
        unitText.text = units
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
