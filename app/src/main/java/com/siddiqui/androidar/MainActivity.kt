package com.siddiqui.androidar

import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.siddiqui.androidar.databinding.ActivityMainBinding
import io.github.sceneview.ar.node.ArModelNode

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val MIN_OPENGL_VERSION = 3.0

    lateinit var modelNode: ArModelNode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isDeviceArSupported(this)

        modelNode = ArModelNode().





    }
    private fun isDeviceArSupported(context : Context) : Boolean {
        val openGlVersionString = (context.getSystemService(ACTIVITY_SERVICE) as ActivityManager)
            .deviceConfigurationInfo
            .glEsVersion
        if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {

            Toast.makeText(this, "Minimum Open GL version should be 3 or later", Toast.LENGTH_LONG)

                .show()
            this.finish()
            return false
        }
        return true
    }


    override fun onResume() {
        super.onResume()

    }
}