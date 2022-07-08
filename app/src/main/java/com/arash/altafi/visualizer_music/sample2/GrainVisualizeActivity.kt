package com.arash.altafi.visualizer_music.sample2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arash.altafi.visualizer_music.R
import com.arash.altafi.visualizer_music.databinding.ActivityGrainVisualizeBinding

class GrainVisualizeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGrainVisualizeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGrainVisualizeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.audioVisualizeView.doPlay(R.raw.sample2)

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.audioVisualizeView.release()
    }
}