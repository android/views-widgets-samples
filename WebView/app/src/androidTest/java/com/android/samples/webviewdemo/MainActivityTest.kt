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

    @Rule @JvmField
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
                script("return jsObject && jsObject.postMessage != null;", castOrDie(Boolean::class.javaObjectType)),
                `is`(true)
            )
        )
    }

    @Test
    fun valueInCallback_compareValueInput_returnsTrue(){
        mainActivityRule.getActivity()

        // Setup
        val webView = WebView(context)
        val jsObjName = "jsObject"
        val allowedOriginRules = setOf<String>("https://example.com")

        // Create HTML
        val htmlPage = "<!DOCTYPE html><html><body>" + "    <script>" + "        myObject.postMessage('hello');" + "    </script>" + "</body></html>"

        // Create JsObject
        MainActivity.createJsObject(
            webView,
            jsObjName,
            allowedOriginRules
        ) { message -> MainActivity.invokeShareIntent(message) }

        //Inject JsObject into Html
        webView.loadData(htmlPage, "text/html", "UTF-8")


        //Call js code to invoke callback (in script tag of htmlPage)

        // evaluate what comes out -> it should be hello
        // *Note: "response from callback" is a place holder here I am unsure what should be placed there
        assertEquals("response from callback", "hello")

    }

    @Test
    // Checks that postMessage runs on the UI thread
    fun checkingThreadCallbackRunsOn() {
        mainActivityRule.getActivity()

        // Setup
        val webView = WebView(context)
        val jsObjName = "jsObject"
        val allowedOriginRules = setOf<String>("https://example.com")

        // Create HTML
        val htmlPage =
            "<!DOCTYPE html><html><body>" + "    <script>" + "        jsObject.postMessage('hello');" + "    </script>" + "</body></html>"


        // Create JsObject
        MainActivity.createJsObject(
            webView,
            jsObjName,
            allowedOriginRules
        ) { message -> MainActivity.invokeShareIntent(message) }


        // Inject JsObject into Html by loading it in the webview
        webView.loadData(htmlPage, "text/html", "UTF-8")


        // Use coroutine to go onto UI thread here?
        // Call js code to invoke callback (in script tag of htmlPage)


        // check that method is running on the UI thread
        assertTrue(isUiThread())
    }

    /**
     * Returns true if the current thread is the UI thread based on the
     * Looper.
     */
    private fun isUiThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }
}