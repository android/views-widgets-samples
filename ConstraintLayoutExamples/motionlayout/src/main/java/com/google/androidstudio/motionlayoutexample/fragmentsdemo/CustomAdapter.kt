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

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.androidstudio.motionlayoutexample.R
import com.google.androidstudio.motionlayoutexample.databinding.ItemLayoutBinding

class CustomAdapter(private val userList: ArrayList<User>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txtName.text = userList[position].name
        holder.binding.txtTitle.text = userList[position].title
        holder.itemView.setOnClickListener {
            val parent = it?.parent?.parent?.parent?.parent
            if (parent is MotionLayout) {
                val offsetViewBounds = Rect()
                it.getDrawingRect(offsetViewBounds)
                parent.offsetDescendantRectToMyCoords(it, offsetViewBounds)
                val transaction = (it.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val fragment = ItemFragment.newInstance()
                fragment.update(holder)
                transaction.replace(R.id.container, fragment)
                transaction.commitNow()
                parent.transitionToEnd()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}