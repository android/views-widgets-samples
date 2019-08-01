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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.androidstudio.motionlayoutexample.R

class ListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    companion object {
        fun newInstance() = ListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        Log.i(ListFragment::class.java.simpleName, "onCreateView, container is $container")
        return inflater.inflate(R.layout.motion_22_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.list)

        recyclerView.layoutManager = LinearLayoutManager(
                context, RecyclerView.VERTICAL, false)
        val users = ArrayList<User>()
        users.add(User("Paul", "Mr"))
        users.add(User("Jane", "Miss"))
        users.add(User("John", "Dr"))
        users.add(User("Amy", "Mrs"))
        users.add(User("Paul", "Mr"))
        users.add(User("Jane", "Miss"))
        users.add(User("John", "Dr"))
        users.add(User("Amy", "Mrs"))
        users.add(User("Paul", "Mr"))
        users.add(User("Jane", "Miss"))
        users.add(User("John", "Dr"))
        users.add(User("Amy", "Mrs"))
        users.add(User("Paul", "Mr"))
        users.add(User("Jane", "Miss"))
        users.add(User("John", "Dr"))
        users.add(User("Amy", "Mrs"))

        val adapter = CustomAdapter(users)
        recyclerView.adapter = adapter

        super.onViewCreated(view, savedInstanceState)
    }
}
