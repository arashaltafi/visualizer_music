package com.arash.altafi.visualizer_music.sample8

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.arash.altafi.visualizer_music.databinding.ActivitySample8Binding
import com.arash.altafi.visualizer_music.sample8.visualizer.FFTAudioProcessor
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.*
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation

class Sample8Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySample8Binding
    private var player: ExoPlayer? = null
    private val fftAudioProcessor = FFTAudioProcessor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySample8Binding.inflate(layoutInflater)
        setContentView(binding.root)

        init(
            "test 1",
            "https://irsv.upmusics.com/Downloads/Musics/Mohsen%20Yeganeh%20-%20Behet%20Ghol%20Midam%20(320).mp3",
            "https://upmusics.com/wp-content/uploads/2017/08/photo_2017-08-30_19-39-52.jpg"
        )
    }

    private fun init(title: String, url: String, background: String) {
        binding.apply {

            val btnPause: AppCompatImageView = videoPlayer.findViewById(R.id.exo_pause)
            val btnPlay: AppCompatImageView = videoPlayer.findViewById(R.id.exo_play)

            ivBackground.visibility = View.VISIBLE
            ivMusic.visibility = View.VISIBLE
            visualizer.visibility = View.VISIBLE

            btnPause.setOnClickListener {
                onPause()
            }

            btnPlay.setOnClickListener {
                onResume()
            }

            Picasso.get()
                .load(background)
                .transform(BlurTransformation(this@Sample8Activity, 25, 1))
                .into(ivBackground)

            Picasso.get()
                .load(background)
                .into(ivMusic)

            val renderersFactory = object : DefaultRenderersFactory(this@Sample8Activity) {
                override fun buildAudioRenderers(
                    context: Context,
                    extensionRendererMode: Int,
                    mediaCodecSelector: MediaCodecSelector,
                    enableDecoderFallback: Boolean,
                    audioSink: AudioSink,
                    eventHandler: Handler,
                    eventListener: AudioRendererEventListener,
                    out: ArrayList<Renderer>
                ) {
                    out.add(
                        MediaCodecAudioRenderer(
                            context,
                            mediaCodecSelector,
                            enableDecoderFallback,
                            eventHandler,
                            eventListener,
                            DefaultAudioSink(
                                AudioCapabilities.getCapabilities(context),
                                arrayOf(fftAudioProcessor)
                            )
                        )
                    )
                    super.buildAudioRenderers(
                        context,
                        extensionRendererMode,
                        mediaCodecSelector,
                        enableDecoderFallback,
                        audioSink,
                        eventHandler,
                        eventListener,
                        out
                    )
                }
            }

            val trackSelector = DefaultTrackSelector(this@Sample8Activity)
            player = ExoPlayer.Builder(this@Sample8Activity, renderersFactory)
                .setTrackSelector(trackSelector)
                .setSeekBackIncrementMs(5000)
                .setSeekForwardIncrementMs(10000)
                .build()

            visualizer.processor = fftAudioProcessor

            videoPlayer.player = player
            val mediaItem: MediaItem = MediaItem.Builder()
                .setUri(url)
//              .setMimeType(MimeTypes.APPLICATION_MP4) //For Videos and Mp3
//              .setMimeType(MimeTypes.APPLICATION_M3U8) //For Stream Live Videos
                .setLiveConfiguration(
                    MediaItem.LiveConfiguration.Builder()
                        .setMaxPlaybackSpeed(1.02f)
                        .build()
                )
                .build()
            player?.setMediaItem(mediaItem)
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
                        btnPause.visibility = View.VISIBLE
                        btnPlay.visibility = View.GONE
                    } else {
                        btnPause.visibility = View.GONE
                        btnPlay.visibility = View.VISIBLE
                    }
                }
                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_READY -> {
                            player?.playWhenReady = true
                        }
                        Player.STATE_BUFFERING -> {
                            videoPlayer.keepScreenOn = true
                        }
                        Player.STATE_IDLE -> {
                            finish()
                        }
                        else -> {
                            player?.playWhenReady = true
                        }
                    }
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onStart() {
        super.onStart()
        player?.play()
    }

    override fun onStop() {
        super.onStop()
        binding.videoPlayer.player?.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.videoPlayer.player?.playWhenReady
        player?.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player?.pause()
    }

}