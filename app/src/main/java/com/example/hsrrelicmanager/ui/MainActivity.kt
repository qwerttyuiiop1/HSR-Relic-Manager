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
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.rules.ActionGroup
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

    var _groupData: MutableList<ActionGroup>? = null
    var _manualStatus: MutableMap<Relic, List<Relic.Status>>? = null
    var _relics: MutableList<Relic>? = null

    val cachedGroupData: MutableList<ActionGroup> get() {
        if (_groupData == null) {
            dbManager.open()
            _groupData = dbManager.listGroups()
            dbManager.close()
        }
        return _groupData!!
    }
    val cachedManualStatus: MutableMap<Relic, List<Relic.Status>> get() {
        if (_manualStatus == null) {
            dbManager.open()
            _manualStatus = dbManager.listManualStatuses()
            dbManager.close()
        }
        return _manualStatus!!
    }
    val cachedRelics: List<Relic> get() {
        if (_relics == null) {
            dbManager.open()
            _relics = dbManager.listRelics()
            dbManager.close()
        }
        return _relics!!
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
    }

    override fun onPause() {
        super.onPause()
        _relics = null
        _groupData = null
        _manualStatus = null
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
}

