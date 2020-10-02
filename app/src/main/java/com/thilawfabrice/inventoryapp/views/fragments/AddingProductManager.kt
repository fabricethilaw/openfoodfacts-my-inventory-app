package com.thilawfabrice.inventoryapp.views.fragments

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.thilawfabrice.inventoryapp.formatDate
import com.thilawfabrice.inventoryapp.repository.api.FoodDetails
import com.thilawfabrice.inventoryapp.viewmodels.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AddingProductManager(
    private val viewLifecycleOwner: LifecycleOwner,
    private val viewModel: ProductViewModel
) {
    /**
     *
     */
    fun checkScannedBarcodeOnline(
        barcodeToVerify: String,
        onSuccess: (FoodDetails) -> Unit, onInvalidCode: () -> Unit, onFailure: (Exception) -> Unit
    ) {

        // Non blocking task on main thread
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            try {
                viewModel.checkReferenceOnline(barcodeToVerify)
                    .observeForever { details ->
                        if (details != null) onSuccess(details) else onInvalidCode()
                    }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }


    fun saveProduct(productDetails: FoodDetails, expiryDate: Calendar) {
        viewModel.saveProduct(productDetails, expiryDate.formatDate())
    }
}