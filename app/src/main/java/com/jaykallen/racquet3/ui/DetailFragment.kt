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
import kotlinx.android.synthetic.main.content_main_toolbar.*
import kotlinx.android.synthetic.main.dialog_units.*
import kotlinx.android.synthetic.main.dialog_yesno.*
import kotlinx.android.synthetic.main.fragment_detail.*

// todo add calculations to the viewmodel

class DetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel
    private val slopeMetric = 0.3175
    private val slopeInches = 0.125
    private var mUnits: String? = null
    private var mBalance: String = ""
    private var recordId: Long = 0L
    private var mSlope: Double = 0.toDouble()
    private var mLength = 0.0
    private var mBalancePoint = 0.0
    private var mHeadWeight = 0.0

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
        println("listening to id call")
        viewModel.getId(recordId)
        viewModel.idLiveData.observe(this, Observer { item ->
            updateUi(item)
        })
    }

    private fun updateUi(racquet: RacquetModel) {
        println("Updating UI with ${racquet.name}")
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

    private fun setupButtons(view: View) {
        view.findViewById<TextView>(R.id.cancelText).setOnClickListener {
            onCancelClick()
        }
        view.findViewById<TextView>(R.id.doneText).setOnClickListener {
            onDoneClick()
        }
        view.findViewById<Button>(R.id.unitButton).setOnClickListener {
            dialogUnits()
        }
        view.findViewById<Button>(R.id.calculateButton).setOnClickListener {
            calcHeadWeight()
            calcBalancePoint()
        }
        view.findViewById<Button>(R.id.deleteButton).setOnClickListener {
            onDeleteClick()
        }
    }

    private fun calcHeadWeight() {
        val balancePoint = Helper.verifyEntry(balancePointEdit.text.toString())
        println("Calculating headweight: " + balancePoint)
        if (balancePoint > 0.0) {
            mLength = Helper.verifyEntry(lengthEdit.text.toString())
            mHeadWeight = Helper.calcHeadWeight(mSlope, mLength, balancePoint)
            println("Slope: $mSlope Length:$mLength BP:$balancePoint")
            setDirectionSpinner2(mHeadWeight)
            headWeightEdit.setText(String.format("%.5g", Math.abs(mHeadWeight)))
        }
    }

    private fun calcBalancePoint() {
        var headWeight: Double? = Helper.verifyEntry(headWeightEdit.text.toString())
        println("Calculating Balancepoint: " + headWeight!!)
        if (headWeight > 0.0) {
            headWeight = getDirectionSpinner(headWeight)
            mLength = Helper.verifyEntry(lengthEdit.text.toString())
            mBalancePoint = Helper.calcBalancePoint(mSlope, mLength, headWeight)
            balancePointEdit.setText(Math.abs(mBalancePoint).toString() + "")
        }
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
        dialog.clearTextView.text = "Are you sure you want to delete this record?"
        dialog.yesButton.setOnClickListener {
            val racquetModel = createRacquet()
            viewModel.delete(racquetModel)
            dialog.dismiss()
        }
        dialog.noButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun onCancelClick() {
        Navigation.findNavController(view!!).popBackStack(R.id.action_detailFragment_to_catalogFragment ,false)
    }

    private fun setDirectionSpinner2(direction: Double) {
        balanceSpinner.post {
            var headdir = 1
            mBalance = "Even"
            if (direction < 0) {
                headdir = 0
                mBalance = "Head Light"
            } else if (direction > 0) {
                headdir = 2
                mBalance = "Head Heavy"
            }
            balanceSpinner.setSelection(headdir)
        }
    }

    private fun getDirectionSpinner(value: Double?): Double {
        var value = value
        val headdir = balanceSpinner.selectedItemPosition
        if (headdir == 0) {
            value = value!! * -1
        }
        return value!!
    }

    private fun dialogUnits() {
        // This will set the units
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
