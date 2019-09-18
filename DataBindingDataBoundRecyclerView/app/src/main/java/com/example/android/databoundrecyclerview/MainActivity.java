/*
 * Copyright (C) 2016 The Android Open Source Project
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

package com.example.android.databoundrecyclerview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.android.databoundrecyclerview.databinding.ActivityMainBinding;
import com.example.android.databoundrecyclerview.databinding.CityItemBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        City[] cities = {new City("Istanbul"),
                new City("Barcelona"),
                new City("London"),
                new City("San Francisco")};
        ActionCallback actionCallback = new ActionCallback() {
            @Override
            public void onClick(City city) {
                city.setFavorite(!city.isFavorite());
            }

            @Override
            public void onClick(State state) {
                state.setPopulation(state.getPopulation() + 1);
            }
        };
        CityAdapter adapter = new CityAdapter(actionCallback, cities);
        binding.cityList.setAdapter(adapter);
        MixedAdapter mixedAdapter = new MixedAdapter(actionCallback,
                cities);
        mixedAdapter.addItem(0, new State("Kenya", 47));
        mixedAdapter.addItem(1, new State("United States", 323));
        mixedAdapter.addItem(2, new State("Brazil", 206));
        binding.mixedList.setAdapter(mixedAdapter);
    }

    /**
     * This is an example of a data bound adapter use case where all items have the same type.
     * <p>
     * The parent class handles the item creation and this child class only implements the
     * bindItem to set values in a type checked way.
     */
    private static class CityAdapter extends DataBoundAdapter<CityItemBinding> {
        List<City> mCityList = new ArrayList<>();
        private ActionCallback mActionCallback;

        public CityAdapter(ActionCallback actionCallback, City... cities) {
            super(R.layout.city_item);
            mActionCallback = actionCallback;
            Collections.addAll(mCityList, cities);
        }

        @Override
        protected void bindItem(DataBoundViewHolder<CityItemBinding> holder, int position,
                                List<Object> payloads) {
            holder.binding.setData(mCityList.get(position));
            holder.binding.setCallback(mActionCallback);
        }

        @Override
        public int getItemCount() {
            return mCityList.size();
        }
    }

    /**
     * This is an example of an adapter that has a convention on its items where:
     * <ul>
     * <li> Each item type is represented with a different layout file
     * <li> Each layout file name their main object "data"
     * <li> Each layout file can receive extra parameters. This one only uses "callback".
     * </ul>
     * <p>
     * This kind of adapter usage might be very useful to simplify lists that have too many
     * different item types. Also, see the similarity between <code>city_item.xml</code> and
     * <code>state_item.xml</code>.
     */
    private static class MixedAdapter extends MultiTypeDataBoundAdapter {
        private ActionCallback mActionCallback;

        public MixedAdapter(ActionCallback actionCallback, Object... items) {
            super(items);
            mActionCallback = actionCallback;
        }

        @Override
        protected void bindItem(DataBoundViewHolder holder, int position, List payloads) {
            super.bindItem(holder, position, payloads);
            // this will work even if the layout does not have a callback parameter
            holder.binding.setVariable(BR.callback, mActionCallback);
        }

        @Override
        public int getItemLayoutId(int position) {
            // use layout ids as types
            Object item = getItem(position);
            if (item instanceof City) {
                return R.layout.city_item;
            }
            if (item instanceof State) {
                return R.layout.state_item;
            }
            throw new IllegalArgumentException("unknown item type " + item);
        }
    }
}
