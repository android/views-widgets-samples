package com.google.androidstudio.motionlayoutexample.histogramdemo

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import androidx.core.content.ContextCompat
import com.google.androidstudio.motionlayoutexample.R
import kotlinx.android.synthetic.main.histogram_layout.*
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.ArrayList

class HistogramActivity : AppCompatActivity() {

    companion object {
        private const val DEFAULT_COLOUR_ID = R.color.colorAccent
        private const val HISTOGRAM_BARS_RESTORE_KEY = "HISTOGRAM"
    }

    // List used for save and restoring data to the widget
    private var mBars: ArrayList<HistogramBarMetaData> = ArrayList()

    // The main widget
    private var mWidget: HistogramWidget? = null

    // Animation guard
    private val mAnimating = AtomicBoolean(false)
    private val mAnimationListener: TransitionListener = object : BasicTransitionListener() {
        override fun onTransitionStarted(motionLayout: MotionLayout, startId: Int, endId: Int) {
            mAnimating.set(true)
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
            mAnimating.set(false)
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.histogram_layout)
        mWidget = histogram
        restoreView(savedInstanceState, mWidget!!)
        mWidget!!.setTransitionListener(mAnimationListener)
    }

    /**
     * Add random data to the histogram.
     */
    fun onClickAdd(view: View?) {
        if (mAnimating.get()) {
            return
        }
        add()
        mWidget!!.animateWidget()
    }

    fun onClickSort(view: View?) {
        if (mAnimating.get()) {
            return
        }
        mBars = mWidget!!.sort()
        mWidget!!.animateWidget()
    }

    fun onClickRandom(view: View) {
        if (mAnimating.get()) {
            return
        }
        add()
        mBars = mWidget!!.sort()
        mWidget!!.animateWidget()
    }

    private fun add() {
        val rand = Random()
        var barColour = ContextCompat.getColor(this, DEFAULT_COLOUR_ID)
        val barDataList = ArrayList<HistogramBarMetaData>(mWidget!!.size)
        var name = 0
        for (id in mWidget!!.barIds) {
            val barData = HistogramBarMetaData(
                    id,
                    rand.nextFloat(),
                    barColour,
                    ColorHelper.getContrastColor(barColour),
                    name.toString())
            barColour = ColorHelper.getNextColor(barColour)
            barDataList.add(barData)
            name++
        }
        mBars = barDataList
        mWidget!!.setData(barDataList)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (mBars.isNotEmpty()) {
            outState.putParcelableArrayList(HISTOGRAM_BARS_RESTORE_KEY, mBars)
        }
        super.onSaveInstanceState(outState)
    }

    private fun restoreView(savedInstance: Bundle?, widget: HistogramWidget) {
        if (savedInstance == null) { // nothing to restore.
            return
        }
        mBars = savedInstance.getParcelableArrayList(HISTOGRAM_BARS_RESTORE_KEY) ?: ArrayList()
        if (mBars.isEmpty()) {
            return
        }

        widget.viewTreeObserver.addOnGlobalLayoutListener(object :
                OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Because we're creating all views in code, all the view ids are recreated.
                if (mWidget!!.barIds.size != mBars.size) {
                    throw RuntimeException("Restoring array doesn't match the view size.")
                }

                val updatedBars = ArrayList<HistogramBarMetaData>()
                for (i in 0 until mBars.size) {
                    val id = mWidget!!.barIds[i]
                    val metaData = mBars[i]
                    updatedBars.add(HistogramBarMetaData(id, metaData))
                }

                mBars = updatedBars
                widget.setData(mBars)
                widget.animateWidget()
                widget.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }
}

open class BasicTransitionListener : TransitionListener {
    override fun onTransitionTrigger(p0: MotionLayout, p1: Int, p2: Boolean, p3: Float) { }

    override fun onTransitionStarted(p0: MotionLayout, p1: Int, p2: Int) { }

    override fun onTransitionChange(p0: MotionLayout, p1: Int, p2: Int, p3: Float) { }

    override fun onTransitionCompleted(p0: MotionLayout, p1: Int) { }
}
