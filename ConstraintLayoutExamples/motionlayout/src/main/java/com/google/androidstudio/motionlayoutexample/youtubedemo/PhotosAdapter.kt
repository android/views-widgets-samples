/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.androidstudio.motionlayoutexample.youtubedemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.androidstudio.motionlayoutexample.R
import com.google.androidstudio.motionlayoutexample.youtubedemo.YouTubeDemoViewHolder.CatRowViewHolder
import com.google.androidstudio.motionlayoutexample.youtubedemo.YouTubeDemoViewHolder.TextDescriptionViewHolder
import com.google.androidstudio.motionlayoutexample.youtubedemo.YouTubeDemoViewHolder.TextHeaderViewHolder

class FrontPhotosAdapter : RecyclerView.Adapter<YouTubeDemoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YouTubeDemoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.motion_24_recyclerview_expanded_text_header -> TextHeaderViewHolder (itemView)
            R.layout.motion_24_recyclerview_expanded_text_description -> TextDescriptionViewHolder (itemView)
            R.layout.motion_24_recyclerview_expanded_row -> CatRowViewHolder(itemView)
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: YouTubeDemoViewHolder, position: Int) {
        when (holder) {
            is TextHeaderViewHolder -> {}
            is TextDescriptionViewHolder -> {}
            is CatRowViewHolder -> {
                val imagePosition = position - 2
                holder.textView.text = holder.textView.resources.getString(R.string.cat_n, imagePosition)
                Glide.with(holder.imageView)
                    .load(Cats.catImages[imagePosition])
                    .into(holder.imageView)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return when (position) {
            0 -> R.layout.motion_24_recyclerview_expanded_text_header
            1 -> R.layout.motion_24_recyclerview_expanded_text_description
            else -> R.layout.motion_24_recyclerview_expanded_row
        }
    }

    override fun getItemCount() = Cats.catImages.size + 2 // For text header and text description
}

/**
 * [RecyclerView.ViewHolder] types used by this adapter.
 */
sealed class YouTubeDemoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class TextHeaderViewHolder(
        itemView: View
    ) : YouTubeDemoViewHolder(itemView)

    class TextDescriptionViewHolder(
        itemView: View
    ) : YouTubeDemoViewHolder(itemView)

    class CatRowViewHolder(
        itemView: View
    ) : YouTubeDemoViewHolder(itemView) {
        val imageView = itemView.findViewById(R.id.image_row) as ImageView
        val textView = itemView.findViewById(R.id.text_row) as TextView
    }
}
