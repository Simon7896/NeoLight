package com.example.neolight.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.neolight.data.local.database.FlashOption
import com.example.neolight.data.local.database.FlashOptionDao
import com.example.neolight.data.local.database.Name

class FlashOptionsRepository(private val flashOptionDao: FlashOptionDao) {

    val allOptions: LiveData<List<FlashOption>> = flashOptionDao.getAll()

    @WorkerThread
    suspend fun insert(option: FlashOption){
        flashOptionDao.insert(option)
    }

    @WorkerThread
    suspend fun delete(name: String){
        flashOptionDao.delete(Name(name))
    }
}