/*
 * Copyright (C) 2020 Fabrice Thilaw (@thilawfab)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * */

package com.thilawfabrice.inventoryapp

import android.app.Activity
import android.app.Application
import com.thilawfabrice.inventoryapp.repository.LocalDatabase
import com.thilawfabrice.inventoryapp.repository.Repository
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat
import java.util.*

class App : Application() {

    val repository by lazy { Repository(database) }

    private val database by lazy {
        LocalDatabase.buildDatabase(this)
    }


}

fun Activity.getApp(): App = this.applicationContext as App

fun Calendar.formatDate(): String {
    val fm = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
    return fm.format(this.time)
}

fun String.formatToDate(): DateTime? {
    val fm = DateTimeFormat.forPattern("MMMM dd, yyyy")
    return fm.parseDateTime(this)
}

fun DateTime.takeOldest(otherDateTime: DateTime): DateTime {
    return if (isBefore(otherDateTime.toInstant())) {
        this
    } else otherDateTime
}

fun DateTime.formatDate() = toCalendar(Locale.US).formatDate()

