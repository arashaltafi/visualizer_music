package com.arash.altafi.visualizer_music.sample5.library;

import static com.google.android.exoplayer2.C.TRACK_TYPE_AUDIO;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.TeeAudioProcessor;
import com.google.android.exoplayer2.source.MediaLoadData;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;

import java.nio.ByteBuffer;

public class AudioPlayer implements MediaSourceEventListener {

    public AudioDataReceiver audioDataReceiver;
    public static AudioDataFetch audioDataFetch;
    public static int sampleRate;
    public static int channels;
    private SimpleExoPlayer player;

    public AudioPlayer(SimpleExoPlayer player) {
        this.player = player;
        audioDataReceiver = new AudioDataReceiver();
        setAudioDataFetch(audioDataReceiver);
    }

    public void play(Context context, String resId) {
        if (player != null) {
            stop();
        }

        final CustomRendererFactory rendererFactory = new CustomRendererFactory(context, new TeeAudioProcessor.AudioBufferSink() {
            int counter = 0;
            @Override
            public void flush(int sampleRateHz, int channelCount, int encoding) {
                // nothing to do here
            }

            @Override
            public void handleBuffer(ByteBuffer buffer) {
                counter++;
                if (!audioDataReceiver.isLocked()) {
                    audioDataReceiver.setLocked(true);
                    audioDataFetch.setAudioDataAsByteBuffer(buffer.duplicate(), sampleRate, channels);
                } else {
                    Log.d("main_Activity", "handleBuffer: skipped no" + counter);
                }
            }
        });
        player = new SimpleExoPlayer.Builder(context, rendererFactory).build();
        player.setVolume(0);

        //create datasource from resource
        Uri uri = Uri.parse(resId);

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "Visualizer"));
        final ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri));
        mediaSource.addEventListener(new Handler(), this);

        //load datasource into player
        player.setMediaSource(mediaSource);
        player.prepare();
        player.play();
    }


    public void stop() {
        if (player != null) {
            player.stop();
            player.setPlayWhenReady(false);
            player.seekTo(0);
            player.release();
        }
    }

    @Override
    public void onDownstreamFormatChanged(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {
        if (mediaLoadData.trackType == TRACK_TYPE_AUDIO) {
            channels = mediaLoadData.trackFormat.channelCount;
            sampleRate = mediaLoadData.trackFormat.sampleRate;
        }
    }

    public void setAudioDataFetch(AudioDataFetch audioDataFetch) {
        this.audioDataFetch = audioDataFetch;
    }


    public interface AudioDataFetch {
        void setAudioDataAsByteBuffer(ByteBuffer buffer, int sampleRate, int channelCount);
    }
}
