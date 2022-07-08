package com.arash.altafi.visualizer_music.sample6.library;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {

    private MediaPlayer mMediaPlayer;

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void play(Context c, int rid, final AudioPlayerEvent audioPlayerEvent) {
        stop();

        mMediaPlayer = MediaPlayer.create(c, rid);
        mMediaPlayer.setOnCompletionListener(mediaPlayer -> {
            stop();
            if (audioPlayerEvent != null)
                audioPlayerEvent.onCompleted();
        });

        mMediaPlayer.start();
    }

    public int getAudioSessionId() {
        if (mMediaPlayer == null)
            return -1;
        return mMediaPlayer.getAudioSessionId();
    }

    public interface AudioPlayerEvent {
        void onCompleted();
    }

}