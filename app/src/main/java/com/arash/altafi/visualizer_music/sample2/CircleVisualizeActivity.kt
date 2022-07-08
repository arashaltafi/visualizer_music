package com.arash.altafi.visualizer_music.sample2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arash.altafi.visualizer_music.R
import com.arash.altafi.visualizer_music.databinding.ActivityCircleVisualizeBinding

class CircleVisualizeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCircleVisualizeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCircleVisualizeBinding.inflate(layoutInflater)
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