package com.thilawfabrice.inventoryapp

import android.app.Activity
import android.app.Application
import com.thilawfabrice.inventoryapp.repository.LocalDatabase
import com.thilawfabrice.inventoryapp.repository.Repository

class App : Application() {

    val repository by lazy { Repository(database) }

    private val database by lazy {
        LocalDatabase.buildDatabase(this)
    }


}

fun Activity.getApp(): App = this.applicationContext as App
