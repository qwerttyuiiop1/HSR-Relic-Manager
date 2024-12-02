package com.example.hsrrelicmanager.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.core.AutoclickService
import com.example.hsrrelicmanager.databinding.ActivityMainBinding
import com.example.hsrrelicmanager.databinding.NavbarBinding
import com.example.hsrrelicmanager.task.HSRAutoClickService
import com.example.hsrrelicmanager.ui.db.DBManager
import com.example.hsrrelicmanager.ui.inventory.InventoryBodyFragment
import com.example.hsrrelicmanager.ui.inventory.InventoryHeaderFragment
import com.example.hsrrelicmanager.ui.rules.main.RuleBodyFragment
import com.example.hsrrelicmanager.ui.rules.main.RuleHeaderFragment

open class MainActivity : AppCompatActivity() {

    //not sure if dapat protected
    protected lateinit var binding: ActivityMainBinding
    protected lateinit var navbarBinding: NavbarBinding
    private var selectedFrame: View? = null

    val cachedGroupData by lazy {
        dbManager.open()
        val groupData = dbManager.listGroups()
        dbManager.close()
        groupData
    }
    val cachedManualStatus by lazy {
        dbManager.open()
        val manualStatus = dbManager.listManualStatuses()
        dbManager.close()
        manualStatus
    }
    val cachedRelics by lazy {
        dbManager.open()
        val relics = dbManager.listRelics()
        dbManager.close()
        relics
    }
    var dbManager = DBManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navbarBinding = binding.navbar

        // Hide the action bar
        supportActionBar?.hide()

        // Color of top and bottom bar
        val window = this.window
        window.statusBarColor = Color.parseColor("#252e4a")
        window.navigationBarColor = Color.parseColor("#111624")

        // Inventory tab by default
        if (savedInstanceState == null) {
            loadFragment(InventoryHeaderFragment(), InventoryBodyFragment())

            selectedFrame = navbarBinding.inventoryButtonFrame
            navbarBinding.inventoryButtonCircle.setColorFilter(Color.WHITE)
        }

        setupClickListeners()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        // TEMP ONLY! Refactor when database has been implemented
        dbManager.open()
//        createDummyGroupData()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    override fun onDestroy() {
        dbManager.close()
        super.onDestroy()
    }

    // navigation
    private fun setupClickListeners() {
        navbarBinding.ruleButtonFrame.setOnClickListener {
            handleFrameClick(navbarBinding.ruleButtonFrame, RuleHeaderFragment(), RuleBodyFragment())
        }

        //placeholder only
        navbarBinding.playButtonFrame.setOnClickListener {
            handleFrameClick(navbarBinding.playButtonFrame)
            val i = Intent(this, HSRAutoClickService::class.java)
            i.setAction(AutoclickService.ACTION_INIT)
            startService(i)
            finish()
        }

        navbarBinding.inventoryButtonFrame.setOnClickListener {
            handleFrameClick(navbarBinding.inventoryButtonFrame, InventoryHeaderFragment(), InventoryBodyFragment())
        }
    }

     fun loadFragment(header: Fragment?, body: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        header?.let { transaction.replace(R.id.header_fragment_container, it) }
        transaction.replace(R.id.body_fragment_container, body)
        transaction.commit()
    }

    private fun handleFrameClick(selectedFrame: View) {
        if (this.selectedFrame == selectedFrame) {
            return
        } else {
            this.selectedFrame?.let { resetFrameColors() }
            this.selectedFrame = selectedFrame
        }

        val outerCircleIconId = when (selectedFrame.id) {
            R.id.rule_button_frame -> R.id.rule_button_circle
            R.id.play_button_frame -> R.id.play_button_circle
            R.id.inventory_button_frame -> R.id.inventory_button_circle
            else -> null
        }

        outerCircleIconId?.let {
            val outerCircleIcon = findViewById<ImageView>(it)
            outerCircleIcon.setColorFilter(Color.WHITE)
        }
    }

    private fun handleFrameClick(selectedFrame: View, header: Fragment, body: Fragment) {
        if (this.selectedFrame == selectedFrame) {
            return
        } else {
            loadFragment(header, body)
            handleFrameClick(selectedFrame)
        }
    }

    private fun resetFrameColors() {
        navbarBinding.ruleButtonCircle.setColorFilter(Color.parseColor("#1C243B"))
        navbarBinding.playButtonCircle.setColorFilter(Color.parseColor("#1C243B"))
        navbarBinding.inventoryButtonCircle.setColorFilter(Color.parseColor("#1C243B"))
    }

    // Only while DB has not been implemented
//    private fun createDummyGroupData() {
//        for (i in 1..3) {
//            val filterGroup =
//                ActionGroup().apply {
//                    addGroup(
//                        ActionGroup().apply {
//                            action = StatusAction(
//                                if (i % 2 == 0) Relic.Status.LOCK
//                                else Relic.Status.TRASH
//                            )
//                        }
//                    )
//                    filters[Filter.Type.RARITY] = Filter.RarityFilter(
//                        mutableSetOf(1, 2, 4)
//                    )
//                }
//            val lockActionGroup =
//                ActionGroup().apply {
//                    action = StatusAction(Relic.Status.LOCK)
//                    filters[Filter.Type.RARITY] = Filter.RarityFilter(
//                        mutableSetOf(5)
//                    )
//                }
//            val trashActionGroup =
//                ActionGroup().apply {
//                    action = StatusAction(Relic.Status.TRASH)
//                    filters[Filter.Type.SLOT] = Filter.SlotFilter(
//                        mutableSetOf(Slot.fromName("Feet")!!)
//                    )
//                }
//            val resetActionGroup =
//                ActionGroup().apply {
//                    action = StatusAction(Relic.Status.DEFAULT)
//                    filters[Filter.Type.LEVEL] = Filter.LevelFilter(
//                        atLeast = 10
//                    )
//                }
//            val enhanceActionGroup =
//                ActionGroup().apply {
//                    action = EnhanceAction(15)
//                    filters[Filter.Type.MAIN_STAT] = Filter.MainStatFilter(
//                        mutableSetOf(mainstatSets[0])
//                    )
//                }
//
//            groupData.add(filterGroup)
//            groupData.add(lockActionGroup)
//            groupData.add(trashActionGroup)
//            groupData.add(resetActionGroup)
//            groupData.add(enhanceActionGroup)
//        }
//        for (i in 0..<groupData.size) {
//            groupData.get(i).position = i
//        }
//    }
}

