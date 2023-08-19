package com.siddiqui.androidar

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.ArCoreApk
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.siddiqui.androidar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var yourModelRenderable: ModelRenderable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        maybeEnableArButton()

        val arFragment = supportFragmentManager.findFragmentById(R.id.ar_fragment) as ArFragment

        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)
            val modelNode = TransformableNode(arFragment.transformationSystem)
            modelNode.setParent(anchorNode)
            modelNode.renderable = yourModelRenderable
            modelNode.select()
        }

        ModelRenderable.builder()
            .setSource(this, Uri.parse("models/cube.glb")) // or "model.glb"
            .build()
            .thenAccept { renderable ->
                yourModelRenderable = renderable
            }
            .exceptionally { throwable ->
                Log.e("ARApp", "Error loading model", throwable)
                null
            }

    }

    private fun maybeEnableArButton() {
        ArCoreApk.getInstance().checkAvailabilityAsync(this) { availability ->
            if (availability.isSupported) {
                    showSnackbar(binding.mainLayout,"ARCore Support Device ")
            } else { // The device is unsupported or unknown.
                    showSnackbar(binding.mainLayout,"ARCore doesn't support this device")
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }
    private fun showSnackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(view, message, duration).show()
    }
}