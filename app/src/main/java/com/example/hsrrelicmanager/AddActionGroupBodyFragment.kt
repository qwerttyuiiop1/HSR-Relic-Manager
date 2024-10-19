package com.example.hsrrelicmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AddActionGroupBodyFragment : Fragment() {

    private lateinit var adapter: ActionItemAdapter
    private val actionItems = mutableListOf("")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_action_group_body, container, false)

        var recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewActionGroup)
        adapter = ActionItemAdapter(actionItems)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        return view
    }
}
