package com.arash.altafi.visualizer_music.sample5

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arash.altafi.visualizer_music.databinding.ActivitySample5Binding
import com.arash.altafi.visualizer_music.sample5.library.AudioPlayer
import com.google.android.exoplayer2.SimpleExoPlayer

class Sample5Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySample5Binding

    private var player: SimpleExoPlayer? = null
    private var audioPlayer: AudioPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySample5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        player = null
        binding.bar.show()
        audioPlayer = AudioPlayer(player)
        audioPlayer!!.audioDataReceiver.setAudioDataListener(binding.bar)
    }

    private fun startPlayingAudio(url: String) {
        audioPlayer!!.play(this, url)
    }

    private fun stopPlayingAudio() {
        audioPlayer?.stop()
    }

    override fun onStart() {
        super.onStart()
        startPlayingAudio(
            "https://dls.music-fa.com/tagdl/1401/Abozar%20Roohi%20-%20Salam%20Farmande%20(320).mp3",
        )
    }

    override fun onStop() {
        super.onStop()
        stopPlayingAudio()
    }

}