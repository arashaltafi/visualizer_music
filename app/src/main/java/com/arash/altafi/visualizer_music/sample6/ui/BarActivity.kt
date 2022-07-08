package com.arash.altafi.visualizer_music.sample6.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arash.altafi.visualizer_music.R
import com.arash.altafi.visualizer_music.databinding.ActivityBarBinding
import com.arash.altafi.visualizer_music.sample6.library.AudioPlayer

class BarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBarBinding
    private var mAudioPlayer: AudioPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        mAudioPlayer = AudioPlayer()
    }

    private fun startPlayingAudio(resId: Int) {
        binding.apply {
            mAudioPlayer?.play(this@BarActivity, resId) {
                bar.hide()
            }
            val audioSessionId: Int? = mAudioPlayer?.audioSessionId
            if (audioSessionId != -1) audioSessionId?.let { bar.setAudioSessionId(it) }
        }
    }

    private fun stopPlayingAudio() {
        binding.apply {
            if (mAudioPlayer != null) mAudioPlayer?.stop()
            bar.release()
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