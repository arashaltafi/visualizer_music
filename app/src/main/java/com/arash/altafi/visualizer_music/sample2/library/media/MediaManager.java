package com.arash.altafi.visualizer_music.sample2.library.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.arash.altafi.visualizer_music.sample2.library.utils.LogUtils;

public class MediaManager {

    private MediaPlayer mediaPlayer;
    private Context mContext;
    private MediaManagerListener mListener;

    private MediaManager() {
    }

    public MediaManager(Context context) {
        mContext = context;
    }


    /**
     * play audio by res id
     *
     * @param raw res id of audio
     */
    public void doPlay(final int raw) {
        try {
            mediaPlayer = MediaPlayer.create(mContext, raw);
            if (mediaPlayer == null) {
                LogUtils.d("mediaPlayer is null");
                return;
            }

            mediaPlayer.setOnErrorListener(null);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    if (mListener != null) {
                        mListener.onPrepare();
                    }
                }
            });
            mediaPlayer.start();
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
        }
    }

    /**
     * play audio by file path
     *
     * @param filePath file path of audio
     */
    public void doPlay(final String filePath) {
        try {
            mediaPlayer = MediaPlayer.create(mContext, Uri.parse(filePath));
            if (mediaPlayer == null) {
                LogUtils.d("mediaPlayer is null");
                return;
            }

            mediaPlayer.setOnErrorListener(null);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    if (mListener != null) {
                        mListener.onPrepare();
                    }
                }
            });
            mediaPlayer.start();
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
        }
    }

    public int getMediaPlayerId() {
        return mediaPlayer.getAudioSessionId();
    }

    public void setMediaManagerListener(MediaManagerListener listener) {
        mListener = listener;
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
