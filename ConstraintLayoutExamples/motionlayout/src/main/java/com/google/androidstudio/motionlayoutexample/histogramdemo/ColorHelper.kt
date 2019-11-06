/*
 * Copyright (C) 2019 The Android Open Source Project
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

package com.google.androidstudio.motionlayoutexample.histogramdemo

import android.graphics.Color
import androidx.annotation.ColorInt

class ColorHelper {
    companion object {
        private const val GRADIENT = 20

        /**
         * Returns a different color somewhat gradient.
         */
        @JvmStatic
        fun getNextColor(@ColorInt color: Int): Int {
            return Color.argb(
                    alpha(color),
                    (red(color) + GRADIENT) % 256,
                    (green(color) + GRADIENT) % 256,
                    (blue(color) + GRADIENT) % 256)
        }
        /**
         * Returns a different color somewhat contrasting.
         */
        @JvmStatic
        fun getContrastColor(@ColorInt color: Int): Int {
            return Color.argb(
                    alpha(color),
                    255 - red(color),
                    255 - green(color),
                    255 - blue(color)
            )
        }

        private fun alpha(@ColorInt color: Int): Int {
            return color shr 24 and 0xff
        }

        private fun red(@ColorInt color: Int): Int {
            return color shr 16 and 0xff
        }

        private fun green(@ColorInt color: Int): Int {
            return color shr 8 and 0xff
        }

        private fun blue(@ColorInt color: Int): Int {
            return color and 0xff
        }
    }
}
