package com.jaykallen.racquet3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jaykallen.racquet3.managers.Helper
import com.jaykallen.racquet3.model.RacquetModel
import com.jaykallen.racquet3.room.RoomRepository
import com.jaykallen.racquet3.room.RoomyDatabase
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val slopeMetric = 0.3175
    private val slopeInches = 0.125
    private val repository: RoomRepository
    var idLiveData = MutableLiveData<RacquetModel>()

    init {
        val dao = RoomyDatabase.getDatabase(application).roomDao()
        repository = RoomRepository(dao)
    }

    fun getId(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val item = repository.getId(id)
            withContext(Dispatchers.Main) {
                idLiveData.value = item
            }
        }
    }

    fun insert(racquetModel: RacquetModel) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(racquetModel)
        }
    }

    fun update(racquetModel: RacquetModel) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.update(racquetModel)
        }
    }

    fun delete(racquetModel: RacquetModel) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.delete(racquetModel)
        }
    }

    fun calcHeadWeight(units: String?, length: String, balancePoint: String) {
        val balancePointNum = Helper.verifyEntry(balancePoint)
        val lengthNum = Helper.verifyEntry(length)
        println("Calc HW: balance point=$balancePoint")
        if (units == "Inches") {
            val midpoint = length / 2
            mHeadWeight = Helper.calcHeadWeight(mSlope, mLength, balancePoint)
            println("Slope: $mSlope Length:$mLength BP:$balancePoint")
            setDirectionSpinner2(mHeadWeight)
            headWeightEdit.setText(String.format("%.5g", Math.abs(mHeadWeight)))
        }
    }

    fun calcBalancePoint(units: String?, length: String, headWeight: String) {
        var headWeight: Double? = Helper.verifyEntry(headWeightEdit.text.toString())
        println("Calculating Balancepoint: " + headWeight!!)
        if (headWeight > 0.0) {
            headWeight = getDirectionSpinner(headWeight)
            mLength = Helper.verifyEntry(lengthEdit.text.toString())
            mBalancePoint = Helper.calcBalancePoint(mSlope, mLength, headWeight)
            balancePointEdit.setText(Math.abs(mBalancePoint).toString() + "")
        }
    }

}
