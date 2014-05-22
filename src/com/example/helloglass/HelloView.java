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
import android.widget.TextView;

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

	private final Handler mHandler = new Handler();

	private ChangeListener mChangeListener;

	private TextView sampleTextview;

	public HelloView(Context context) {
		this(context, null, 0);
	}

	public HelloView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public HelloView(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);

		LayoutInflater.from(context).inflate(R.layout.card_main, this);
		sampleTextview = (TextView) findViewById(R.id.sample_txt);
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
		if (mChangeListener != null) {
			mChangeListener.onChange();
		}
	}

	public void changeText(String text) {
		sampleTextview.setText(text);
		updateText();
	}

}
