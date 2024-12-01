package com.example.hsrrelicmanager.ui.rules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.ui.MainActivity

class AddActionGroupHeaderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_action_group_header, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton: ImageView = view.findViewById(R.id.action_group_backarrow)
        backButton.setOnClickListener {
            (requireActivity() as MainActivity).loadFragment(RuleHeaderFragment(), RuleBodyFragment())
        }
    }
}


