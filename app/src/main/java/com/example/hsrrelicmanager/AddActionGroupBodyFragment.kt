package com.example.hsrrelicmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hsrrelicmanager.databinding.FragmentActionGroupBodyBinding
import androidx.recyclerview.widget.LinearLayoutManager

class AddActionGroupBodyFragment : Fragment() {
    lateinit var binding: FragmentActionGroupBodyBinding

    private lateinit var adapter: ActionItemAdapter
    private val actionItems = mutableListOf("")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentActionGroupBodyBinding.inflate(inflater, container, false).apply {
            binding = this
            filterSectonAdd.setOnClickListener{
                requireActivity().findViewById<View>(R.id.activity_main_layout).blur()
                val dialog = AddFilterDialog()
                dialog.show(parentFragmentManager, "AddFilterDialog")
            }
            adapter = ActionItemAdapter(actionItems)

            recyclerViewActionGroup.layoutManager = LinearLayoutManager(context)
            recyclerViewActionGroup.adapter = adapter
        }.root
    }

}
