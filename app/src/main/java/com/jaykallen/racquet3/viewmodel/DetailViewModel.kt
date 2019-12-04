package com.jaykallen.racquet3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jaykallen.racquet3.model.RacquetModel
import com.jaykallen.racquet3.room.RoomRepository
import com.jaykallen.racquet3.room.RoomyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val slopeMetric = 0.3175
    private val slopeInches = 0.125
    private val repository: RoomRepository
    var idLiveData = MutableLiveData<RacquetModel>()
    var statLiveData = MutableLiveData<RacquetModel>()

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

    fun calcRacquet(racquet: RacquetModel) {
        if (racquet.headWeight == 0.0) {
            val hw = calcHeadWeight(racquet.units, racquet.length, racquet.balancePoint)
            when {
                hw < 0.0 -> racquet.balance = "Head Light"
                hw == 0.0 -> racquet.balance = "Even"
                hw > 0.0 -> racquet.balance = "Head Heavy"
            }
            racquet.headWeight = Math.abs(hw)
        } else {
            val bp = calcBalancePoint(racquet.units, racquet.length, racquet.headWeight, racquet.balance == "Head Light")
            racquet.balancePoint = bp
        }
        statLiveData.value = racquet
    }

    private fun calcHeadWeight(units: String, length: Double, balancePoint: Double): Double {
        val slope = if (units == "Inches") slopeInches else slopeMetric
        val midpoint = length / 2
        val headWeight = (balancePoint - midpoint) / slope
        println("Calc HW Formula: $balancePoint - $midpoint / $slopeInches = $headWeight")
        return headWeight

    }

    private fun calcBalancePoint(units: String, length: Double, headWeight: Double, light: Boolean): Double {
        val slope = if (units == "Inches") slopeInches else slopeMetric
        var midpoint = length / 2
        if (light) midpoint = -midpoint
        val balancePoint = headWeight * slope + midpoint
        println("Calc BP Formula: $headWeight * $slope + $midpoint = $balancePoint")
        return Math.abs(balancePoint)
    }

}
