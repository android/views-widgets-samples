/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.android.samples.webviewdemo

import org.junit.Assert.assertThat
import org.junit.Test
import java.util.Objects

/**
 * Tests to ensure that callbacks are behaving as expected
 */


class EmailValidatorTest {
    @Test
    fun testConvertFahrenheitToCelsius() {
        val actual: Float = ConverterUtil.convertCelsiusToFahrenheit(100)
        // expected value is 212
        val expected = 212f
        // use this method because float is not precise
       // assertEquals("Conversion from celsius to fahrenheit failed", expected, actual, 0.001)
    }
    @Test
    fun postMessage_CorrectStringFormat_ReturnsTrue() {
        // create object
                // what object do we need to create?
                // we need to create a js object to call postMessage on
        
        val jsObject: Objects =

            // create expected return
        // call post message
        // check that post message

       // assertEquals("Hi", jsObject.postMessage("Hi"))
    }
}
