package com.thilawfabrice.inventoryapp.views.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.thilawfabrice.inventoryapp.R
import com.thilawfabrice.inventoryapp.getApp
import com.thilawfabrice.inventoryapp.viewmodels.ProductViewModel
import com.thilawfabrice.inventoryapp.viewmodels.ProductViewModel.Companion.FACTORY
import com.thilawfabrice.inventoryapp.views.ProductViewItem
import com.thilawfabrice.inventoryapp.views.adapters.AdapterItemList
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ProductListFragment : Fragment() {

    private val vmFactory: ViewModelProvider.NewInstanceFactory by lazy {
        FACTORY(
            requireActivity().getApp().repository,
            ""
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
            findNavController().navigate(R.id.action_ProductListFragment_to_AddProductFragment)
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
                        displayProductList(it.toSet(), listView, adapterView, emptyListTextView)
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
}