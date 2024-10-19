package com.example.hsrrelicmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hsrrelicmanager.databinding.FragmentActionGroupBodyBinding
import com.example.hsrrelicmanager.model.relics.RelicSet

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
                requireActivity().supportFragmentManager.setFragmentResultListener("level", viewLifecycleOwner) { _, bundle ->
                    val minLevel = bundle.getInt("minLevel")
                    val maxLevel = bundle.getInt("maxLevel")
                    val isAtMost = bundle.getBoolean("isAtMost")
                    Toast.makeText(context, "minLevel: $minLevel, maxLevel: $maxLevel, isAtMost: $isAtMost", Toast.LENGTH_SHORT).show()
                }
                requireActivity().supportFragmentManager.setFragmentResultListener("selectedSets", viewLifecycleOwner) { _, bundle ->
                    val relicSet = bundle.getParcelableArrayList<RelicSet>("selectedSets")
                    Toast.makeText(context, "relicSet: $relicSet", Toast.LENGTH_SHORT).show()
                }
            }
            adapter = ActionItemAdapter(actionItems)

            recyclerViewActionGroup.layoutManager = LinearLayoutManager(context)
            recyclerViewActionGroup.adapter = adapter
        }.root
    }

}
