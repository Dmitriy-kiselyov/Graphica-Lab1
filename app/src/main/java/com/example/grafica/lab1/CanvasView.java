package com.example.grafica.lab1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Sex_predator on 29.02.2016.
 */
public class CanvasView extends View {

    private static long MOUTH_ANIMATION_TIME = 5_000; //5s
    private static long PUPIL_ANIMATION_TIME = 1_000; //1s

    private long mStartTime;

    private Paint mFacePaint, mEyePaint, mTeethPaint, mBlackPaint, mStrokePaint;

    private int mWidth, mHeight;
    private Rect mLeftEye, mRightEye;
    private RectF mMouth;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mFacePaint = new Paint();
        mFacePaint.setAntiAlias(true);
        mFacePaint.setARGB(255, 255, 245, 245);

        mEyePaint = new Paint();
        mEyePaint.setAntiAlias(true);
        mEyePaint.setARGB(255, 230, 252, 255);

        mBlackPaint = new Paint();
        mBlackPaint.setAntiAlias(true);
        mBlackPaint.setColor(Color.BLACK);

        mStrokePaint = new Paint();
        mStrokePaint.setStrokeWidth(2f);
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);

        mTeethPaint = new Paint();
        mTeethPaint.setAntiAlias(true);
        mTeethPaint.setColor(Color.WHITE);

        mLeftEye = new Rect();
        mRightEye = new Rect();
        mMouth = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mStartTime = getTime();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long currentTime = getTime() - mStartTime;

        //draw face
        int centerX = mWidth / 2;
        int centerY = mHeight / 2;
        int faceRadius = Math.min(centerX, centerY) - 20;
        int marginLeft = centerX - faceRadius;

        drawFace(canvas, centerX, centerY, faceRadius);

        //draw eyes
        int eyeLine = centerY - faceRadius / 3;
        int eyeHeight = faceRadius / 5;
        int eyeWidth = faceRadius * 3 / 5;

        mLeftEye.set(marginLeft + (faceRadius - eyeWidth) / 2, eyeLine - eyeHeight / 2,
                     centerX - (faceRadius - eyeWidth) / 2, eyeLine + eyeHeight / 2);
        mRightEye.set(mLeftEye.left + faceRadius, eyeLine - eyeHeight / 2,
                      mLeftEye.right + faceRadius, eyeLine + eyeHeight / 2);

        //animate pupil
        float pupilX; //from 0 to 1
        int pupilWidth = mLeftEye.width() / 4;

        long move = currentTime / PUPIL_ANIMATION_TIME;
        pupilX = (currentTime % PUPIL_ANIMATION_TIME) / (float) PUPIL_ANIMATION_TIME;
        if (move % 2 == 1)
            pupilX = 1f - pupilX;

        drawEye(canvas, mLeftEye, pupilX, pupilWidth);
        drawEye(canvas, mRightEye, pupilX, pupilWidth);

        //draw nose
        drawNose(canvas, centerX - faceRadius / 12, centerY);
        drawNose(canvas, centerX + faceRadius / 12, centerY);

        //draw teeth
        int teethLine = centerY + faceRadius / 2;
        int toothWidth = faceRadius / 8;

        //animate mouth
        float toothHeight; //from 1 to toothWidth + 1

        move = currentTime / MOUTH_ANIMATION_TIME;
        float mouthY = (currentTime % MOUTH_ANIMATION_TIME) / (float) MOUTH_ANIMATION_TIME;
        if (move % 2 == 1)
            mouthY = 1 - mouthY;
        toothHeight = mouthY * toothWidth + 1;

        mMouth.set(centerX - 3 * toothWidth, teethLine - toothHeight,
                   centerX + 3 * toothWidth, teethLine + toothHeight);
        drawMouth(canvas, 2, 6);

        invalidate(); //animate forever
    }

    private long getTime() {
        return System.nanoTime() / 1_000_000;
    }

    private void drawMouth(Canvas canvas, int n, int m) {
        canvas.drawRect(mMouth, mTeethPaint);
        float stroke = mStrokePaint.getStrokeWidth();
        mStrokePaint.setStrokeWidth(stroke + 2);
        canvas.drawRect(mMouth, mStrokePaint);
        mStrokePaint.setStrokeWidth(stroke);

        float height = mMouth.height() / n;
        float width = mMouth.width() / m;

        for (int i = 0; i < n; i++) {
            float top = mMouth.top + height * i;
            float bottom = top + height;

            for (int j = 0; j < m; j++) {
                float left = mMouth.left + width * j;
                float right = left + width;

                canvas.drawRect(left, top, right, bottom, mStrokePaint);
            }
        }
    }

    private void drawNose(Canvas canvas, int x, int y) {
        canvas.drawCircle(x, y, 3, mBlackPaint);
    }

    private void drawEye(Canvas canvas, Rect eye, float pupilX, int pupilWidth) {
        canvas.drawRect(eye, mEyePaint);
        canvas.drawRect(eye, mStrokePaint);

        float pupilLeft = (eye.width() - pupilWidth) * pupilX;
        canvas.drawRect(eye.left + pupilLeft + 5, eye.top + 5,
                        eye.left + pupilLeft + pupilWidth - 5, eye.bottom - 5, mBlackPaint);
    }

    private void drawFace(Canvas canvas, int x, int y, int radius) {
        canvas.drawCircle(x, y, radius, mFacePaint);
        canvas.drawCircle(x, y, radius, mStrokePaint);
    }

}
