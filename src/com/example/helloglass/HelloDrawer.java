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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.View;

import com.google.android.glass.timeline.DirectRenderingCallback;
import com.google.android.glass.timeline.LiveCard;

/**
 * SurfaceHolder.Callback used to draw the timer on the timeline
 * {@link LiveCard}.
 */
public class HelloDrawer implements DirectRenderingCallback {

	private Context context;
	public static String MY_MESSAGE = "change_text";
	private SurfaceHolder mHolder;
	private boolean mRenderingPaused;
	private int num;

	private final HelloView mView;
	private final HelloView.ChangeListener mListener = new HelloView.ChangeListener() {
		@Override
		public void onChange() {
			if (mHolder != null) {
				draw();
			}
		}
	};

	public HelloDrawer(Context context) {
		mView = new HelloView(context);
		mView.setListener(mListener);
		this.context = context;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// Measure and layout the view with the canvas dimensions.
		int measuredWidth = View.MeasureSpec.makeMeasureSpec(width,
				View.MeasureSpec.EXACTLY);
		int measuredHeight = View.MeasureSpec.makeMeasureSpec(height,
				View.MeasureSpec.EXACTLY);

		mView.measure(measuredWidth, measuredHeight);
		mView.layout(0, 0, mView.getMeasuredWidth(), mView.getMeasuredHeight());
		draw();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// The creation of a new Surface implicitly resumes the rendering.
		mRenderingPaused = false;
		mHolder = holder;
		draw();
		context.registerReceiver(mBroadcast, new IntentFilter(MY_MESSAGE));
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mHolder = null;
		context.unregisterReceiver(mBroadcast);
	}

	@Override
	public void renderingPaused(SurfaceHolder holder, boolean paused) {
		mRenderingPaused = paused;
		draw();
	}

	public void draw() {
		if (!mRenderingPaused && mHolder != null) {
			Canvas canvas;
			try {
				canvas = mHolder.lockCanvas();
			} catch (Exception e) {
				return;
			}
			if (canvas != null) {
				mView.draw(canvas);
				mHolder.unlockCanvasAndPost(canvas);
			}
		}
	}

	private BroadcastReceiver mBroadcast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context mContext, Intent mIntent) {
			// TODO Auto-generated method stub
			if (MY_MESSAGE.equals(mIntent.getAction())) {
				num++;
				if (num % 3 == 0) {
					mView.changeText("I'm lovin' it.");
				} else if (num % 3 == 1) {
					mView.changeText("Oh yeah!");
				} else {
					mView.changeText("Hello, World!");
				}
			}
		}
	};
}
