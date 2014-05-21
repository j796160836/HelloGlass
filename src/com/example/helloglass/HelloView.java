/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.helloglass;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * View used to draw a running timer.
 */
public class HelloView extends FrameLayout {

    /**
     * Interface to listen for changes on the view layout.
     */
    public interface ChangeListener {
        /** Notified of a change in the view. */
        public void onChange();
    }

    // Visible for testing.
    static final long DELAY_MILLIS = 1000;

    private final Handler mHandler = new Handler();
    private final Runnable mUpdateTextRunnable = new Runnable() {

        @Override
        public void run() {
            if (mRunning) {
                postDelayed(mUpdateTextRunnable, DELAY_MILLIS);
                updateText();
            }
        }
    };

//    private final Timer mTimer;
//    private final Timer.TimerListener mTimerListener = new Timer.TimerListener() {
//
//        @Override
//        public void onStart() {
//            mRunning = true;
//            long delayMillis = Math.abs(mTimer.getRemainingTimeMillis()) % DELAY_MILLIS;
//            if (delayMillis == 0) {
//                delayMillis = DELAY_MILLIS;
//            }
//            postDelayed(mUpdateTextRunnable, delayMillis);
//        }
//
//        @Override
//        public void onPause() {
//            mRunning = false;
//            removeCallbacks(mUpdateTextRunnable);
//        }
//
//        @Override
//        public void onReset() {
//            mTipView.setVisibility(View.INVISIBLE);
//            updateText(mTimer.getRemainingTimeMillis(), mWhiteColor);
//        }
//    };

    private boolean mRunning;

    private ChangeListener mChangeListener;

    public HelloView(Context context) {
        this(context, null, 0);
    }

    public HelloView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        

    }

    public HelloView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        
        LayoutInflater.from(context).inflate(R.layout.card_main, this);
        updateText();
    }

    /**
     * Sets a {@link ChangeListener}.
     */
    public void setListener(ChangeListener listener) {
        mChangeListener = listener;
    }

    /**
     * Returns the set {@link ChangeListener}.
     */
    public ChangeListener getListener() {
        return mChangeListener;
    }

    @Override
    public boolean postDelayed(Runnable action, long delayMillis) {
        return mHandler.postDelayed(action, delayMillis);
    }

    @Override
    public boolean removeCallbacks(Runnable action) {
        mHandler.removeCallbacks(action);
        return true;
    }

    /**
     * Updates the text from the Timer's value, overridable for testing.
     */
    protected void updateText() {
//    	mHoursView.setText("test");
        if (mChangeListener != null) {
            mChangeListener.onChange();
        }
    }

}
