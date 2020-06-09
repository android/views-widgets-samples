package com.google.androidstudio.motionlayoutexample.fragmentsdemo

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.androidstudio.motionlayoutexample.R
import com.google.androidstudio.motionlayoutexample.viewpagerdemo.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_dragon_boat.*
import java.util.concurrent.atomic.AtomicBoolean


class FragmentExample3Activity : AppCompatActivity(), View.OnClickListener, MotionLayout.TransitionListener {

    private lateinit var viewPagerMotionLayout: MotionLayout

    private lateinit var cardMotionLayout: MotionLayout

    private var transitionOnViewPagerUpdated: AtomicBoolean = AtomicBoolean(false)

    override fun onClick(view: View?) {
        Log.d("hydrated", "view clicked: ${view?.id}")
    }

    override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {
        Log.d("hydrated", "onTransitionTrigger: ${motionLayout?.id} wit progress: $progress")
    }

    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
        Log.d("hydrated", "onTransitionStarted")
//        if (motionLayout?.id == rootMotionLayout.id) {
//            Log.d("hydrated", "It's card transition..disable viewpager transition..")
//            viewPagerMotionLayout.setTransition(R.id.start, R.id.start)
//        }
    }

    override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
        if (motionLayout?.id == cardMotionLayout.id) {

            Log.d("hydrated", "onTransitionChange on cardMotionLayout with progress: $progress")

            if (progress == 0.0f) {
                Log.d("hydrated", "It's card transition $progress..enable viewpager transition..")
                viewPagerMotionLayout.setTransition(R.id.start, R.id.end)
                endingContainer.setOnTouchListener { view, motionEvent -> return@setOnTouchListener false }
                transitionOnViewPagerUpdated.getAndSet(false)
            } else {
                if (transitionOnViewPagerUpdated.get().not()) {
                    Log.d("hydrated", "It's card transition $progress..disable viewpager transition..")
                    viewPagerMotionLayout.setTransition(R.id.pagerMoveDownStart, R.id.pagerMoveDownEnd)
                    endingContainer.setOnTouchListener { view, motionEvent -> return@setOnTouchListener true }
                    transitionOnViewPagerUpdated.getAndSet(true)
                }
                viewPagerMotionLayout.setInterpolatedProgress(progress)
            }
        } else if (motionLayout?.id == viewPagerMotionLayout.id) {

            Log.d("hydrated", "onTransitionChange on viewPagerMotionLayout with progress: $progress")

//            if (progress == 0.0f) {
//                cardMotionLayout.setTransition(R.id.cardSceneStart, R.id.cardSceneEnd)
//            } else {
//                cardMotionLayout.setTransition(R.id.sideCardMoveRightStart, R.id.sideCardMoveRightEnd)
//                cardMotionLayout.setInterpolatedProgress(progress)
//            }
        }
    }

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dragon_boat)

        viewPagerMotionLayout = findViewById<MotionLayout>(R.id.motionLayout)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addPage("Page 1", R.layout.motion_16_viewpager_page1)
        adapter.addPage("Page 2", R.layout.motion_16_viewpager_page2)
        adapter.addPage("Page 3", R.layout.motion_16_viewpager_page3)
        viewpager.adapter = adapter
        tabs.setupWithViewPager(viewpager)

        motionLayout.setDebugMode(MotionLayout.DEBUG_SHOW_PATH)
        motionLayout.setTransitionListener(this)

        cardMotionLayout = findViewById<MotionLayout>(R.id.rootMotionLayout)
        cardMotionLayout.setDebugMode(MotionLayout.DEBUG_SHOW_PATH)
        cardMotionLayout.setTransitionListener(this)


    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

}