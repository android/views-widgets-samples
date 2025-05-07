/*
 * Copyright 2018 The Android Open Source Project
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

package androidx.viewpager2.integration.testapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * This activity lists all the activities in this application.
 */
class BrowseActivity : Activity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)

        with(findViewById<RecyclerView>(R.id.list)) {
            layoutManager = LinearLayoutManager(this@BrowseActivity)
            adapter = BrowseAdapter(data = getData()) { map ->
                val intent = Intent(map["intent"] as Intent)
                intent.addCategory(Intent.CATEGORY_SAMPLE_CODE)
                startActivity(intent)
            }
        }
    }

    private fun getData(): List<Map<String, Any>> {
        val data = mutableListOf<Map<String, Any>>()

        data.add(
            mapOf(
                "title" to "ViewPager2 with Views",
                "intent" to activityToIntent(CardViewActivity::class.java.name)
            )
        )
        data.add(
            mapOf(
                "title" to "ViewPager2 with Fragments",
                "intent" to activityToIntent(CardFragmentActivity::class.java.name)
            )
        )
        data.add(
            mapOf(
                "title" to "ViewPager2 with a Mutable Collection (Views)",
                "intent" to activityToIntent(MutableCollectionViewActivity::class.java.name)
            )
        )
        data.add(
            mapOf(
                "title" to "ViewPager2 with a Mutable Collection (Fragments)",
                "intent" to activityToIntent(MutableCollectionFragmentActivity::class.java.name)
            )
        )
        data.add(
            mapOf(
                "title" to "ViewPager2 with a TabLayout (Views)",
                "intent" to activityToIntent(CardViewTabLayoutActivity::class.java.name)
            )
        )
        data.add(
            mapOf(
                "title" to "ViewPager2 with Fake Dragging",
                "intent" to activityToIntent(FakeDragActivity::class.java.name)
            )
        )
        data.add(
            mapOf(
                "title" to "ViewPager2 with PageTransformers",
                "intent" to activityToIntent(PageTransformerActivity::class.java.name)
            )
        )
        data.add(
            mapOf(
                "title" to "ViewPager2 with a Preview of Next/Prev Page",
                "intent" to activityToIntent(PreviewPagesActivity::class.java.name)
            )
        )
        data.add(
            mapOf(
                "title" to "ViewPager2 with Nested RecyclerViews",
                "intent" to activityToIntent(ParallelNestedScrollingActivity::class.java.name)
            )
        )

        return data
    }

    private fun activityToIntent(activity: String): Intent =
        Intent(Intent.ACTION_VIEW).setClassName(this.packageName, activity)
}
