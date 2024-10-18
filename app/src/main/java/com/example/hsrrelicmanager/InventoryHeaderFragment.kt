package com.example.hsrrelicmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment

class InventoryHeaderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.inventory_header_fragment, container, false)

        val rulesSpinner: Spinner = view.findViewById(R.id.inventory_spinner)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.inventory_spinner_choices,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            rulesSpinner.adapter = adapter
        }

        return view
    }
}