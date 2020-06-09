package com.google.androidstudio.motionlayoutexample.fragmentsdemo

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.androidstudio.motionlayoutexample.R
import com.google.androidstudio.motionlayoutexample.utils.fragment.DebitCardFragment
import com.google.androidstudio.motionlayoutexample.viewpagerdemo.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_dragon_boat.*


class FragmentExample4Activity : AppCompatActivity(), View.OnClickListener, MotionLayout.TransitionListener {
    override fun onClick(p0: View?) {
    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
    }

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        Log.d("hydrated", "onTransitionStarted")
    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dragon_boat4)

        val motionLayout = findViewById<MotionLayout>(R.id.motionLayoutawdaw)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addPage("Page 1", R.layout.motion_16_viewpager_page1)
        adapter.addPage("Page 2", R.layout.motion_16_viewpager_page2)
        adapter.addPage("Page 3", R.layout.motion_16_viewpager_page3)
        viewpager.adapter = adapter
        tabs.setupWithViewPager(viewpager)

//        motionLayout.setDebugMode(MotionLayout.DEBUG_SHOW_PATH)
//        motionLayout.setTransitionListener(this)

        val newFragment: Fragment = DebitCardFragment()
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.add(R.id.content, newFragment).commit()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

}