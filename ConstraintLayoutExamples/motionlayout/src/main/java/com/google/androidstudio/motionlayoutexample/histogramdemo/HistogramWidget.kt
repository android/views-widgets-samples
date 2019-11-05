package com.google.androidstudio.motionlayoutexample.histogramdemo

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.constraintlayout.motion.widget.TransitionBuilder
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.google.androidstudio.motionlayoutexample.R
import java.util.*

class HistogramWidget : MotionLayout {
    companion object {
        private const val TAG = "HistogramWidget"
        private const val DEFAULT_HEIGHT_DP = 1
        private const val DEFAULT_HEIGHT_PERCENT = 0.01f
    }

    // All the bars, id to view map. Alternatively we can use findViewById.
    private val mBars: MutableMap<Int, TextView> = HashMap()

    // Currently state of the bars
    private var mCurrentBars: MutableList<HistogramBarMetaData> = ArrayList()
    // Bars to which we're animating towards.
    private var mNextBars: MutableList<HistogramBarMetaData> = ArrayList()

    // Number of bars in the histogram.
    private var mSize = 0
    // Default left margin in dp
    private var mLeftMarginDp = 0

    // Weight to use for the horizontal chain (of bars)
    private val mWeights: FloatArray

    // The main transition of this widget. We'll rely all animation on this transition.
    private var mBarTransition: MotionScene.Transition? = null

    /**
     * The list of View ids of the bar in the histogram in order.
     */
    val barIds: List<Int> get() = mCurrentBars.map { it.id }

    /**
     * Number of bars in the histogram.
     * Set by custom attribute [R.styleable.HistogramWidget_columns] in xml.
     */
    val size: Int get() = mSize

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
            super(context, attrs, defStyleAttr) {
        val array = context.theme.obtainStyledAttributes(
                attrs, R.styleable.HistogramWidget, 0, 0)
        try {
            mSize = array.getInt(R.styleable.HistogramWidget_columns, 0)
            mLeftMarginDp = array.getInt(R.styleable.HistogramWidget_leftMarginDp, 0)
        } finally {
            array.recycle()
        }
        mWeights = FloatArray(mSize)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        createBars(this, mSize)
        val scene = MotionScene(this)
        mBarTransition = createTransition(scene)

        /**
         * The name is unintuitive due to legacy support.
         * [MotionScene.addTransition] adds the transition to the scene while
         * [MotionScene.setTransition] sets the transition to be the current transition.
         */
        scene.addTransition(mBarTransition)
        scene.setTransition(mBarTransition)
        setScene(scene)
    }

    /**
     * Create a basic transition programmatically.
     */
    private fun createTransition(scene: MotionScene): MotionScene.Transition {
        val startSetId = View.generateViewId()
        val startSet = ConstraintSet()
        startSet.clone(this)
        val endSetId = View.generateViewId()
        val endSet = ConstraintSet()
        endSet.clone(this)
        val transitionId = View.generateViewId()
        return TransitionBuilder.buildTransition(
                scene,
                transitionId,
                startSetId, startSet,
                endSetId, endSet)
    }

    /**
     * Sets data to the widget.
     */
    fun setData(newData: List<HistogramBarMetaData>) {
        val startSet: ConstraintSet = getConstraintSet(mBarTransition!!.startConstraintSetId)
        updateConstraintSet(startSet, mCurrentBars)
        val endSet: ConstraintSet = getConstraintSet(mBarTransition!!.endConstraintSetId)
        updateConstraintSet(endSet, newData)
        mNextBars = ArrayList(newData)
    }

    /**
     * Update the constraint set with the bar metadata.
     */
    private fun updateConstraintSet(
            set: ConstraintSet,
            list: List<HistogramBarMetaData>) {
        list.forEach { metadata ->
            val view = mBars[metadata.id]!!
            val height: Float = metadata.height * height
            view.setTextColor(metadata.barTextColour)
            view.text = metadata.name

            // These are attributes we wish to animate. We set them through ConstraintSet.
            set.constrainHeight(view.id, height.toInt())
            set.setColorValue(view.id, "BackgroundColor", metadata.barColour)
        }
    }

    /**
     * Animate the widget from start to the end.
     * It'll animate based on the [.setData] and/or [.sort].
     */
    fun animateWidget() {
        val startSet: ConstraintSet = getConstraintSet(mBarTransition!!.startConstraintSetId)
        val endSet: ConstraintSet = getConstraintSet(mBarTransition!!.endConstraintSetId)

        setTransition(mBarTransition!!.startConstraintSetId, mBarTransition!!.endConstraintSetId)
        transitionToEnd()

        // Update the end state to be the current.
        startSet.clone(endSet)
        mCurrentBars = ArrayList(mNextBars)
    }

    /**
     * Sort the data and return the resulting state of the bars.
     * @return List of HistogramBarData in order at which it's sorted.
     */
    fun sort(): ArrayList<HistogramBarMetaData> {
        mNextBars.sortBy { it.height }
        val startSet: ConstraintSet = getConstraintSet(mBarTransition!!.startConstraintSetId)
        startSet.createHorizontalChain(
                ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,
                mCurrentBars.map{ it.id }.toIntArray(), mWeights, LayoutParams.CHAIN_SPREAD)
        val endSet: ConstraintSet = getConstraintSet(mBarTransition!!.endConstraintSetId)
        endSet.createHorizontalChain(
                ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,
                mNextBars.map{ it.id }.toIntArray(), mWeights, LayoutParams.CHAIN_SPREAD)

        return ArrayList(mNextBars)
    }

    /**
     * Programmatically create views that represent histogram bars
     */
    private fun createBars(layout: MotionLayout, columns: Int) {
        if (columns <= 1) {
            return
        }

        val marginInDp = fromDp(context, mLeftMarginDp)
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
            mWeights[i] = 1f

            // Create the currentBars list to best mimic the initial state.
            mCurrentBars.add(HistogramBarMetaData(bar.id, DEFAULT_HEIGHT_PERCENT, barColour, 0, bar.text.toString()))
            mBars[bar.id] = bar
        }
        set.createHorizontalChain(
                ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,
                barIds.toIntArray(), mWeights, ConstraintSet.CHAIN_SPREAD
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

    /**
     * For debugging purpose.
     */
//    private fun printDebug() {
//        val from = StringBuilder("Animating from : ")
//        for (i in mCurrentBars) {
//            from.append("$i, ")
//        }
//        Log.d(TAG, from.toString())
//        val to = StringBuilder("            to : ")
//        for (i in mNextBars) {
//            to.append("$i, ")
//        }
//        Log.d(TAG, to.toString())
//    }
}
