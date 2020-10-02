package com.thilawfabrice.inventoryapp.views.fragments

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.thilawfabrice.inventoryapp.viewmodels.ProductViewModel
import com.thilawfabrice.inventoryapp.views.ProductViewItem
import com.thilawfabrice.inventoryapp.views.adapters.AdapterProductList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListManager(
    private val viewLifecycleOwner: LifecycleOwner,
    private val viewModel: ProductViewModel,
) {

    /**
     *
     */
    fun updateUIWithProductList(
        listView: RecyclerView,
        adapterView: AdapterProductList,
        emptyListTextView: TextView
    ) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main)
        {

            observeList {
                // Update UI on main thread
                Handler(Looper.getMainLooper()).post {
                    if (it.isEmpty()) {
                        listView.visibility = View.GONE
                        emptyListTextView.visibility = View.VISIBLE
                    } else {
                        listView.visibility = View.VISIBLE
                        emptyListTextView.visibility = View.GONE
                    }

                    adapterView.update(it.toSet())
                }
            }
        }
    }

    private suspend fun observeList(

        callback: (List<ProductViewItem>) -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.getProductList().run {

                if (this.hasActiveObservers().not()) {
                    observeForever { items ->
                        callback(items)
                    }
                }
            }
        }
    }


}
