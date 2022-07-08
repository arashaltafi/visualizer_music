package com.arash.altafi.visualizer_music.sample2.library.view;

import static com.arash.altafi.visualizer_music.sample2.library.constants.VisualizeMode.HORIZONTAL_LINE_BOTTOM;
import static com.arash.altafi.visualizer_music.sample2.library.constants.VisualizeMode.HORIZONTAL_LINE_TOP;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import androidx.annotation.Nullable;

import com.arash.altafi.visualizer_music.R;

public class SingleVisualizeView extends AudioVisualizeView{

    private int mOrientation;


    public SingleVisualizeView(Context context) {
        super(context);
    }

    public SingleVisualizeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleVisualizeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void handleAttr(TypedArray typedArray) {
        mOrientation = typedArray.getInteger(R.styleable.AudioVisualizeView_visualize_orientation, HORIZONTAL_LINE_TOP);
    }

    @Override
    protected void drawChild(Canvas canvas) {
        mStrokeWidth = (mRect.width() - (mSpectrumCount - 1) * mItemMargin) / mSpectrumCount * 1.0f;
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < mSpectrumCount; i++) {
            switch (mOrientation) {
                case HORIZONTAL_LINE_TOP:
                    canvas.drawLine(mRect.width() * i / mSpectrumCount, mRect.height() / 2,mRect.width() * i / mSpectrumCount, 2 + mRect.height() / 2 - mRawAudioBytes[i], mPaint);
                    break;
                case HORIZONTAL_LINE_BOTTOM:
                    canvas.drawLine(mRect.width() * i / mSpectrumCount, mRect.height() / 2,mRect.width() * i / mSpectrumCount, 2 + mRect.height() / 2 + mRawAudioBytes[i], mPaint);
                    break;
                default:
                    break;
            }
        }
    }
}
