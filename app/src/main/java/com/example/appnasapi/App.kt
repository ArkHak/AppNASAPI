package com.example.appnasapi

import android.app.Application
import com.example.appnasapi.bd.PODDatabase
import com.example.appnasapi.bd.PODRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { PODDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { PODRepository(database.podDao()) }
}