package com.arash.altafi.visualizer_music.sample1

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.LinearInterpolator
import com.arash.altafi.visualizer_music.R
import com.arash.altafi.visualizer_music.databinding.ActivitySample1Binding
import com.arash.altafi.visualizer_music.sample1.library.NierVisualizerManager
import com.arash.altafi.visualizer_music.sample1.library.converter.AbsAudioDataConverter
import com.arash.altafi.visualizer_music.sample1.library.converter.AudioDataConverterFactory
import com.arash.altafi.visualizer_music.sample1.library.renderer.IRenderer
import com.arash.altafi.visualizer_music.sample1.library.renderer.circle.CircleBarRenderer
import com.arash.altafi.visualizer_music.sample1.library.renderer.circle.CircleRenderer
import com.arash.altafi.visualizer_music.sample1.library.renderer.circle.CircleSolidRenderer
import com.arash.altafi.visualizer_music.sample1.library.renderer.circle.CircleWaveRenderer
import com.arash.altafi.visualizer_music.sample1.library.renderer.columnar.ColumnarType1Renderer
import com.arash.altafi.visualizer_music.sample1.library.renderer.columnar.ColumnarType2Renderer
import com.arash.altafi.visualizer_music.sample1.library.renderer.columnar.ColumnarType3Renderer
import com.arash.altafi.visualizer_music.sample1.library.renderer.columnar.ColumnarType4Renderer
import com.arash.altafi.visualizer_music.sample1.library.renderer.line.LineRenderer
import com.arash.altafi.visualizer_music.sample1.library.renderer.other.ArcStaticRenderer
import com.arash.altafi.visualizer_music.sample1.library.util.NierAnimator
import com.arash.altafi.visualizer_music.PermissionUtils

