/*
* Copyright 2014 The Android Open Source Project
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
package com.example.android.cardview;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Unit tests for CardView samples.
 */
public class SampleTests extends ActivityInstrumentationTestCase2<CardViewActivity> {

    private CardViewActivity mActivity;
    private CardViewFragment mFragment;

    public SampleTests() {
        super(CardViewActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mFragment = (CardViewFragment) mActivity.getFragmentManager().findFragmentById(R.id
                .container);
    }

    public void testPreconditions() {
        assertNotNull(String.format("%s is null", CardViewActivity.class.getSimpleName()),
                mActivity);
        assertNotNull(String.format("%s is null", CardViewFragment.class.getSimpleName()),
                mFragment);
        assertNotNull("SeekBar for Radius is null", mFragment.mRadiusSeekBar);
        assertNotNull("SeekBar for Elevation is null", mFragment.mElevationSeekBar);
    }

    public void testRadiusSeekbarChangesRadiusOfCardView() {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                float radius = 50.0f;
                mFragment.mRadiusSeekBar.setProgress((int) radius);
                assertEquals(radius, mFragment.mCardView.getRadius());
            }
        });
    }

    public void testElevationSeekbarChangesElevationOfCardView() {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                float elevation = 40.0f;
                mFragment.mElevationSeekBar.setProgress((int) elevation);
                assertEquals(elevation, mFragment.mCardView.getElevation());
            }
        });
    }
}