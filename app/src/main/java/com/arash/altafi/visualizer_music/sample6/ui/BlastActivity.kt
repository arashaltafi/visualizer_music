package com.arash.altafi.visualizer_music.sample6.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arash.altafi.visualizer_music.R
import com.arash.altafi.visualizer_music.databinding.ActivityBlastBinding
import com.arash.altafi.visualizer_music.sample6.library.AudioPlayer

class BlastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlastBinding
    private var mAudioPlayer: AudioPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        mAudioPlayer = AudioPlayer()
    }

    private fun startPlayingAudio(resId: Int) {
        binding.apply {
            mAudioPlayer!!.play(this@BlastActivity, resId) {
                blast.hide()
            }
            val audioSessionId = mAudioPlayer!!.audioSessionId
            if (audioSessionId != -1) blast.setAudioSessionId(audioSessionId)
        }
    }

    private fun stopPlayingAudio() {
        binding.apply {
            if (mAudioPlayer != null) mAudioPlayer!!.stop()
            blast.release()
        }
    }

    override fun onStart() {
        super.onStart()
        startPlayingAudio(R.raw.sample)
    }

    override fun onStop() {
        super.onStop()
        stopPlayingAudio()
    }
}