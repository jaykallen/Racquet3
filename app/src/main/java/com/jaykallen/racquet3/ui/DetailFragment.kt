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
import androidx.navigation.Navigation
import com.jaykallen.racquet3.R
import com.jaykallen.racquet3.StartApp
import com.jaykallen.racquet3.managers.Helper
import com.jaykallen.racquet3.managers.SharedPrefsManager
import com.jaykallen.racquet3.model.RacquetModel
import com.jaykallen.racquet3.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.content_detail_toolbar.*
import kotlinx.android.synthetic.main.content_main_toolbar.*
import kotlinx.android.synthetic.main.content_main_toolbar.titleText
import kotlinx.android.synthetic.main.dialog_units.*
import kotlinx.android.synthetic.main.dialog_yesno.*
import kotlinx.android.synthetic.main.fragment_detail.*

// todo add calculations to the viewmodel

class DetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel
    private val slopeMetric = 0.3175
    private val slopeInches = 0.125
    private var mUnits: String? = null
    private var mBalance: String = "Head Light"
    private var recordId: Long = 0L
    private var mSlope: Double = 0.toDouble()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("***************** Detail Fragment *******************")
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        getSafeArgs()
        listenData()
        setupToolbar()
        setupButtons(view!!)
    }

    private fun setupButtons(view: View) {
        view.findViewById<TextView>(R.id.doneText).setOnClickListener {
            onDoneClick()
        }
        view.findViewById<Button>(R.id.unitsButton).setOnClickListener {
            dialogUnits()
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
        if (headWeightEdit.text.toString() == "") {
            viewModel.calcHeadWeight(mUnits, lengthEdit.text.toString(), balancePointEdit.text.toString())
        } else {
            viewModel.calcBalancePoint(mUnits, lengthEdit.text.toString(), headWeightEdit.text.toString(), true)
        }
        viewModel.statLiveData.observe(this, Observer { item ->
            updateUi(item)
        })
    }

    private fun setupToolbar() {
        titleText.text = "Detail"
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
                updateUi(item)
            })
        } else {
            titleText.text = "New Racquet"
        }
    }

    private fun updateUi(racquet: RacquetModel) {
        println("Updating UI with record $recordId: ${racquet.name}")
        nameEdit.setText(racquet.name)
        lengthEdit.setText(racquet.length.toString())
        weightEdit.setText(racquet.weight.toString())
        balancePointEdit.setText(racquet.balancePoint.toString())
        headWeightEdit.setText(racquet.headWeight.toString())
        notesEdit.setText(racquet.notes)
    }

    private fun setUnits() {
        if (SharedPrefsManager.getUnits(StartApp.applicationContext()) == "Inches") {
            unitText.text = "Inches"
            lengthUnitsText.text = "in"
            balancePointUnitsText.text = "in"
            weightUnitsText.text = "oz"
            mSlope = slopeInches
        } else {
            unitText.text = "Metric"
            lengthUnitsText.text = "cm"
            balancePointUnitsText.text = "cm"
            weightUnitsText.text = "grams"
            mSlope = slopeMetric
        }
    }

    private fun changeBalance() {
        when (mBalance) {
            "Head Light" -> mBalance = "Even"
            "Even" -> mBalance = "Head Heavy"
            "Head Heavy" -> mBalance = "Head Light"
        }
        balanceButton.text = mBalance
    }

    private fun createRacquet(): RacquetModel {
        return RacquetModel(
            0, nameEdit.text.toString(), mUnits ?: "",
            headSizeEdit.text.toString().toDouble(),
            lengthEdit.text.toString().toDouble(),
            weightEdit.text.toString().toDouble(),
            mBalance,
            balancePointEdit.text.toString().toDouble(),
            headWeightEdit.text.toString().toDouble(),
            "Luxilon", "16x19", 52.0,
            notesEdit.text.toString()
        )
    }

    private fun onDoneClick() {
        val racquetModel = createRacquet()
        if (recordId == 0L) {
            println("Add racquet " + nameEdit.text.toString())
            viewModel.insert(racquetModel)
        } else {
            println("Update racquet " + nameEdit.text.toString())
            racquetModel.id = 1
            viewModel.update(racquetModel)
        }
    }

    private fun onDeleteClick() {
        val dialog = Dialog(activity, R.style.DialogStyle)
        dialog.setContentView(R.layout.dialog_yesno)
        dialog.setTitle("Delete Record")
        dialog.messageText.text = "Are you sure you want to delete this record?"
        dialog.yesButton.setOnClickListener {
            val racquetModel = createRacquet()
            viewModel.delete(racquetModel)
            dialog.dismiss()
        }
        dialog.noButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun dialogUnits() {         // This will set the units
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


}
