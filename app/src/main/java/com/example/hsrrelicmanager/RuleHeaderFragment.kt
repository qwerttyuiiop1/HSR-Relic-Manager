package com.example.hsrrelicmanager

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

class RuleHeaderFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.rule_header_fragment, container, false)

        val rulesSpinner: Spinner = view.findViewById(R.id.rule_spinner)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.rule_spinner_choices,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            rulesSpinner.adapter = adapter
        }

        val addButtonFrame: View = view.findViewById(R.id.rule_add_button_frame)
        addButtonFrame.setOnClickListener {
            blurBackground()
            showAddRuleDialog()
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun blurBackground() {
        val bgView = requireActivity().findViewById<View>(R.id.activity_main_layout)

        val blurEffect = RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
        bgView.setRenderEffect(blurEffect)
    }


    private fun showAddRuleDialog() {
        val dialog = AddRuleDialogFragment()
        dialog.show(parentFragmentManager, "AddRuleDialog")
    }
}
