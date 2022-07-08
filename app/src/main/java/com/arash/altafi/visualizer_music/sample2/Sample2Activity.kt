package com.arash.altafi.visualizer_music.sample2

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.RECORD_AUDIO
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arash.altafi.visualizer_music.databinding.ActivitySample2Binding
import com.arash.altafi.visualizer_music.PermissionUtils

class Sample2Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySample2Binding

    private val registerResult = PermissionUtils.register(this,
        object : PermissionUtils.PermissionListener {
            override fun observe(permissions: Map<String, Boolean>) {
                if (
                    permissions[RECORD_AUDIO] == true &&
                    permissions[READ_EXTERNAL_STORAGE] == true
                ) {
                    init()
                }
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySample2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!PermissionUtils.isGranted(this, RECORD_AUDIO, READ_EXTERNAL_STORAGE)) {
            PermissionUtils.requestPermission(
                this, registerResult, RECORD_AUDIO, READ_EXTERNAL_STORAGE
            )
        } else {
            init()
        }

    }

    private fun init() {
        binding.apply {
            singleEntrance.setOnClickListener {
                startActivity(Intent(this@Sample2Activity, SingleVisualizeActivity::class.java))
            }
            reflectEntrance.setOnClickListener {
                startActivity(Intent(this@Sample2Activity, ReflectVisualizeActivity::class.java))
            }
            circleEntrance.setOnClickListener {
                startActivity(Intent(this@Sample2Activity, CircleVisualizeActivity::class.java))
            }
            grainEntrance.setOnClickListener {
                startActivity(Intent(this@Sample2Activity, GrainVisualizeActivity::class.java))
            }
            netEntrance.setOnClickListener {
                startActivity(Intent(this@Sample2Activity, NetVisualizeActivity::class.java))
            }
            waveEntrance.setOnClickListener {
                startActivity(Intent(this@Sample2Activity, WaveVisualizeActivity::class.java))
            }
        }
    }

}