@SuppressLint("MissingPermission")
class Sample1Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySample1Binding

    companion object {
        const val STATE_PLAYING = 0
        const val STATE_PAUSE = 1
        const val STATE_STOP = 2
        const val STATUS_UNKNOWN = 0
        const val STATUS_AUDIO_RECORD = 1
        const val STATUS_MEDIA_PLAYER = 2
        const val SAMPLING_RATE = 44100
        const val AUDIO_RECORD_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
        const val AUDIO_RECORD_FORMAT =  AudioFormat.ENCODING_PCM_16BIT
    }

    private var mVisualizerManager: NierVisualizerManager? = null
    private val mPlayer by lazy {
        MediaPlayer().apply {
            resources.openRawResourceFd(R.raw.sample).apply {
                setDataSource(fileDescriptor, startOffset, length)
            }
        }
    }
    private val mAudioBufferSize by lazy {
        AudioRecord.getMinBufferSize(
            SAMPLING_RATE,
            AUDIO_RECORD_CHANNEL_CONFIG,
            AUDIO_RECORD_FORMAT
        )
    }

    private var mAudioRecord :AudioRecord?= null

    private val registerResult = PermissionUtils.register(this,
        object : PermissionUtils.PermissionListener {
            override fun observe(permissions: Map<String, Boolean>) {
                if (permissions[Manifest.permission.RECORD_AUDIO] == true) {
                    mAudioRecord = AudioRecord(
                        MediaRecorder.AudioSource.MIC,
                        SAMPLING_RATE,
                        AUDIO_RECORD_CHANNEL_CONFIG,
                        AUDIO_RECORD_FORMAT,
                        mAudioBufferSize
                    )
                }
            }
        })
    private val mRenderers = arrayOf<Array<IRenderer>>(
        arrayOf(ColumnarType1Renderer()),
        arrayOf(ColumnarType2Renderer()),
        arrayOf(ColumnarType3Renderer()),
        arrayOf(ColumnarType4Renderer()),
        arrayOf(LineRenderer(true)),
        arrayOf(CircleBarRenderer()),
        arrayOf(CircleRenderer(true)),
        arrayOf(
            CircleRenderer(true),
            CircleBarRenderer(),
            ColumnarType4Renderer()
        ),
        arrayOf(CircleRenderer(true), CircleBarRenderer(), LineRenderer(true)),
        arrayOf(
            ArcStaticRenderer(
                paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.parseColor("#cfa9d0fd")
                }),
            ArcStaticRenderer(
                paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.parseColor("#dad2eafe")
                },
                amplificationOuter = .83f,
                startAngle = -90f,
                sweepAngle = 225f
            ),
            ArcStaticRenderer(
                paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.parseColor("#7fa9d0fd")
                },
                amplificationOuter = .93f,
                amplificationInner = 0.8f,
                startAngle = -45f,
                sweepAngle = 135f
            ),
            CircleSolidRenderer(
                paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.parseColor("#d2eafe")
                },
                amplification = .45f
            ),
            CircleBarRenderer(
                paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    strokeWidth = 4f
                    color = Color.parseColor("#efe3f2ff")
                },
                modulationStrength = 1f,
                type = CircleBarRenderer.Type.TYPE_A_AND_TYPE_B,
                amplification = 1f, divisions = 8
            ),
            CircleBarRenderer(
                paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    strokeWidth = 5f
                    color = Color.parseColor("#e3f2ff")
                },
                modulationStrength = 0.1f,
                amplification = 1.2f,
                divisions = 8
            ),
            CircleWaveRenderer(
                paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    strokeWidth = 6f
                    color = Color.WHITE
                },
                modulationStrength = 0.2f,
                type = CircleWaveRenderer.Type.TYPE_B,
                amplification = 1f,
                animator = NierAnimator(
                    interpolator = LinearInterpolator(),
                    duration = 20000,
                    values = floatArrayOf(0f, -360f)
                )
            ),
            CircleWaveRenderer(
                paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    strokeWidth = 6f
                    color = Color.parseColor("#7fcee7fe")
                },
                modulationStrength = 0.2f,
                type = CircleWaveRenderer.Type.TYPE_B,
                amplification = 1f,
                divisions = 8,
                animator = NierAnimator(
                    interpolator = LinearInterpolator(),
                    duration = 20000,
                    values = floatArrayOf(0f, -360f)
                )
            )
        )
    )
    private var mCurrentStyleIndex = 0
    private var mMediaPlayerState = STATE_STOP
    private var mAudioRecordState = STATE_STOP
    private var mStatus = STATUS_UNKNOWN

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySample1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!PermissionUtils.isGranted(this, Manifest.permission.RECORD_AUDIO)) {
            PermissionUtils.requestPermission(
                this, registerResult, Manifest.permission.RECORD_AUDIO
            )
        } else {
            mAudioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLING_RATE,
                AUDIO_RECORD_CHANNEL_CONFIG,
                AUDIO_RECORD_FORMAT,
                mAudioBufferSize
            )
        }

        supportActionBar?.hide()
        binding.svWave.setZOrderOnTop(true)
        binding.svWave.holder.setFormat(PixelFormat.TRANSLUCENT)
        binding.tvChangeStyle.setOnClickListener {
            changeStyle()
        }
        binding.tvMediaPlayerStartOrStop.setOnClickListener {
            mPlayer.apply {
                when (mMediaPlayerState) {
                    STATE_PLAYING -> {
                        stop()
                        mMediaPlayerState = STATE_STOP
                        mVisualizerManager?.stop()
                        binding.tvMediaPlayerPauseOrResume.isEnabled = false
                        binding.tvMediaPlayerStartOrStop.text = "START"
                    }
                    STATE_STOP -> {
                        prepare()
                        start()
                        mMediaPlayerState = STATE_PLAYING
                        if (mStatus == STATUS_AUDIO_RECORD || mStatus == STATUS_UNKNOWN) {
                            mAudioRecord?.stop()
                            binding.tvAudioRecordStartOrStop.text = "START"
                            mAudioRecordState = STATE_STOP
                            mStatus = STATUS_MEDIA_PLAYER
                            createNewVisualizerManager()
                        }
                        useStyle(mCurrentStyleIndex)
                        binding.tvMediaPlayerPauseOrResume.isEnabled = true
                        binding.tvMediaPlayerStartOrStop.text = "STOP"
                    }
                    STATE_PAUSE -> {
                        stop()
                        prepare()
                        start()
                        mMediaPlayerState = STATE_PLAYING
                        useStyle(mCurrentStyleIndex)
                        binding.tvMediaPlayerPauseOrResume.isEnabled = true
                        binding.tvMediaPlayerStartOrStop.text = "STOP"
                    }
                }
                binding.tvMediaPlayerPauseOrResume.text = "PAUSE"
            }
            mStatus = STATUS_MEDIA_PLAYER
        }
        binding.tvMediaPlayerPauseOrResume.setOnClickListener {
            mPlayer.apply {
                when (mMediaPlayerState) {
                    STATE_PLAYING -> {
                        pause()
                        mMediaPlayerState = STATE_PAUSE
                        mVisualizerManager?.pause()
                        binding.tvMediaPlayerPauseOrResume.text = "RESUME"
                    }
                    STATE_PAUSE -> {
                        start()
                        mMediaPlayerState = STATE_PLAYING
                        mVisualizerManager?.resume()
                        binding.tvMediaPlayerPauseOrResume.text = "PAUSE"
                    }
                }
            }
        }
        binding.tvAudioRecordStartOrStop.setOnClickListener {
            mAudioRecord?.apply {
                when (mAudioRecordState) {
                    STATE_PLAYING -> {
                        stop()
                        mAudioRecordState = STATE_STOP
                        mVisualizerManager?.stop()
                        binding.tvAudioRecordStartOrStop.text = "START"
                    }
                    STATE_STOP -> {
                        startRecording()
                        mAudioRecordState = STATE_PLAYING
                        if (mStatus == STATUS_MEDIA_PLAYER || mStatus == STATUS_UNKNOWN) {
                            mPlayer.stop()
                            binding.tvMediaPlayerStartOrStop.text = "START"
                            binding.tvMediaPlayerPauseOrResume.isEnabled = false
                            mMediaPlayerState = STATE_STOP
                            mStatus = STATUS_AUDIO_RECORD
                            createNewVisualizerManager()
                        }
                        useStyle(mCurrentStyleIndex)
                        binding.tvAudioRecordStartOrStop.text = "STOP"
                    }
                }
            }
            mStatus = STATUS_AUDIO_RECORD
        }
    }

    private fun changeStyle() {
        useStyle(++mCurrentStyleIndex)
    }

    private fun useStyle(idx: Int) {
        mVisualizerManager?.start(binding.svWave, mRenderers[idx % mRenderers.size])
    }

    private fun createNewVisualizerManager() {
        mVisualizerManager?.release()
        mVisualizerManager = NierVisualizerManager().apply {
            when (mStatus) {
                STATUS_MEDIA_PLAYER -> {
                    init(mPlayer.audioSessionId)
                }
                STATUS_AUDIO_RECORD -> {
                    init(object : NierVisualizerManager.NVDataSource {

                        private val mBuffer: ByteArray = ByteArray(512)
                        private val mAudioDataConverter: AbsAudioDataConverter? =
                            mAudioRecord?.let {
                                AudioDataConverterFactory.getConverterByAudioRecord(
                                    it
                                )
                            }

                        override fun getDataSamplingInterval() = 0L

                        override fun getDataLength() = mBuffer.size

                        override fun fetchFftData(): ByteArray? {
                            return null
                        }

                        override fun fetchWaveData(): ByteArray {
                            mAudioDataConverter?.convertWaveDataTo(mBuffer)
                            return mBuffer
                        }

                    })
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mVisualizerManager?.apply {
            when (mStatus) {
                STATUS_MEDIA_PLAYER -> {
                    if (mPlayer.isPlaying) {
                        resume()
                    }
                }
                STATUS_AUDIO_RECORD -> {
                    if (mAudioRecord?.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                        resume()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mVisualizerManager?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mVisualizerManager?.release()
        mVisualizerManager = null
        mPlayer.release()
        mAudioRecord?.release()
    }

}