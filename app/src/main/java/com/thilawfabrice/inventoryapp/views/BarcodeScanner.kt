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

package com.thilawfabrice.inventoryapp.views

import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator

class BarcodeScanner(fragment: Fragment) {

    private val integrator by lazy {
        IntentIntegrator.forSupportFragment(fragment)
    }

    init {
        integrator.setPrompt("Scan a product reference")
        integrator.setCameraId(0) // Use a specific camera of the device
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        integrator.setOrientationLocked(false)
    }

    fun startScanning() {
        integrator.initiateScan()
    }
}

