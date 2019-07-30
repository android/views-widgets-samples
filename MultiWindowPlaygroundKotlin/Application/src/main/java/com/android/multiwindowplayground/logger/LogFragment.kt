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

package com.android.multiwindowplayground.logger

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ScrollView

/**
 * Simple fragment which contains a [LogView] and is used to output log data it receives through the
 * [LogNode] interface.
 */
class LogFragment : Fragment() {

    internal lateinit var logView: LogView
    private lateinit var scrollView: ScrollView

    private fun inflateViews(): View {
        val scrollParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        val logParams = ViewGroup.LayoutParams(scrollParams).apply {
            height = WRAP_CONTENT
        }

        // Want to set padding as 16 dips, setPadding takes pixels.  Hooray math!
        val paddingDips = 16
        val scale = resources.displayMetrics.density.toDouble()
        val paddingPixels = (paddingDips * scale + .5).toInt()

        logView = LogView(activity as FragmentActivity).apply {
            setTextAppearance(android.R.style.TextAppearance_Material_Medium)
            layoutParams = logParams
            isClickable = true
            isFocusable = true
            typeface = Typeface.create("monospace", Typeface.NORMAL)
            setPadding(paddingPixels, paddingPixels, paddingPixels, paddingPixels)
            compoundDrawablePadding = paddingPixels
            gravity = Gravity.BOTTOM
        }

        scrollView = ScrollView(activity).apply {
            layoutParams = scrollParams
            addView(logView)
        }
        return scrollView
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val result = inflateViews()

        logView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                scrollView.run { post { smoothScrollTo(0, scrollView.bottom + logView.height) }}
            }
        })
        return result
    }

}