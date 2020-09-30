package com.thilawfabrice.inventoryapp

import android.app.Application
import com.thilawfabrice.inventoryapp.repository.LocalDatabase

class App: Application() {

    private val database by lazy {
        LocalDatabase.buildDatabase(this)
    }

    fun getAppDatabase() = database

}
