package com.arash.altafi.visualizer_music.sample6.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arash.altafi.visualizer_music.R
import com.arash.altafi.visualizer_music.databinding.ActivityCircleLineBinding
import com.arash.altafi.visualizer_music.sample6.library.AudioPlayer

class CircleLineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCircleLineBinding
    private var mAudioPlayer: AudioPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCircleLineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        mAudioPlayer = AudioPlayer()
    }

    override fun onStart() {
        super.onStart()
        startPlayingAudio(R.raw.sample5)
    }

    override fun onStop() {
        super.onStop()
        stopPlayingAudio()
    }

    private fun startPlayingAudio(resId: Int) {
        binding.apply {
            mAudioPlayer?.play(this@CircleLineActivity, resId) {
                circle.hide()
            }
            val audioSessionId: Int? = mAudioPlayer?.audioSessionId
            if (audioSessionId != -1) audioSessionId?.let { circle.setAudioSessionId(it) }
        }
    }

    private fun stopPlayingAudio() {
        binding.apply {
            if (mAudioPlayer != null) mAudioPlayer?.stop()
            circle.release()
        }
    }

}