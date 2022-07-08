package com.arash.altafi.visualizer_music.sample4

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.arash.altafi.visualizer_music.databinding.ActivitySample4Binding
import com.arash.altafi.visualizer_music.sample4.library.FFTAudioProcessor
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.*
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class Sample4Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySample4Binding
    private var player: ExoPlayer? = null
    private val fftAudioProcessor = FFTAudioProcessor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySample4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        val context = this.applicationContext

        val renderersFactory = object : DefaultRenderersFactory(context) {

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
        player = SimpleExoPlayer.Builder(context, renderersFactory)
            .build()

        binding.visualizer.processor = fftAudioProcessor

        val uri = Uri.parse("https://irsv.upmusics.com/Downloads/Musics/Mohsen%20Yeganeh%20-%20Behet%20Ghol%20Midam%20(320).mp3")
        val mediaSource = ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(context, "ExoVisualizer")
        ).createMediaSource(MediaItem.Builder().setUri(uri).build())
        player?.playWhenReady = true
        player?.setMediaSource(mediaSource)
        player?.prepare()
    }

    override fun onResume() {
        super.onResume()
        player?.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        player?.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        player?.release()
    }

}