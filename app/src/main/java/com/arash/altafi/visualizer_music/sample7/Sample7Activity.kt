package com.arash.altafi.visualizer_music.sample7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.arash.altafi.visualizer_music.databinding.ActivitySample7Binding
import com.arash.altafi.visualizer_music.sample3.AudioPlayer
import com.arash.altafi.visualizer_music.sample3.AudioPlayer.playMusic
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation

class Sample7Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySample7Binding
    private var player: SimpleExoPlayer? = null
    private var player1: SimpleExoPlayer? = null
    private var audioPlayer: com.arash.altafi.visualizer_music.sample5.library.AudioPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySample7Binding.inflate(layoutInflater)
        setContentView(binding.root)

        init(
            "test 1",
            "https://irsv.upmusics.com/Downloads/Musics/Mohsen%20Yeganeh%20-%20Behet%20Ghol%20Midam%20(320).mp3",
            "https://upmusics.com/wp-content/uploads/2017/08/photo_2017-08-30_19-39-52.jpg"
        )
    }

    private fun init(title: String, url: String, background: String) {
        binding.apply {
            ivBackground.visibility = View.VISIBLE
            ivMusic.visibility = View.VISIBLE
            bar.visibility = View.VISIBLE

            Picasso.get()
                .load(background)
                .transform(BlurTransformation(this@Sample7Activity, 25, 1))
                .into(ivBackground)

            Picasso.get()
                .load(background)
                .into(ivMusic)

            val trackSelector = DefaultTrackSelector(this@Sample7Activity)

            player = SimpleExoPlayer.Builder(this@Sample7Activity)
                .setTrackSelector(trackSelector)
                .setSeekBackIncrementMs(5000)
                .setSeekForwardIncrementMs(10000)
                .build()

            SimpleExoPlayer.Builder(this@Sample7Activity)
                .setTrackSelector(trackSelector)
                .setSeekBackIncrementMs(5000)
                .setSeekForwardIncrementMs(10000)
                .build()

            audioPlayer = com.arash.altafi.visualizer_music.sample5.library.AudioPlayer(
                player1
            )
            bar.show()
            audioPlayer?.audioDataReceiver?.setAudioDataListener(bar)
            audioPlayer?.play(this@Sample7Activity, url)

            videoPlayer.player = player
            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                this@Sample7Activity,
                Util.getUserAgent(this@Sample7Activity, title)
            )
            val mediaSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(url))
            val mediaItem: MediaItem = MediaItem.Builder()
                .setUri(url)
                .setLiveConfiguration(
                    MediaItem.LiveConfiguration.Builder()
                        .setMaxPlaybackSpeed(1.02f)
                        .build()
                )
                .build()
            player?.setMediaItem(mediaItem)
            player?.setMediaSource(mediaSource)
            player?.prepare()
            player?.playWhenReady = true
            videoPlayer.requestFocus()
            videoPlayer.setShowFastForwardButton(true)
            videoPlayer.setShowNextButton(false)
            videoPlayer.setShowPreviousButton(false)
            player?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    if (isPlaying) {
                        bar.show()
                    } else {
                        bar.hide()
                    }
                }
                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_READY -> {
                            player?.playWhenReady = true
                            bar.show()
                        }
                        Player.STATE_BUFFERING -> {
                            videoPlayer.keepScreenOn = true
                            bar.hide()
                        }
                        Player.STATE_IDLE -> {
                            finish()
                            bar.hide()
                        }
                        else -> {
                            player?.playWhenReady = true
                            bar.show()
                        }
                    }
                }
            })
        }
    }

    private fun stopPlayingAudio() {
        if (player != null) player?.stop()
        binding.bar.hide()
        audioPlayer?.stop()
    }

    override fun onStop() {
        super.onStop()
        stopPlayingAudio()
    }

}