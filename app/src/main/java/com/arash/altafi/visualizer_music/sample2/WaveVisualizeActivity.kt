package com.arash.altafi.visualizer_music.sample2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arash.altafi.visualizer_music.R
import com.arash.altafi.visualizer_music.databinding.ActivityWaveVisualizeBinding

class WaveVisualizeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWaveVisualizeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaveVisualizeBinding.inflate(layoutInflater)
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