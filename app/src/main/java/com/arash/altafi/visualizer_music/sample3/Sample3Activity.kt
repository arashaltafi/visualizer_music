package com.arash.altafi.visualizer_music.sample3

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.arash.altafi.visualizer_music.databinding.ActivitySample3Binding
import com.arash.altafi.visualizer_music.sample3.AudioPlayer.playMusic
import com.arash.altafi.visualizer_music.PermissionUtils
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation

class Sample3Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySample3Binding
    private var player: SimpleExoPlayer? = null
    private val registerResult = PermissionUtils.register(this,
        object : PermissionUtils.PermissionListener {
            override fun observe(permissions: Map<String, Boolean>) {
                if (permissions[Manifest.permission.RECORD_AUDIO] == true) {
                    init(
                        "test 1",
                        "https://irsv.upmusics.com/Downloads/Musics/Mohsen%20Yeganeh%20-%20Behet%20Ghol%20Midam%20(320).mp3",
                        "https://upmusics.com/wp-content/uploads/2017/08/photo_2017-08-30_19-39-52.jpg"
                    )
                }
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySample3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ensurePermissionAllowed()
    }

    private fun init(title: String, url: String, background: String) {
        binding.apply {
            ivBackground.visibility = View.VISIBLE
            ivMusic.visibility = View.VISIBLE

            Picasso.get()
                .load(background)
                .transform(BlurTransformation(this@Sample3Activity, 25, 1))
                .into(ivBackground)

            Picasso.get()
                .load(background)
                .into(ivMusic)

            val trackSelector = DefaultTrackSelector(this@Sample3Activity)
            player = SimpleExoPlayer.Builder(this@Sample3Activity)
                .setTrackSelector(trackSelector)
                .setSeekBackIncrementMs(5000)
                .setSeekForwardIncrementMs(10000)
                .build()

            startPlayingAudio(url, title)

            videoPlayer.player = player
            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                this@Sample3Activity,
                Util.getUserAgent(this@Sample3Activity, title)
            )
            val mediaSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(url))
            player?.setMediaSource(mediaSource)
            player?.prepare()
            player?.playWhenReady = true
            videoPlayer.requestFocus()
            videoPlayer.setShowFastForwardButton(true)
            videoPlayer.setShowNextButton(false)
            videoPlayer.setShowPreviousButton(false)
        }
    }

    private fun ensurePermissionAllowed() {
        if (!PermissionUtils.isGranted(this, Manifest.permission.RECORD_AUDIO)) {
            PermissionUtils.requestPermission(
                this, registerResult, Manifest.permission.RECORD_AUDIO
            )
        } else {
            init(
                "test 1",
                "https://irsv.upmusics.com/Downloads/Musics/Mohsen%20Yeganeh%20-%20Behet%20Ghol%20Midam%20(320).mp3",
                "https://upmusics.com/wp-content/uploads/2017/08/photo_2017-08-30_19-39-52.jpg"
            )
        }
    }

    private fun startPlayingAudio(resId: String, title: String) {
        player?.playMusic(applicationContext, title, resId, binding.videoPlayer, object : AudioPlayer.AudioPlayerEvent {
            override fun onCompleted() {
                binding.blob.hide()
            }
        })
        val audioSessionId: Int? = player?.audioSessionId
        if (audioSessionId != -1) audioSessionId?.let { binding.blob.setAudioSessionId(it) }
    }

    private fun stopPlayingAudio() {
        if (player != null) player?.stop()
        binding.blob.release()
    }

    override fun onStop() {
        super.onStop()
        stopPlayingAudio()
    }
}