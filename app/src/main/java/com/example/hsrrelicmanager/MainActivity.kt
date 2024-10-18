package com.example.hsrrelicmanager

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hsrrelicmanager.core.AutoclickService
import com.example.hsrrelicmanager.databinding.ActivityMainBinding
import com.example.hsrrelicmanager.databinding.NavbarBinding

open class MainActivity : AppCompatActivity() {

    //not sure if dapat protected
    protected lateinit var binding: ActivityMainBinding
    protected lateinit var navbarBinding: NavbarBinding
    private var selectedFrame: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navbarBinding = binding.navbar

        // Hide the action bar
        supportActionBar?.hide()

        setupClickListeners()
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
        }

        navbarBinding.inventoryButtonFrame.setOnClickListener {
            handleFrameClick(navbarBinding.inventoryButtonFrame, InventoryHeaderFragment(), InventoryBodyFragment())
        }
    }

    private fun loadFragment(header: Fragment, body: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.header_fragment_container, header)
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

