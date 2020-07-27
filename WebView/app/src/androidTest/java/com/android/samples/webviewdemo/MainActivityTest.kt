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

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.WebView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.web.assertion.WebViewAssertions.webMatches
import androidx.test.espresso.web.model.Atoms.castOrDie
import androidx.test.espresso.web.model.Atoms.script
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.espresso.web.webdriver.DriverAtoms.findElement
import androidx.test.espresso.web.webdriver.DriverAtoms.getText
import androidx.test.espresso.web.webdriver.DriverAtoms.webClick
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Launch, interact, and verify conditions in an activity that has a WebView instance.
 */
@RunWith(AndroidJUnit4::class)


class MainActivityTest {

    val context = ApplicationProvider.getApplicationContext<Context>()

    @Rule
    @JvmField
    val mainActivityRule = ActivityTestRule(MainActivity::class.java)
    fun afterActivityLaunched() {
        // Technically we do not need to do this - MainActivity has javascript turned on.
        // Other WebViews in your app may have javascript turned off, however since the only way
        // to automate WebViews is through javascript, it must be enabled.
        onWebView().forceJavascriptEnabled()
    }

    // Test to check that the drop down menu behaves as expected
    @Test
    fun dropDownMenu_SanFran() {
        mainActivityRule.getActivity()
        onWebView()
            .withElement(findElement(Locator.ID, "location"))
            .perform(webClick()) // Similar to perform(click())
            .withElement(findElement(Locator.ID, "SF"))
            .perform(webClick()) // Similar to perform(click())
            .withElement(findElement(Locator.ID, "title"))
            .check(webMatches(getText(), containsString("San Francisco")))
    }

    // Test for checking createJsObject
    @Test
    fun jsObjectIsInjectedAndContainsPostMessage() {
        mainActivityRule.getActivity()
        onWebView()
            .check(
                webMatches(
                    script(
                        "return jsObject && jsObject.postMessage != null;",
                        castOrDie(Boolean::class.javaObjectType)
                    ),
                    `is`(true)
                )
            )
    }

    @Test
    fun valueInCallback_compareValueInput_returnsTrue() {
        mainActivityRule.activity
        // Setup
        val jsObjName = "jsObject"
        val allowedOriginRules = setOf<String>("https://example.com")
        val expectedMessage = "hello"
        // Get a handler that can be used to post to the main thread
        val mainHandler = Handler(Looper.getMainLooper());
        mainHandler.post {
            run {
                val webView = WebView(context)
                // Create JsObject
                createJsObject(
                    webView,
                    jsObjName,
                    allowedOriginRules
                ) { message -> //save message; call .set()
                }
                //Inject JsObject into Html
                webView.loadDataWithBaseURL(
                    "https://example.com", "<html></html>",
                    "text/html", null, null
                )
                //Call js code to invoke callback
                webView.evaluateJavascript("${jsObjName}.postMessage(`hello`)", null)
            }
        }
        // evaluate what comes out -> it should be hello
        // *Note: //.get() is a place holder
        assertEquals(
            expectedMessage
            , "//.get()"
        )
    }

    @Test
    // Checks that postMessage runs on the UI thread
    fun checkingThreadCallbackRunsOn() {
        mainActivityRule.activity
        // Setup
        val jsObjName = "jsObject"
        val allowedOriginRules = setOf<String>("https://example.com")
        // Get a handler that can be used to post to the main thread
        val mainHandler = Handler(Looper.getMainLooper())
        // Start Interacting with webView on UI thread
        mainHandler.post {
            run {
                val webView = WebView(context)
                // Create JsObject
                createJsObject(
                    webView,
                    jsObjName,
                    allowedOriginRules
                ) { message -> assertTrue(isUiThread()) }
                //Inject JsObject into Html
                webView.loadDataWithBaseURL(
                    "https://example.com", "<html></html>",
                    "text/html", "UTF-8", null
                )
                //Call js code to invoke callback
                webView.evaluateJavascript("${jsObjName}.postMessage(`hello`)", null)
            }
        }
    }

    /**
     * Returns true if the current thread is the UI thread based on the
     * Looper.
     */
    private fun isUiThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }
}