//package com.example.hsrrelicmanager.ui
//
//import android.content.DialogInterface
//import android.graphics.RenderEffect
//import android.graphics.Shader
//import android.os.Bundle
//import android.text.SpannableString
//import android.text.Spanned
//import android.text.style.UnderlineSpan
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGrouploit
//import androidx.appcompat.app.AlertDialog
//import androidx.fragment.app.DialogFragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.hsrrelicmanager.R
//import com.example.hsrrelicmanager.core.components.FilterItem
//import com.example.hsrrelicmanager.databinding.DialogSlotFilterBinding
//import com.example.hsrrelicmanager.databinding.ItemSlotRowBinding
//import com.example.hsrrelicmanager.model.Substat
//import com.example.hsrrelicmanager.model.substatSets
//
//class SlotboxAdapter(
//    val sets: List<Slot>,
//    val selectedSubstats: MutableList<Substat>,
//): RecyclerView.Adapter<SlotboxAdapter.ViewHolder>() {
//    inner class ViewHolder(val binding: ItemSlotRowBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(set: Substat) {
//            binding.apply {
//                var level = 1
//
//                checkbox.setOnCheckedChangeListener{_, isChecked ->
//                    if (isChecked) {
//                        if (!selectedSubstats.contains(set)){
//                            selectedSubstats.add(set)
//                            sortSelectedSets()
//                        }
//                    } else {
//                        set.level = 1
//                        selectedSubstats.remove(set)
//                    }
//                }
//
//                subtractLevel.setOnClickListener {
//                    if (level > 1) {
//                        level--
//                        levelNumber.text = level.toString()
//                        set.level = level
//                    }
//                }
//
//                addLevel.setOnClickListener {
//                    level++
//                    levelNumber.text = level.toString()
//                    set.level = level
//                }
//
//                container.setOnClickListener {
//                    checkbox.isChecked = !checkbox.isChecked
//                }
//                checkbox.isChecked = selectedSubstats.contains(set)
//                levelNumber.text = sets[sets.indexOf(set)].level.toString()
//
//
//                name.text = set.name
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ItemSlotRowBinding.inflate(
//            LayoutInflater.from(parent.context)
//        )
//        return ViewHolder(binding)
//    }
//
//    override fun getItemCount() = sets.size
//
//    private fun sortSelectedSets() {
//        selectedSubstats.sortWith(compareBy { sets.indexOf(it) })
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(sets[position])
//    }
//}
//
//class AddSlotDialog(private val items: MutableList<FilterItem>): DialogFragment() {
//
//    val binding: DialogSlotFilterBinding by lazy {
//        DialogSlotFilterBinding.inflate(
//            layoutInflater, null, false
//        )
//    }
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
//        val builder = AlertDialog.Builder(requireActivity())
//        builder.setView(binding.root)
//
//        val dialog = builder.create()
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//        var index = -1
//        index = items.indexOfFirst { it.title == "Slot" }
//        val selectedSubstats = if (index != -1) items[index].Substat else mutableListOf<Substat>()
//        val selectedSlotsCopy = selectedSubstats.toMutableList()
//
//        binding.apply {
//            val adapter = SlotboxAdapter(substatSets, selectedSubstats)
//            recyclerView.adapter = adapter
//            recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//            val content = SpannableString("Deselect All")
//            content.setSpan(
//                UnderlineSpan(), 0, content.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//            btnDeselectAll.text = content
//            btnDeselectAll.setOnClickListener {
//                adapter.selectedSubstats.clear()
//                adapter.notifyDataSetChanged()
//            }
//
//            cancelActionGroupDialogButton.setOnClickListener {
//                adapter.selectedSubstats.clear()
//                if (index == -1){
//                    val addFilterDialog = AddFilterDialog(items)
//                    addFilterDialog.show(requireActivity().supportFragmentManager, "AddSetDialog")
//                }
//                else{
//                    requireActivity().supportFragmentManager.setFragmentResult(
//                        "slots",
//                        Bundle().apply {
//                            putParcelableArrayList("slots", ArrayList(selectedSlotsCopy))
//                        }
//                    )
//                }
//                dismiss()
//            }
//
//            confirmActionGroupDialogButton.setOnClickListener {
//
//                if (adapter.selectedSubstats.isEmpty() && index != -1) {
//                    items.removeAt(index)
//                }
//
//                requireActivity().supportFragmentManager.setFragmentResult(
//                    "slots",
//                    Bundle().apply {
//                        putParcelableArrayList("slots", ArrayList(adapter.selectedSubstats))
//                    }
//                )
//                dismiss()
//                index = items.indexOfFirst { it.title == "Slot" }
//            }
//
//        }
//        val activity = context as MainActivity
//        val bgView = activity.findViewById<View>(R.id.activity_main_layout)
//        bgView.setRenderEffect(
//            RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
//        )
//
//        return dialog
//    }
//
//    override fun onStart() {
//        super.onStart()
//        dialog?.window?.let { window ->
//            val layoutParams = window.attributes
//
//            val displayMetrics = requireContext().resources.displayMetrics
//            val screenWidth = displayMetrics.widthPixels
//
//            layoutParams.width = screenWidth - 200
//            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
//            window.attributes = layoutParams
//        }
//    }
//
//
//    override fun onDismiss(dialog: DialogInterface) {
//        super.onDismiss(dialog)
//        // Remove the blur effect on dismissal
//        val bgView = requireActivity().findViewById<View>(R.id.activity_main_layout)
//        bgView.setRenderEffect(null)
//    }
//}