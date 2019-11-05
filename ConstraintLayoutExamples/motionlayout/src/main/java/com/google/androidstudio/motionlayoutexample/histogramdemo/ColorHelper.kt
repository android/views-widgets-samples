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
