/*
 * Copyright (C) 2016 The Android Open Source Project
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

package com.android.multiwindowplayground.activities;

import com.android.multiwindowplayground.R;
import com.example.android.common.logger.Log;
import com.example.android.common.logger.LogFragment;
import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.MessageOnlyLogFilter;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Activity that logs all key lifecycle callbacks to {@link Log}.
 * Output is also logged to the UI into a {@link LogFragment} through {@link #initializeLogging()}
 * and {@link #stopLogging()}.
 */
public abstract class LoggingActivity extends AppCompatActivity {

    protected String mLogTag = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(mLogTag, "onCreate");


    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        Log.d(mLogTag, "onPostCreate");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(mLogTag, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(mLogTag, "onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(mLogTag, "onResume");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(mLogTag, "onConfigurationChanged: " + newConfig.toString());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d(mLogTag, "onPostCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start logging to UI.
        initializeLogging();

        Log.d(mLogTag, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop logging to UI when this activity is stopped.
        stopLogging();

        Log.d(mLogTag, "onStop");
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);

        Log.d(mLogTag, "onMultiWindowModeChanged: " + isInMultiWindowMode);
    }

    // Logging and UI methods below.

    /** Set up targets to receive log data */
    public void initializeLogging() {
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment logFragment = (LogFragment) getSupportFragmentManager()
                .findFragmentById(R.id.log_fragment);
        msgFilter.setNext(logFragment.getLogView());
    }

    public void stopLogging() {
        Log.setLogNode(null);
    }

    /**
     * Set the description text if a TextView with the id <code>description</code> is available.
     */
    protected void setDescription(@StringRes int textId) {
        // Set the text and background color
        TextView description = (TextView) findViewById(R.id.description);
        if (description != null) {
            description.setText(textId);
        }
    }

    /**
     * Set the background color for the description text.
     */
    protected void setBackgroundColor(@ColorRes int colorId) {
        View scrollView = findViewById(R.id.scrollview);
        if (scrollView != null) {
            scrollView.setBackgroundResource(colorId);
        }
    }

}
