/*
 * Copyright (C) 2018 The Android Open Source Project
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

package com.google.androidstudio.motionlayoutexample.fragmentsdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.androidstudio.motionlayoutexample.R

class ItemFragment : Fragment() {

    companion object {
        fun newInstance() = ItemFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.item_layout, container, false)
    }

    private lateinit var myHolder: CustomAdapter.ViewHolder

    fun update(holder: CustomAdapter.ViewHolder) {
        myHolder = holder
        view?.findViewById<TextView>(R.id.txtTitle)?.text = holder.txtTitle.text
        view?.findViewById<TextView>(R.id.txtName)?.text = holder.txtName.text
    }

    override fun onStart() {
        super.onStart()
        if (this::myHolder.isInitialized) {
            update(myHolder)
        }
    }
}