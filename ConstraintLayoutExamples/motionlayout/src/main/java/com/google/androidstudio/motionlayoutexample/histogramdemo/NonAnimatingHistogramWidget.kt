/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.google.androidstudio.motionlayoutexample.histogramdemo

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.google.androidstudio.motionlayoutexample.R

/**
 * Histogram widget example that does not animate. Starting point for the medium article.
 * In the end we wish to create [HistogramWidget].
 *
 * To try this class out, replace the usages of [HistogramWidget] in [HistogramActivity] and
 * the layout.
 */
class NonAnimatingHistogramWidget : ConstraintLayout {
    companion object {
        private const val TAG = "NonAnimatingHistogramWidget"
        private const val DEFAULT_HEIGHT_DP = 1
        private const val DEFAULT_HEIGHT_PERCENT = 0.01f
    }

    // All the bars, id to view map. Alternatively we can use findViewById.
    private val bars: MutableMap<Int, TextView> = HashMap()

    // Currently state of the bars
    private var currentBars: MutableList<HistogramBarMetaData> = ArrayList()
    // The final positions of the bars
    private var nextBars: ArrayList<HistogramBarMetaData> = ArrayList()

    // Default left margin in dp
    private var leftMarginDp = 0

    // Weight to use for the horizontal chain (of bars)
    private val weights: FloatArray

    /**
     * Number of bars in the histogram.
     * Set by custom attribute [R.styleable.HistogramWidget_columns] in xml.
     */
    var barsSize = 0
        private set(value) { field = value }

    /**
     * The list of View ids of the bar in the histogram in order.
     */
    val barIds: List<Int> get() = currentBars.map { it.id }

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
            super(context, attrs, defStyleAttr) {
        val array = context.theme.obtainStyledAttributes(
                attrs, R.styleable.HistogramWidget, 0, 0)
        try {
            barsSize = array.getInt(R.styleable.HistogramWidget_columns, 0)
            leftMarginDp = array.getInt(R.styleable.HistogramWidget_leftMarginDp, 0)
        } finally {
            array.recycle()
        }
        weights = FloatArray(barsSize)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        createBars(this, barsSize)
    }

    /**
     * Sets data to the widget.
     */
    fun setData(newData: List<HistogramBarMetaData>) {
        val endSet = ConstraintSet()
        endSet.clone(this)

        updateConstraintSet(endSet, newData)
        nextBars = ArrayList(newData)
        endSet.applyTo(this)
    }

    fun animateWidget() {
        // TODO: We'll be implementing this in the example.
        // Do nothing for now.
    }

    fun sort(): ArrayList<HistogramBarMetaData> {
        // TODO: We'll be implementing this in the example.
        // Do nothing for now.
        return nextBars
    }

    /**
     * Update the constraint set with the bar metadata.
     */
    private fun updateConstraintSet(
            set: ConstraintSet,
            list: List<HistogramBarMetaData>) {
        list.forEach { metadata ->
            val view = bars[metadata.id]!!
            val height: Float = metadata.height * height
            view.setTextColor(metadata.barTextColour)
            view.text = metadata.name

            set.constrainHeight(view.id, height.toInt())
            set.setColorValue(view.id, "BackgroundColor", metadata.barColour)
        }
    }

    /**
     * Programmatically create views that represent histogram bars
     */
    private fun createBars(layout: ConstraintLayout, columns: Int) {
        if (columns <= 1) {
            return
        }

        val marginInDp = fromDp(context, leftMarginDp)
        val size = fromDp(context, DEFAULT_HEIGHT_DP)

        val set = ConstraintSet()
        set.clone(layout)
        for (i in 0 until columns) {
            val bar = createBar(layout.context)
            val barColour = ContextCompat.getColor(context, R.color.colorPrimary)

            // Initialize to the best knowledge (non-zero width/height so it's not gone)
            bar.text = i.toString()
            bar.background = ColorDrawable(barColour)
            val layoutParams = LayoutParams(size, size)
            layout.addView(bar, layoutParams)
            set.constrainHeight(bar.id, size)
            set.connect(
                    bar.id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM)
            set.setMargin(bar.id, ConstraintSet.END, marginInDp)
            weights[i] = 1f

            // Create the currentBars list to best mimic the initial state.
            currentBars.add(HistogramBarMetaData(bar.id, DEFAULT_HEIGHT_PERCENT, barColour, 0, bar.text.toString()))
            bars[bar.id] = bar
        }
        set.createHorizontalChain(
                ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,
                barIds.toIntArray(), weights, ConstraintSet.CHAIN_SPREAD
        )
        set.applyTo(layout)
    }

    /**
     * Create a single bar.
     */
    private fun createBar(context: Context): TextView {
        val bar = TextView(context)
        bar.id = ViewGroup.generateViewId()
        bar.gravity = Gravity.CENTER
        return bar
    }

    /**
     * Get px from dp
     */
    private fun fromDp(context: Context, inDp: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (inDp * scale).toInt()
    }
}
