package com.example.grafica.lab1;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by Sex_predator on 29.02.2016.
 */
public class MainFragment extends Fragment {

    private static long ROTATION_TIME = 3_000;

    private FrameLayout mFrameLayout;
    private CanvasView  mCanvas;

    private ObjectAnimator mRotateAnimator;
    private ObjectAnimator mColorAnimator;
    private MediaPlayer    mAudioPlayer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mAudioPlayer = MediaPlayer.create(getActivity(), R.raw.evil_laugh);
        mAudioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mFrameLayout = (FrameLayout) v.findViewById(R.id.frame_layout);

        mCanvas = (CanvasView) v.findViewById(R.id.canvas);
        mCanvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateAnimation();
            }
        });

        mRotateAnimator = ObjectAnimator.ofFloat(mCanvas, "rotation", 0, 720);
        mRotateAnimator.setDuration(ROTATION_TIME);
        mRotateAnimator.setInterpolator(new DecelerateInterpolator(1.3f));

        mColorAnimator = ObjectAnimator.ofInt(mFrameLayout, "backgroundColor",
                                              Color.BLACK, Color.RED, Color.BLACK);
        mColorAnimator.setEvaluator(new ArgbEvaluator());
        mColorAnimator.setRepeatCount(3);
        mColorAnimator.setDuration(ROTATION_TIME / 4);

        return v;
    }

    private void rotateAnimation() {
        if (!mRotateAnimator.isRunning() && !mAudioPlayer.isPlaying() &&
            !mColorAnimator.isRunning()) {
            mRotateAnimator.start();
            mColorAnimator.start();
            mAudioPlayer.seekTo(0);
            mAudioPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mAudioPlayer != null) {
            mAudioPlayer.stop();
            mAudioPlayer.release();
            mAudioPlayer = null;
        }
    }

}
