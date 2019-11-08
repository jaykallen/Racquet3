package com.jaykallen.racquet3

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData

/**
 * Created by Jay Kallen on 6/13/18.
 * This controls all the CRUD operations
 */

class RoomMgr {
    class CreateData(var dataModel: DataModel): AsyncTask<String, String, String>() {

        override fun doInBackground(vararg p0: String?): String {
            println("Creating record in background")
            val databaseMgr = RoomInstance.instance
            databaseMgr?.daoAccess()?.create(dataModel)
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            println("dataModel ${dataModel.name} added")
        }
    }

    class GetAll: AsyncTask<String, String, String>() {
        var dataLive: MutableLiveData<List<DataModel>> = MutableLiveData()
        var dataModel: List<DataModel> = ArrayList()

        override fun doInBackground(vararg p0: String?): String {
            println("Starting doInBackground")
            val roomMgr = RoomInstance.instance
            dataModel = roomMgr?.daoAccess()?.getAll()?:ArrayList()
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            println("DataModels retrieved = ${dataModel.size}")
            dataLive.value = dataModel
        }
    }

    class GetById(val id: String): AsyncTask<String, String, String>() {
        var dataLive: MutableLiveData<DataModel> = MutableLiveData()
        var dataModel = DataModel()

        override fun doInBackground(vararg p0: String?): String {
            val roomMgr = RoomInstance.instance
            dataModel = roomMgr?.daoAccess()?.getById(id)?:DataModel()
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            println("DataModels retrieved = ${dataModel.name}")
            dataLive.value = dataModel
        }
    }

    class UpdateData(var dataModel: DataModel): AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val databaseMgr = RoomInstance.instance
            databaseMgr?.daoAccess()?.update(dataModel)
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            println("dataModel ${dataModel.name} updated")
        }
    }

    class DeleteData(var dataModel: DataModel): AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val databaseMgr = RoomInstance.instance
            databaseMgr?.daoAccess()?.delete(dataModel)
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            println("dataModel ${dataModel.name} deleted")
        }
    }

}