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

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.example.recyclersample.data.DataSource
import com.example.recyclersample.data.Flower
import kotlin.random.Random

class FlowersListViewModel(val datasource: DataSource) : ViewModel() {

    val flowersLiveData = datasource.getFlowerList().map { list ->
        listOf(DataItem.HeaderItem) + list.map { DataItem.FlowerItem(it) }
    }

    fun insertFlower(flowerName: String?, flowerDescription: String?) {
        if(flowerName == null || flowerDescription == null){
            return
        }

        val image = datasource.getRandomFlowerAsset()
        val newFlower = Flower(
            Random.nextLong(),
            flowerName,
            image,
            flowerDescription
        )

        datasource.addFlower(newFlower)
    }
}

class FlowerListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlowersListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FlowersListViewModel(
                datasource = DataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed class DataItem {
    abstract val id: Long

    data class FlowerItem(val flower: Flower) : DataItem() {
        override val id = flower.id
    }

    object HeaderItem : DataItem() {
        override val id = Long.MIN_VALUE
    }
}