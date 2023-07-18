package com.example.neolight

import android.app.Application
import com.example.neolight.data.FlashOptionsRepository
import com.example.neolight.data.local.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NeoLightApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { AppDatabase.getDatabase(this, applicationScope)}
    val repository by lazy { FlashOptionsRepository(database.optionsDao())}
}