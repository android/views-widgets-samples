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

package com.example.android.interpolatorplayground;

/**
 * Callback used to update the UI when path control points are
 * manipulated by the user.
 */
public abstract class ControlPointCallback {

    abstract void onControlPoint1Moved(float cx1, float cy1);
    abstract void onControlPoint2Moved(float cx2, float cy2);
}
