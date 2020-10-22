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

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclersample.addFlower.AddFlowerActivity
import com.example.recyclersample.flowerDetail.FlowerDetailActivity
import com.example.recyclersample.R
import com.example.recyclersample.addFlower.FLOWER_DESCRIPTION
import com.example.recyclersample.addFlower.FLOWER_NAME

const val FLOWER_ID = "flower id"

class FlowersListActivity : AppCompatActivity() {
    private val newFlowerActivityRequestCode = 1
    private lateinit var flowerListViewModel: FlowersListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flowerListViewModel = ViewModelProvider(
            this,
            FlowerListViewModelFactory(this.applicationContext)
        ).get(FlowersListViewModel::class.java)

        val flowersAdapter = FlowersAdapter { flower -> adapterOnClick(flower) }

        flowerListViewModel.flowersLiveData.observe(this, {
            it?.let {
                flowersAdapter.submitList(it as MutableList<DataItem>)
            }
        })

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = flowersAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            fabOnClick()
        }
    }

    private fun adapterOnClick(flowerItem: DataItem.FlowerItem) {
        val intent = Intent(this, FlowerDetailActivity()::class.java)
        intent.putExtra(FLOWER_ID, flowerItem.flower.id)
        this.startActivity(intent)
    }

    private fun fabOnClick() {
        val intent = Intent(this@FlowersListActivity, AddFlowerActivity()::class.java)
        startActivityForResult(intent, newFlowerActivityRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newFlowerActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val flowerName = data.getStringExtra(FLOWER_NAME)
                val flowerDescription = data.getStringExtra(FLOWER_DESCRIPTION)

                flowerListViewModel.insertFlower(flowerName, flowerDescription)
            }
        }
    }
}