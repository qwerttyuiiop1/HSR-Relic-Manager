package com.example.hsrrelicmanager.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.hsrrelicmanager.R

class RuleHeaderFragment : Fragment() {
    private lateinit var rulesSpinner: Spinner

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.rule_header_fragment, container, false)

        rulesSpinner = view.findViewById(R.id.rule_spinner)

        val arr = resources.getStringArray(R.array.rule_spinner_choices)
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, arr)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rulesSpinner.adapter = adapter

        val addButtonFrame: View = view.findViewById(R.id.rule_add_button_frame)
        addButtonFrame.setOnClickListener {
            blurBackground()
            showAddRuleDialog()
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        rulesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)

                (activity as MainActivity).loadFragment(null, when(selectedItem.toString()) {
                    "Ordered" -> RuleBodyFragment()
                    else -> CategorizedRuleBodyFragment()
                })
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                return
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun blurBackground() {
        requireActivity().findViewById<View>(R.id.activity_main_layout).blur()
    }


    private fun showAddRuleDialog() {
        val dialog = AddRuleDialogFragment()
        dialog.show(parentFragmentManager, "AddRuleDialog")
    }
}
