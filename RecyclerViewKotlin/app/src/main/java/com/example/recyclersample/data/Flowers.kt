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

package com.example.recyclersample.data

import android.content.res.Resources
import com.example.recyclersample.R

fun flowerList(resources: Resources): List<Flower> {
    return listOf(
        Flower(
            1,
            resources.getString(R.string.flower1_name),
            R.drawable.rose,
            resources.getString(R.string.flower1_description)
        ),
        Flower(
            2,
            resources.getString(R.string.flower2_name),
            R.drawable.freesia,
            resources.getString(R.string.flower2_description)
        ),
        Flower(
            3,
            resources.getString(R.string.flower3_name),
            R.drawable.lily,
            resources.getString(R.string.flower3_description)
        ),
        Flower(
            4,
            resources.getString(R.string.flower4_name),
            R.drawable.sunflower,
            resources.getString(R.string.flower4_description)
        ),
        Flower(
            5,
            resources.getString(R.string.flower5_name),
            R.drawable.peony,
            resources.getString(R.string.flower5_description)
        ),
        Flower(
            6,
            resources.getString(R.string.flower6_name),
            R.drawable.daisy,
            resources.getString(R.string.flower6_description)
        ),
        Flower(
            7,
            resources.getString(R.string.flower7_name),
            R.drawable.lilac,
            resources.getString(R.string.flower7_description)
        ),
        Flower(
            8,
            resources.getString(R.string.flower8_name),
            R.drawable.marigold,
            resources.getString(R.string.flower8_description)
        ),
        Flower(
            9,
            resources.getString(R.string.flower9_name),
            R.drawable.poppy,
            resources.getString(R.string.flower9_description)
        ),
        Flower(
            10,
            resources.getString(R.string.flower10_name),
            R.drawable.daffodil,
            resources.getString(R.string.flower10_description)
        ),
        Flower(
            11,
            resources.getString(R.string.flower11_name),
            R.drawable.dahlia,
            resources.getString(R.string.flower11_description)
        )
    )
}