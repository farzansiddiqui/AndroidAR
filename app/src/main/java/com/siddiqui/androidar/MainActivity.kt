package com.siddiqui.androidar

import android.app.ActivityManager
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.siddiqui.androidar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val MIN_OPENGL_VERSION = 3.0
    private lateinit var arFragment: ArFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isDeviceArSupported(this)

        arFragment =
            (supportFragmentManager.findFragmentById(R.id.sceneform_ar_scene_view) as ArFragment?)!!
        this.arFragment.setOnTapArPlaneListener { hitResult: HitResult, _: Plane?, motionEvent: MotionEvent? ->
            val anchor = hitResult.createAnchor()
            placeObjectOnScene(arFragment, anchor, Uri.parse("model.glb"))
        }
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
    private fun addModelToScene(arFragment: ArFragment, anchor: Anchor, renderable: Renderable) {
        val transformableNode = TransformableNode(arFragment.transformationSystem)
        transformableNode.renderable = renderable

        val anchorNode = AnchorNode(anchor)
        transformableNode.setParent(anchorNode)
        arFragment.arSceneView.scene.addChild(anchorNode)
        transformableNode.select()
    }
    private fun placeObjectOnScene(fragment: ArFragment, anchor: Anchor, uri: Uri) {
        ModelRenderable.builder()
            .setSource(fragment.context, uri)
            .build()
            .thenAccept { renderable: ModelRenderable? ->
                addModelToScene(
                    fragment, anchor, renderable!!
                )
            }
            .exceptionally { throwable: Throwable ->
                Toast.makeText(
                    fragment.context, "Error:" + throwable.message,
                    Toast.LENGTH_LONG
                ).show()
                null
            }
    }


    override fun onResume() {
        super.onResume()

    }
}