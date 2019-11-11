package com.jaykallen.racquet3.room

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.jaykallen.racquet3.model.RacquetModel

/**
 * Created by Jay Kallen on 6/13/18.
 * This controls all the CRUD operations
 */

class RoomMgr {
    class createData(var racquetModel: RacquetModel): AsyncTask<String, String, String>() {

        override fun doInBackground(vararg p0: String?): String {
            println("Creating record in background")
            val databaseMgr = RoomInstance.instance
            databaseMgr?.daoAccess()?.create(racquetModel)
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            println("racquetModel ${racquetModel.name} added")
        }
    }

    class getAll: AsyncTask<String, String, String>() {
        var racquetsLiveData: MutableLiveData<List<RacquetModel>> = MutableLiveData()
        var racquetModel: List<RacquetModel> = ArrayList()

        override fun doInBackground(vararg p0: String?): String {
            println("Starting doInBackground")
            val roomMgr = RoomInstance.instance
            racquetModel = roomMgr?.daoAccess()?.getAll()?:ArrayList()
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            println("Retrieved = ${racquetModel.size}")
            racquetsLiveData.value = racquetModel
        }
    }

    class getById(val id: String): AsyncTask<String, String, String>() {
        var dataLive: MutableLiveData<RacquetModel> = MutableLiveData()
        var racquetModel = RacquetModel()

        override fun doInBackground(vararg p0: String?): String {
            val roomMgr = RoomInstance.instance
            racquetModel = roomMgr?.daoAccess()?.getById(id)?: RacquetModel()
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            println("Retrieved = ${racquetModel.name}")
            dataLive.value = racquetModel
        }
    }

    class update(var racquetModel: RacquetModel): AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val databaseMgr = RoomInstance.instance
            databaseMgr?.daoAccess()?.update(racquetModel)
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            println("racquetModel ${racquetModel.name} updated")
        }
    }

    class delete(var racquetModel: RacquetModel): AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val databaseMgr = RoomInstance.instance
            databaseMgr?.daoAccess()?.delete(racquetModel)
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            println("racquetModel ${racquetModel.name} deleted")
        }
    }

}