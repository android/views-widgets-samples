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

package com.example.recyclersample.flowerList

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclersample.addFlower.AddFlowerActivity
import com.example.recyclersample.flowerDetail.FlowerDetailActivity
import com.example.recyclersample.R
import com.example.recyclersample.addFlower.FLOWER_DESCRIPTION
import com.example.recyclersample.addFlower.FLOWER_NAME
import com.example.recyclersample.data.Flower

const val FLOWER_ID = "flower id"

class FlowersListActivity : AppCompatActivity() {
    private val newFlowerActivityRequestCode = 1
    private val flowersListViewModel by viewModels<FlowersListViewModel> {
        FlowersListViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Instantiates headerAdapter and flowersAdapter. Both adapters are added to concatAdapter.
        which displays the contents sequentially */
        val headerAdapter = HeaderAdapter()
        val flowersAdapter = FlowersAdapter { flower -> adapterOnClick(flower) }
        val concatAdapter = ConcatAdapter(headerAdapter, flowersAdapter)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = concatAdapter

        flowersListViewModel.flowersLiveData.observe(this, {
            it?.let {
                flowersAdapter.submitList(it as MutableList<Flower>)
                headerAdapter.updateFlowerCount(it.size)
            }
        })

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            fabOnClick()
        }
    }

    /* Opens FlowerDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(flower: Flower) {
        val intent = Intent(this, FlowerDetailActivity()::class.java)
        intent.putExtra(FLOWER_ID, flower.id)
        startActivity(intent)
    }

    /* Adds flower to flowerList when FAB is clicked. */
    private fun fabOnClick() {
        val intent = Intent(this, AddFlowerActivity::class.java)
        startActivityForResult(intent, newFlowerActivityRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        /* Inserts flower into viewModel. */
        if (requestCode == newFlowerActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val flowerName = data.getStringExtra(FLOWER_NAME)
                val flowerDescription = data.getStringExtra(FLOWER_DESCRIPTION)

                flowersListViewModel.insertFlower(flowerName, flowerDescription)
            }
        }
    }
}