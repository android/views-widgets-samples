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
import android.os.Looper
import android.webkit.WebView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.web.assertion.WebViewAssertions.webMatches
import androidx.test.espresso.web.model.Atoms.castOrDie
import androidx.test.espresso.web.model.Atoms.script
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.espresso.web.webdriver.DriverAtoms.*
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.containsString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Launch, interact, and verify conditions in an activity that has a WebView instance.
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
class MainActivityTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Rule
    @JvmField
    val mainActivityRule = ActivityTestRule(MainActivity::class.java)

    // Test to check that the drop down menu behaves as expected
    @Test
    fun dropDownMenu_SanFran() {
        onWebView()
            .forceJavascriptEnabled()
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
        onWebView()
            .check(
                webMatches(
                    script(
                        "return jsObject && typeof jsObject.postMessage == \"function\"",
                        castOrDie(Boolean::class.javaObjectType)
                    ),
                    `is`(true)
                )
            )
    }

    @Test
    fun valueInCallback_compareValueInput() = runBlocking {
        // Setup
        val jsObjName = "jsObject"
        val allowedOriginRules = setOf("https://example.com")
        val expectedMessage = "hello"
        val onMessageReceived = CompletableDeferred<String?>()
        launch(Dispatchers.Main) {
            val webView = WebView(context).apply {
                settings.javaScriptEnabled = true
            }
            // Create & inject JsObject
            createJsObject(
                webView,
                jsObjName,
                allowedOriginRules
            ) { message ->
                onMessageReceived.complete(message)
            }
            webView.loadDataWithBaseURL(
                "https://example.com",
                "<html><script>${jsObjName}.postMessage('${expectedMessage}')</script></html>",
                "text/html",
                null,
                null
            )
        }
        // evaluate argument being passed into onMessageReceived
        assertEquals(expectedMessage, onMessageReceived.await())
    }

    @Test
    fun checkingThreadCallbackRunsOn() = runBlocking {
        // Setup
        val jsObjName = "jsObject"
        val allowedOriginRules = setOf("https://example.com")
        val expectedMessage = "hello"
        val onMessageReceived = CompletableDeferred<Looper?>()
        launch(Dispatchers.Main) {
            val webView = WebView(context).apply {
                settings.javaScriptEnabled = true
            }
            // Create & inject JsObject
            createJsObject(
                webView,
                jsObjName,
                allowedOriginRules
            ) {
                onMessageReceived.complete(Looper.myLooper())
            }
            webView.loadDataWithBaseURL(
                "https://example.com",
                "<html><script>${jsObjName}.postMessage('${expectedMessage}')</script></html>",
                "text/html",
                null,
                null
            )
        }
        assertEquals(onMessageReceived.await(), Looper.getMainLooper())
    }
}