package com.arash.altafi.visualizer_music.sample6

import android.Manifest
import android.content.Intent
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arash.altafi.visualizer_music.PermissionUtils
import com.arash.altafi.visualizer_music.databinding.ActivitySample6Binding
import com.arash.altafi.visualizer_music.sample1.Sample1Activity
import com.arash.altafi.visualizer_music.sample6.ui.*

class Sample6Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySample6Binding

    private val registerResult = PermissionUtils.register(this,
        object : PermissionUtils.PermissionListener {
            override fun observe(permissions: Map<String, Boolean>) {
                if (permissions[Manifest.permission.RECORD_AUDIO] == true) {
                    init()
                }
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySample6Binding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!PermissionUtils.isGranted(this, Manifest.permission.RECORD_AUDIO)) {
            PermissionUtils.requestPermission(
                this, registerResult, Manifest.permission.RECORD_AUDIO
            )
        } else {
            init()
        }
    }

    private fun init() {
        binding.apply {
            vBarBtn.setOnClickListener {
                startActivity(Intent(this@Sample6Activity, BarActivity::class.java))
            }
            vBlastBtn.setOnClickListener {
                startActivity(Intent(this@Sample6Activity, BlastActivity::class.java))
            }
            vBlobBtn.setOnClickListener {
                startActivity(Intent(this@Sample6Activity, BlobActivity::class.java))
            }
            vWaveBtn.setOnClickListener {
                startActivity(Intent(this@Sample6Activity, WaveActivity::class.java))
            }
            vCircleLineBtn.setOnClickListener {
                startActivity(Intent(this@Sample6Activity, CircleLineActivity::class.java))
            }
            vHifiBtn.setOnClickListener {
                startActivity(Intent(this@Sample6Activity, HiFiActivity::class.java))
            }
        }
    }

}