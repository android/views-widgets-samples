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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.androidstudio.motionlayoutintegrations.databinding.ActivityViewPagerIntegrationBinding

/**
 * Demonstrate driving an animated header built using MotionLayout from a user swiping in a
 * ViewPager
 */
class ViewPagerIntegration : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityViewPagerIntegrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set up view pager to have three tabs
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addPageFragment(Page1(), "List")
        adapter.addPageFragment(Page2(), "Item")
        adapter.addPageFragment(Page3(), "Launch")

        binding.pager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.pager)

        // use a page change listener to transfer swipe progress to MotionLayout
        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // calculate the swipe progress to a percent between 0f and 1f, where 0f is the
                // first tab and 1f is the last tab
                val progress = (position + positionOffset) / (adapter.count - 1)
                // ask MotionLayout to snap to the current progress
                binding.motionLayout.progress = progress
            }

            override fun onPageSelected(position: Int) {
                // ignore
            }

            override fun onPageScrollStateChanged(state: Int) {
                // ignore
            }

        })
    }
}

/**
 * Adapter needed for ViewPager
 */
class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private val fragmentList = ArrayList<Fragment>()
    private val fragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addPageFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }
}

/**
 * Fragments for ViewPager
 */
class Page1 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_1, container, false)
    }
}

/**
 * Fragments for ViewPager
 */
class Page2 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_2, container, false)
    }
}

/**
 * Fragments for ViewPager
 */
class Page3 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_3, container, false)
    }
}