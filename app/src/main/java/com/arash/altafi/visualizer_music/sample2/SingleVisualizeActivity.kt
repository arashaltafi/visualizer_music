package com.arash.altafi.visualizer_music.sample2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arash.altafi.visualizer_music.R
import com.arash.altafi.visualizer_music.databinding.ActivitySingleVisualizeBinding

class SingleVisualizeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleVisualizeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleVisualizeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.audioVisualizeView.doPlay(R.raw.sample)

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.audioVisualizeView.release()
    }
}