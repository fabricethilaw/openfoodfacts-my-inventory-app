package com.thilawfabrice.inventoryapp.views.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import com.thilawfabrice.inventoryapp.repository.api.FoodDetails
import com.thilawfabrice.inventoryapp.viewmodels.ProductViewModel
import com.thilawfabrice.inventoryapp.viewmodels.ProductViewModel.Companion.FACTORY
import com.thilawfabrice.inventoryapp.views.BarcodeScanner
import com.thilawfabrice.inventoryapp.views.ProductViewItem
import com.thilawfabrice.inventoryapp.views.adapters.AdapterItemList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ProductListFragment : Fragment() {
    private val barcodeManager by lazy { BarcodeScanner(this) }


    private val vmFactory: ViewModelProvider.NewInstanceFactory by lazy {
        FACTORY(
            requireActivity().getApp().repository,
            "" // replace this value to provide the model with any relevant data you wish at initialization step
        )
    }
    private val viewModel: ProductViewModel by viewModels { vmFactory }
    private val adapterView: AdapterItemList by lazy { AdapterItemList(requireContext()) }

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

        observeAvailableProductList(listView, emptyListTextView)

    }

    private fun initializeUI(view: View): Pair<RecyclerView, TextView> {
        val listView = view.findViewById<RecyclerView>(R.id.listViewProducts)
        val emptyListTextView = view.findViewById<TextView>(R.id.emptyList)
        listView.adapter = adapterView

        view.findViewById<FloatingActionButton>(R.id.btnAddProduct).setOnClickListener {
            showScanner()
        }
        return Pair(listView, emptyListTextView)
    }


    private fun observeAvailableProductList(
        listView: RecyclerView,
        emptyListTextView: TextView
    ) {
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.getProductList().run {

                if (this.hasActiveObservers().not()) {
                    observeForever {
                        displayProductList(
                            it.toSet(),
                            listView,
                            adapterView,
                            emptyListTextView
                        )
                    }
                }
            }
        }
    }

    private fun displayProductList(
        data: Set<ProductViewItem>,
        listView: RecyclerView,
        adapterView: AdapterItemList,
        emptyListTextView: TextView
    ) {
        // Update UI on main thread
        Handler(Looper.getMainLooper()).post {
            if (data.isEmpty()) {
                listView.visibility = GONE
                emptyListTextView.visibility = VISIBLE
            } else {
                listView.visibility = VISIBLE
                emptyListTextView.visibility = GONE
            }

            adapterView.update(data)
        }
    }

    private fun showScanner() {
        runWithPermissions(Permission.CAMERA) {
            barcodeManager.startScanning()
            // Go to onActivityResult to get scan result.
        }
    }

    private fun handleScanResult(barcode: String) {
        val loaderAnimation = MaterialDialog(requireContext()).show {
            title(R.string.food_check)
            cancelable(false)
            customView(R.layout.loader_dialog)
        }

        checkScannedBarcodeOnline(barcode) { scanResult ->
            // hide loader
            Handler(Looper.getMainLooper()).post {
                loaderAnimation.dismiss()
            }

            if (scanResult != null) {
                letUserSetExpiryDate()
            } else {
                tellUserThatProductNotFound()
            }
        }
    }


    private fun checkScannedBarcodeOnline(barcode: String, resultObserver: (FoodDetails?) -> Unit) {

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            try {
                viewModel.checkReferenceOnline(barcode).observeForever(resultObserver)
            } catch (e: Exception) {

                MaterialDialog(requireContext()).show {
                    title(R.string.food_check)
                    message(R.string.unable_to_check_code)
                    positiveButton(R.string.ok) {
                        it.dismiss()
                    }
                }
            }
        }
    }

    private fun letUserSetExpiryDate() {

        MaterialDialog(requireContext()).show {
            title(R.string.product_expiry_date)
            dateTimePicker(requireFutureDateTime = true) { _, dateTime ->
                // Use dateTime (Calendar)

            }
        }

    }

    private fun tellUserThatProductNotFound() {
        MaterialDialog(requireContext()).show {
            title(R.string.food_check)
            message(R.string.product_not_found)
            positiveButton(R.string.ok) {
                it.dismiss()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(requireContext(), "Scanning is cancelled", Toast.LENGTH_LONG).show()
            } else {
                val barcode: String = result.contents
                handleScanResult(barcode)

            }
        } else {
            Toast.makeText(requireContext(), "No result", Toast.LENGTH_LONG)
                .show()
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}