package com.arash.altafi.visualizer_music.sample2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arash.altafi.visualizer_music.R
import com.arash.altafi.visualizer_music.databinding.ActivityReflectVisualizeBinding

class ReflectVisualizeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReflectVisualizeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReflectVisualizeBinding.inflate(layoutInflater)
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