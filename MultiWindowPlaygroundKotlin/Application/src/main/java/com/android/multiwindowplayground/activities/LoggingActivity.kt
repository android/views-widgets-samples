/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.android.multiwindowplayground.activities

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.android.multiwindowplayground.R
import com.android.multiwindowplayground.logger.Log
import com.android.multiwindowplayground.logger.LogFragment
import com.android.multiwindowplayground.logger.LogWrapper
import com.android.multiwindowplayground.logger.MessageOnlyLogFilter

const val LOG_TAG = "LoggingActivity"

/**
 * Activity that logs all key lifecycle callbacks to [Log].
 * Output is also logged to the UI into a [LogFragment] through [initializeLogging] and
 * [stopLogging].
 */
abstract class LoggingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.d(LOG_TAG, "onCreatePersistable")
    }

    override fun onStart() {
        super.onStart()
        // Start logging to UI.
        initializeLogging()

        Log.d(LOG_TAG, "onStart")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(LOG_TAG, "onRestoreInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        Log.d(LOG_TAG, "onRestoreInstanceStatePersistable")
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "onResume")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Log.d(LOG_TAG, "onPostCreate")
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        Log.d(LOG_TAG, "onPostCreate")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(LOG_TAG, "onConfigurationChanged: $newConfig")
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean, newConfig: Configuration?) {
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig)
        Log.d(LOG_TAG, "onMultiWindowModeChanged: $isInMultiWindowMode")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.d(LOG_TAG, "onSaveInstanceState")
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        Log.d(LOG_TAG, "onSaveInstanceStatePersistable")
    }

    override fun onPause() {
        super.onPause()
        Log.d(LOG_TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        // Stop logging to UI when this activity is stopped.
        stopLogging()

        Log.d(LOG_TAG, "onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(LOG_TAG, "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "onDestroy")
    }

    /**
     * Set up targets to receive log data
     */
    private fun initializeLogging() {
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        // Wraps Android's native log framework
        val logWrapper = LogWrapper()
        Log.logNode = logWrapper

        // Filter strips out everything except the message text.
        val msgFilter = MessageOnlyLogFilter()
        logWrapper.next = msgFilter

        // On screen logging via a fragment with a TextView.
        val logFragment = supportFragmentManager.findFragmentById(R.id.log_fragment) as LogFragment
        msgFilter.next = logFragment.logView
    }

    private fun stopLogging() {
        Log.logNode = null
    }

    protected fun setDescription(@StringRes textId: Int) {
        findViewById<TextView>(R.id.description).setText(textId)
    }

    protected fun setBackgroundColor(@ColorRes colorId: Int) {
        findViewById<View>(R.id.scrollview).setBackgroundResource(colorId)
    }

}
