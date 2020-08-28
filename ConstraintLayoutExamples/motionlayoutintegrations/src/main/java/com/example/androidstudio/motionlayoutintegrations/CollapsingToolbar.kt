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

package com.example.androidstudio.motionlayoutintegrations

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidstudio.motionlayoutintegrations.databinding.ActivityCollapsingToolbarBinding
import com.google.android.material.appbar.AppBarLayout

/**
 * Display a collapsing toolbar built using MotionLayout that handles insets and uses a custom view
 */
class CollapsingToolbar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.goEdgeToEdge()

        val binding = ActivityCollapsingToolbarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // When the AppBarLayout progress changes, snap MotionLayout to the current progress
        val listener = AppBarLayout.OnOffsetChangedListener { appBar, verticalOffset ->
            // convert offset into % scrolled
            val seekPosition = -verticalOffset / appBar.totalScrollRange.toFloat()
            // inform both both MotionLayout and CutoutImage of the animation progress.
            binding.motionLayout.progress = seekPosition
            binding.background.translationProgress = (100 * seekPosition).toInt()
        }
        binding.appbarLayout.addOnOffsetChangedListener(listener)

        // get the collapsed height from the motion layout specified in XML
        val desiredToolbarHeight = binding.motionLayout.minHeight

        // Set two guidelines in the collapsed state for displaying a scrim based on the inset. Also
        // resize the MotionLayout when collapsed to add the inset height.
        ViewCompat.setOnApplyWindowInsetsListener(binding.motionLayout) { _, insets: WindowInsetsCompat ->
            // resize the motionLayout in collapsed state to add the needed inset height
            val insetTopHeight = insets.systemWindowInsetTop
            binding.motionLayout.minimumHeight = desiredToolbarHeight + insetTopHeight

            // modify the end ConstraintSet to set a guideline at the top and bottom of inset
            val endConstraintSet = binding.motionLayout.getConstraintSet(R.id.collapsed)
            // this guideline is the bottom of the inset area
            endConstraintSet.setGuidelineEnd(R.id.inset, desiredToolbarHeight)
            // this guideline is the top of the inset area (top of screen)
            endConstraintSet.setGuidelineEnd(R.id.collapsed_top, desiredToolbarHeight + insetTopHeight)

            // set the guideline for the start constraint set as well
            val startConstraintSet = binding.motionLayout.getConstraintSet(R.id.expanded)
            startConstraintSet.setGuidelineBegin(R.id.collapsed_top, insetTopHeight)

            insets
        }
    }

    /**
     * Set various flags to go edge to edge
     */
    private fun Window.goEdgeToEdge() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }
        // TODO: replace this with non-deprecated edge to edge option
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}

/**
 * A custom view to display a circular cutout on an image that can be controlled by MotionLayout.
 *
 * Animation of this view is driven by motionLayout controlling [bottomCutSize] and [endCutSize]
 * and [translationProgress].
 *
 * This View will overwrite scaleType from XML to be matrix to allow custom translation. This is
 * a slightly more efficient way to translate a background than oversizing the view and changing
 * constraints as is done in [Entrance].
 */
class CutoutImage @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private val scratchRect = RectF()

    private var _bottomCutSize: Float

    /**
     * Set the size of the bottomCut.
     *
     * This can directly be called by MotionLayout to animate the size.
     */
    var bottomCutSize: Float
        get() = _bottomCutSize
        set(value) {
            _bottomCutSize = value
            invalidate()
        }

    private var _endCutSize: Float

    /**
     * Set the size of the endCut.
     *
     * This can directly be called by MotionLayout to animate the size.
     */
    var endCutSize: Float
        get() = _endCutSize
        set(value) {
            _endCutSize = value
            invalidate()
        }

    /**
     * Fixed image translation progress to make the image scroll as animation progresses.
     *
     * This uses a Matrix to scale then translate the image based on the current progress.
     *
     * This can be directly called by MotionLayout, or be called in response to progress change like
     * we do in this sample.
     */
    var translationProgress: Int = 0
        set(value) {
            field = value
            val matrix = imageMatrix
            val imageWidth = drawable.intrinsicWidth.toFloat()
            val scaleFactor = width.toFloat() / imageWidth
            matrix.setScale(scaleFactor, scaleFactor)
            matrix.postTranslate(0f, -100f + value)
            imageMatrix = matrix
        }

    private val painter = Paint()

    private val grayPainter = Paint().also {
        it.color = 0x33000000
        it.strokeWidth = dpToF(1)
    }

    /**
     * Read the endCut, bottomCut, and cutoutColor from XML
     */
    init {
        val typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CutoutImage,
                0,
                0
        )

        _endCutSize = typedArray.getDimension(R.styleable.CutoutImage_endCut, dpToF(0))
        _bottomCutSize = typedArray.getDimension(R.styleable.CutoutImage_bottomCut, dpToF(0))
        painter.color = typedArray.getColor(R.styleable.CutoutImage_cutoutColor, 0xFFaaFFaa.toInt())
        typedArray.recycle()
    }

    /**
     * Force the scaleType to matrix
     */
    init {
        scaleType = ScaleType.MATRIX // ignore any other scale types
    }

    private fun dpToF(value: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(),
        resources.displayMetrics
    )

    /**
     * Draw the image with current cutouts applied
     */
    override fun onDraw(canvas: Canvas?) {
        // let the parent draw the bitmap
        super.onDraw(canvas)

        // draw the bottom circle at the correct position and size
        canvas?.drawCircle(
            width.toFloat() / 2, // midpoint of view
            height.toFloat(), // bottom of view
            _bottomCutSize / 2, // radius from diameter
            painter
        )

        // draw the end circle at the correct position and size
        val margin = dpToF(16)
        canvas?.drawCircle(
            width - margin, // end of view, with custom margin applied
            2 * height.toFloat() / 3, // 2/3 down on view (determined by designer)
            _endCutSize / 2, // radius from diameter
            painter
        )

        // add a 1px gray line to the bottom of the end circle region so it clearly divides from
        // surrounding region (this effectively brings the shadow in early on the end circle)
        canvas?.drawLine(
            // start at the left edge of circle (this could do trig to calculate intersection
            // between circle and bottom, but visually this works fine)
            width - margin - _endCutSize / 2,
            height.toFloat(), // bottom of view
            width.toFloat(), // to end of view X
            height.toFloat(), // bottom of view
            grayPainter
        )
    }
}