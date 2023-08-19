package com.siddiqui.androidar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.ar.core.ArCoreApk
import com.siddiqui.androidar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        maybeEnableArButton()
    }

    private fun maybeEnableArButton() {
        ArCoreApk.getInstance().checkAvailabilityAsync(this) { availability ->
            if (availability.isSupported) {
                binding.mArButton.visibility = View.VISIBLE
                binding.mArButton.isEnabled = true
            } else { // The device is unsupported or unknown.
                binding.mArButton.visibility = View.INVISIBLE
                binding.mArButton.isEnabled = false
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }
}