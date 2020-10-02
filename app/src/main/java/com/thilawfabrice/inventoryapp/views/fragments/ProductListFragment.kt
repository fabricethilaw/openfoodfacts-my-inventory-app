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

package com.thilawfabrice.inventoryapp.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.datetime.dateTimePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.integration.android.IntentIntegrator
import com.thilawfabrice.inventoryapp.R
import com.thilawfabrice.inventoryapp.getApp
import com.thilawfabrice.inventoryapp.showDialogBox
import com.thilawfabrice.inventoryapp.viewmodels.ProductViewModel
import com.thilawfabrice.inventoryapp.viewmodels.ProductViewModel.Companion.FACTORY
import com.thilawfabrice.inventoryapp.views.BarcodeScanner
import com.thilawfabrice.inventoryapp.views.adapters.AdapterProductList
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ProductListFragment : Fragment() {
    private val barcodeManager by lazy { BarcodeScanner(this) }

    private val newProductManager by lazy {
        AddingProductManager(viewLifecycleOwner, viewModel)
    }
    private val productListManager by lazy {
        ProductListManager(viewLifecycleOwner, viewModel)
    }
    private val vmFactory: ViewModelProvider.NewInstanceFactory by lazy {
        FACTORY(
            requireActivity().getApp().repository,
            this
        )
    }
    private val viewModel: ProductViewModel by viewModels { vmFactory }
    private val adapterView: AdapterProductList by lazy { AdapterProductList(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val (listView, emptyListTextView) = initializeUI(view)
        displayProductList(
            listView = listView,
            adapterView = adapterView,
            emptyListTextView = emptyListTextView
        )

    }

    private fun initializeUI(view: View): Pair<RecyclerView, TextView> {
        val listView = view.findViewById<RecyclerView>(R.id.listViewProducts)
        val emptyListTextView = view.findViewById<TextView>(R.id.emptyList)
        listView.adapter = adapterView

        view.findViewById<FloatingActionButton>(R.id.btnAddProduct)
            .setOnClickListener {
                runWithPermissions(Permission.CAMERA) {
                    barcodeManager.startScanning()
                    // Go to onActivityResult to get scan result.
                }
            }
        return Pair(listView, emptyListTextView)
    }


    private fun displayProductList(
        listView: RecyclerView,
        adapterView: AdapterProductList, emptyListTextView: TextView
    ) {
        productListManager.updateUIWithProductList(listView, adapterView, emptyListTextView)
    }


    private fun handleNewProductRecording(barcode: String) {

        val loaderAnimation = MaterialDialog(requireContext()).show {
            title(R.string.checking)
            cancelable(false)
            customView(R.layout.loader_dialog)
        }
        newProductManager.checkScannedBarcodeOnline(
            barcodeToVerify = barcode,
            onSuccess = { details ->
                // stop animation
                loaderAnimation.dismiss()
                // Provide expiry date
                letUserSetExpiryDate(details.name) { date ->
                    // Save new product or update existing one
                    // users sees updated automatically
                    newProductManager.saveProduct(details, expiryDate = date)
                }
            },

            onFailure = {
                // stop animation
                loaderAnimation.dismiss()
                tellUserThereWasAnError()
            },

            onInvalidCode = {
                // stop animation
                loaderAnimation.dismiss()
                tellUserThatProductNotFound()
            }

        )
    }


    private fun tellUserThereWasAnError() {
        showDialogBox(
            requireContext(), titleRes = R.string.checking,
            message = getString(R.string.unable_to_check_code)
        )
    }


    private fun letUserSetExpiryDate(productName: String, callback: (Calendar) -> Unit) {
        MaterialDialog(requireContext()).show {

            title(text = getString(R.string.product_expiry_date, productName))
            dateTimePicker(requireFutureDateTime = true) { _, date ->
                // Use dateTime (Calendar)
                callback(date)

            }
        }
    }

    private fun tellUserThatProductNotFound() {
        showDialogBox(
            requireContext(), titleRes = R.string.checking,
            message = getString(R.string.product_not_found)
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(requireContext(), "Scanning is cancelled", Toast.LENGTH_LONG).show()
            } else {
                val barcode: String = result.contents
                handleNewProductRecording(barcode)
            }
        } else {
            Toast.makeText(requireContext(), "No result", Toast.LENGTH_LONG)
                .show()
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}