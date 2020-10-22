/*
 * Copyright 2020 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.recyclersample.flowerList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclersample.R

private const val HEADER_TYPE = 0
private const val FLOWER_TYPE = 1

class FlowersAdapter(private val onClick: (DataItem.FlowerItem) -> Unit) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(
        FlowerDiffCallback()
    ) {

    class FlowerViewHolder(itemView: View, val onClick: (DataItem.FlowerItem) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val flowerTextView: TextView = itemView.findViewById(R.id.flower_text)
        private val flowerImageView: ImageView = itemView.findViewById(R.id.flower_image)
        private var currentDataItem: DataItem.FlowerItem? = null

        init {
            itemView.setOnClickListener {
                currentDataItem?.let {
                    onClick(it)
                }
            }
        }

        fun bind(dataItem: DataItem.FlowerItem) {
            val flower = dataItem.flower
            currentDataItem = dataItem

            flowerTextView.text = flower.name
            if (flower.image != null) {
                flowerImageView.setImageResource(flower.image)
            } else {
                flowerImageView.setImageResource(R.drawable.rose)
            }
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            HEADER_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.header_item, parent, false)
                HeaderViewHolder(view)
            }
            FLOWER_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.flower_item, parent, false)
                FlowerViewHolder(view, onClick)
            }
            else -> { throw Exception("Invalid viewType") }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FlowerViewHolder) {
            val flower = getItem(position) as DataItem.FlowerItem
            holder.bind(flower)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.HeaderItem -> HEADER_TYPE
            is DataItem.FlowerItem -> FLOWER_TYPE
        }
    }
}

private class FlowerDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }
}