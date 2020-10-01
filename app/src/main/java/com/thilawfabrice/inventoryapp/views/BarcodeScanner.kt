package com.thilawfabrice.inventoryapp.views

import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator

class BarcodeScanner(fragment: Fragment) {
    // Ask for runtime perm
    // scan barcode
    // resolve code
    // check code reference online
    // if reference is valide save product details
    // display product list
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

