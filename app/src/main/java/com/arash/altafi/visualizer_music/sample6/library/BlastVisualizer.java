package com.arash.altafi.visualizer_music.sample6.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class BlastVisualizer extends BaseVisualizer {

    private static final int BLAST_MAX_POINTS = 1000;
    private static final int BLAST_MIN_POINTS = 3;

    private Path mSpikePath;
    private int mRadius;
    private int nPoints;

    public BlastVisualizer(Context context) {
        super(context);
    }

    public BlastVisualizer(Context context,
                           @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BlastVisualizer(Context context,
                           @Nullable AttributeSet attrs,
                           int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        mRadius = -1;
        nPoints = (int) (BLAST_MAX_POINTS * mDensity);
        if (nPoints < BLAST_MIN_POINTS)
            nPoints = BLAST_MIN_POINTS;

        mSpikePath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //first time initialization
        if (mRadius == -1) {
            mRadius = getHeight() < getWidth() ? getHeight() : getWidth();
            mRadius = (int) (mRadius * 0.65 / 2);
        }

        //create the path and draw
        if (isVisualizationEnabled && mRawAudioBytes != null) {

            if (mRawAudioBytes.length == 0) {
                return;
            }

            mSpikePath.rewind();

            double angle = 0;
            for (int i = 0; i < nPoints; i++, angle += (360.0f / nPoints)) {
                int x = (int) Math.ceil(i * (mRawAudioBytes.length / nPoints));
                int t = 0;
                if (x < 1024)
                    t = ((byte) (-Math.abs(mRawAudioBytes[x]) + 128)) * (canvas.getHeight() / 4) / 128;

                float posX = (float) (getWidth() / 2
                        + (mRadius + t)
                        * Math.cos(Math.toRadians(angle)));

                float posY = (float) (getHeight() / 2
                        + (mRadius + t)
                        * Math.sin(Math.toRadians(angle)));

                if (i == 0)
                    mSpikePath.moveTo(posX, posY);
                else
                    mSpikePath.lineTo(posX, posY);

            }
            mSpikePath.close();

            canvas.drawPath(mSpikePath, mPaint);

        }

        super.onDraw(canvas);
    }
}