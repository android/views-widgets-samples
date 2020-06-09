package com.google.androidstudio.motionlayoutexample.utils.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.google.androidstudio.motionlayoutexample.R

class DebitCardFragment : Fragment(), MotionLayout.TransitionListener {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_debit_card, container, false)
    }

    lateinit var big_scene: MotionLayout
    lateinit var local_scene: MotionLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        big_scene = activity?.findViewById<MotionLayout>(R.id.main_motionlayout)!!
        local_scene = activity?.findViewById<MotionLayout>(R.id.motionLayoutCard)!!
        local_scene.setTransitionListener(this)
    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
    }

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
        
    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
    }

}