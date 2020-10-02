package com.thilawfabrice.inventoryapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thilawfabrice.inventoryapp.R


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddProductFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_add_product, container, false)
        return view
    }

}