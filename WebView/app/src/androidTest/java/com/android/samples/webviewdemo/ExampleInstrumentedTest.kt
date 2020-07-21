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
import androidx.test.espresso.web.assertion.WebViewAssertions.webMatches
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.espresso.web.webdriver.DriverAtoms.findElement
import androidx.test.espresso.web.webdriver.DriverAtoms.getText
import androidx.test.espresso.web.webdriver.DriverAtoms.webClick
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Rule @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java)

     fun afterActivityLaunched() {
//         Technically we do not need to do this - MainActivity has javascript turned on.
//         Other WebViews in your app may have javascript turned off, however since the only way
//         to automate WebViews is through javascript, it must be enabled.
        onWebView().forceJavascriptEnabled()
    }


    @Test
    fun webViewTest() {

        activityRule.getActivity()


        onWebView()
            .withElement(findElement(Locator.ID, "title"))
            .check(webMatches(getText(), containsString("New York")))
//            .perform(webClick())
//            .withElement(findElement(Locator.TAG_NAME, "h1"))
//            .check(webMatches(getText(), containsString("Apple")))


    }


// Test for calling postMessage
    @Test
    fun callPostMessage() {
        onWebView()
                // Click on the share button
            .withElement(findElement(Locator.ID, "share")) // similar to onView(withId(...))
            .perform(webClick()) // Similar to perform(click())

            // check that an intent was created

            // verify that the data send to post message looks correct

            //.get()
            //.value


            // Similar to check(matches(...))
            //.check(webMatches(executeScript)

    }
}
