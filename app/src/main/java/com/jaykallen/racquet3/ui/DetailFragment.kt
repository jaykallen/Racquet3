package com.jaykallen.racquet3.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.jaykallen.racquet3.R
import com.jaykallen.racquet3.StartApp
import com.jaykallen.racquet3.managers.Helper
import com.jaykallen.racquet3.managers.SharedPrefsManager
import com.jaykallen.racquet3.model.RacquetModel
import com.procatdt.navsample.CatalogViewModel
import kotlinx.android.synthetic.main.dialog_units.*
import kotlinx.android.synthetic.main.dialog_yesno.*
import kotlinx.android.synthetic.main.fragment_detail.*


class DetailFragment : Fragment() {
    private lateinit var viewModel: CatalogViewModel
    private val slopeMetric = 0.3175
    private val slopeInches = 0.125
    private var mUnits: String? = null
    private var mBalance: String = ""
    private var mRacquetId: String = ""
    private var mSlope: Double = 0.toDouble()
    private var mLength = 0.0
    private var mBalancePoint = 0.0
    private var mHeadWeight = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        setupButtons(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CatalogViewModel::class.java)
        println("***************** RacquetModel Activity *******************")
        updateUi()
        setUnits()
    }

    private fun updateUi() {
        //        mRacquetId = intent.getStringExtra(EXTRA_ID)?:""
//        if (mRacquetId != "add") {
//            val racquet = RealmManager.getRacquet(this, mRacquetId)
//            if (racquet != null) {
//                nameEdit.setText(racquet.name)
//                lengthEdit.setText(racquet.length)
//                balancePointEdit.setText(racquet.balancePoint)
//                headWeightEdit.setText(racquet.headWeight)
//                weightEdit.setText(racquet.weight)
//                setDirectionSpinner(racquet.headDirection ?: "")
//                notesEdit.setText(racquet.notes)
//            }
//        } else {
//            deleteButton.visibility = View.INVISIBLE
//        }
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
        view.findViewById<Button>(R.id.calculateButton).setOnClickListener {
            calcHeadWeight()
            calcBalancePoint()
        }
        view.findViewById<Button>(R.id.deleteButton).setOnClickListener {
            onDeleteClick()
        }
        view.findViewById<Button>(R.id.cancelText).setOnClickListener {
            onCancelClick()
        }
        view.findViewById<Button>(R.id.doneText).setOnClickListener {
            onDoneClick()
        }
        view.findViewById<Button>(R.id.unitButton).setOnClickListener {
            dialogUnits()
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

    private fun createRacquet() :RacquetModel {
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
        if (mRacquetId == "add") {
            println("Add racquet " + nameEdit.text.toString())
            viewModel.insert(racquetModel)
        } else {
            println("Update racquet " + nameEdit.text.toString())
            racquetModel.id = 1
            viewModel.insert(racquetModel)
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
        Navigation.findNavController(view!!).navigate(R.id.action_detailFragment_to_catalogFragment)
    }

    private fun setDirectionSpinner2(direction: Double) {
        directionSpinner.post {
            var headdir = 1
            mBalance = "Neutral"
            if (direction < 0) {
                headdir = 0
                mBalance = "Head Light"
            } else if (direction > 0) {
                headdir = 2
                mBalance = "Head Heavy"
            }
            directionSpinner.setSelection(headdir)
        }
    }

    private fun getDirectionSpinner(value: Double?): Double {
        var value = value
        val headdir = directionSpinner.selectedItemPosition
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
