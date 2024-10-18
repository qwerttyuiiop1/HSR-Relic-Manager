package com.example.hsrrelicmanager

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.hsrrelicmanager.databinding.ActivityMainBinding
import com.example.hsrrelicmanager.databinding.NavbarBinding

open class BaseActivity : AppCompatActivity() {

    //not sure if dapat protected
    protected lateinit var binding: ActivityMainBinding
    protected lateinit var navbarBinding: NavbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navbarBinding = binding.navbar

        // Hide the action bar
        supportActionBar?.hide()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        navbarBinding.ruleButtonFrame.setOnClickListener {
            handleFrameClick(navbarBinding.ruleButtonFrame)
            navigateTo(RuleFragment::class.java)
        }

        navbarBinding.playButtonFrame.setOnClickListener {
            handleFrameClick(navbarBinding.playButtonFrame)
            navigateTo(PlayFragment::class.java)
        }

        navbarBinding.inventoryButtonFrame.setOnClickListener {
            handleFrameClick(navbarBinding.inventoryButtonFrame)
        }
    }

    private fun handleFrameClick(selectedFrame: View) {
        resetFrameColors() // resets other icons

        //adjusts icon to white
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

    private fun resetFrameColors() {
        navbarBinding.ruleButtonCircle.setColorFilter(Color.parseColor("#1C243B"))
        navbarBinding.playButtonCircle.setColorFilter(Color.parseColor("#1C243B"))
        navbarBinding.inventoryButtonCircle.setColorFilter(Color.parseColor("#1C243B"))
    }

    private fun navigateTo(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }
}
