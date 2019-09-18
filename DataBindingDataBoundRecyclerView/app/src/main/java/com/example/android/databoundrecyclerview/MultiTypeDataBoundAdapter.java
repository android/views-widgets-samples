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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An abstraction over {@link BaseDataBoundAdapter} that keeps the list of children and can
 * support multiple item types.
 * <p>
 * This class relies on all layouts using the "data" variable to represent the item.
 * <p>
 * Although this class by itself just exists for demonstration purposes, it might be a useful idea
 * for an application to have a generic naming convention for their items to scale lists with
 * many view types.
 * <p>
 * Note that, by using this, you lose the compile time type checking for mapping your items to
 * layout files but at run time, it will still be checked. See
 * {@link androidx.databinding.ViewDataBinding#setVariable(int, Object)} for details.
 */
abstract public class MultiTypeDataBoundAdapter extends BaseDataBoundAdapter {
    private List<Object> mItems = new ArrayList<>();

    public MultiTypeDataBoundAdapter(Object... items) {
        Collections.addAll(mItems, items);
    }

    @Override
    protected void bindItem(DataBoundViewHolder holder, int position, List payloads) {
        holder.binding.setVariable(BR.data, mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Object getItem(int position) {
        return mItems.get(position);
    }

    public void addItem(Object item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void addItem(int position, Object item) {
        mItems.add(position, item);
        notifyItemInserted(position);
    }
}
