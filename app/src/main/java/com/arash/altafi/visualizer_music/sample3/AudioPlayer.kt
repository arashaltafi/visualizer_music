package com.arash.altafi.visualizer_music.sample3

import android.content.Context
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

object AudioPlayer {

    private var mMediaPlayer: SimpleExoPlayer? = null

    fun stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    fun SimpleExoPlayer.playMusic(context: Context, title: String, url: String, videoPlayer: com.google.android.exoplayer2.ui.PlayerView, audioPlayerEvent: AudioPlayerEvent?) {
        stop()

        videoPlayer.player = this
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, title)
        )
        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(url))
        this.setMediaSource(mediaSource)
        this.prepare()
        this.playWhenReady = true
        videoPlayer.requestFocus()
        videoPlayer.setShowFastForwardButton(true)
        videoPlayer.setShowNextButton(false)
        videoPlayer.setShowPreviousButton(false)

        mMediaPlayer?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_READY -> {
                        mMediaPlayer?.playWhenReady = true
                    }
                    Player.STATE_BUFFERING -> {

                    }
                    Player.STATE_ENDED -> {
                        stop()
                        audioPlayerEvent?.onCompleted()
                    }
                    else -> {
                        mMediaPlayer?.playWhenReady = true
                    }
                }
            }
        })
        mMediaPlayer?.playWhenReady = true
    }

    interface AudioPlayerEvent {
        fun onCompleted()
    }
}