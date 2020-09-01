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

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebSettingsCompat.DARK_STRATEGY_PREFER_WEB_THEME_OVER_USER_AGENT_DARKENING
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import androidx.webkit.WebViewFeature
import com.android.samples.webviewdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Creating the custom WebView Client Class
    private class MyWebViewClient(private val assetLoader: WebViewAssetLoader) :
        WebViewClientCompat() {
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(request.url)
        }
    }

    // Invokes native android sharing
    private fun invokeShareIntent(message: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(this@MainActivity, shareIntent, null)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val jsObjName = "jsObject"
        val allowedOriginRules = setOf("https://raw.githubusercontent.com")

        // Configuring Dark Theme
        // *NOTE* : The force dark setting is not persistent. You must call the static
        // method every time your app process is started.
        // *NOTE* : The change from day<->night mode is a
        // configuration change so by default the activity will be restarted
        // (and pickup the new values to apply the theme). Take care when overriding this
        //  default behavior to ensure this method is still called when changes are made.
        val nightModeFlag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        // Check if the system is set to light or dark mode
        if (nightModeFlag == Configuration.UI_MODE_NIGHT_YES) {
            // Switch WebView to dark mode; uses default dark theme
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                WebSettingsCompat.setForceDark(
                    binding.webview.settings,
                    WebSettingsCompat.FORCE_DARK_ON
                )
            }

            /* Set how WebView content should be darkened. There are three options for how to darken
             * a WebView.
             * PREFER_WEB_THEME_OVER_USER_AGENT_DARKENING- checks for the "color-scheme" <meta> tag.
             * If present, it uses media queries. If absent, it applies user-agent (automatic)
             * darkening DARK_STRATEGY_WEB_THEME_DARKENING_ONLY - uses media queries always, even
             * if there's no "color-scheme" <meta> tag present.
             * DARK_STRATEGY_USER_AGENT_DARKENING_ONLY - it ignores web page theme and always
             * applies user-agent (automatic) darkening.
             * More information about Force Dark Strategy can be found here:
             * https://developer.android.com/reference/androidx/webkit/WebSettingsCompat#setForceDarkStrategy(android.webkit.WebSettings,%20int)
             */
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK_STRATEGY)) {
                WebSettingsCompat.setForceDarkStrategy(
                    binding.webview.settings,
                    DARK_STRATEGY_PREFER_WEB_THEME_OVER_USER_AGENT_DARKENING
                )
            }
        }

        // Configure asset loader with custom domain
        // *NOTE* :
        // The assets path handler is set with the sub path /views-widgets-samples/ here because we
        // are tyring to ensure that the address loaded with
        // loadUrl("https://raw.githubusercontent.com/views-widgets-samples/assets/index.html") does
        // not conflict with a real web address. In this case, if the path were only /assests/ we
        // would need to load "https://raw.githubusercontent.com/assets/index.html" in order to
        // access our local index.html file.
        // However we cannot guarantee "https://raw.githubusercontent.com/assets/index.html" is not
        // a valid web address. Therefore we must let the AssetLoader know to expect the
        // /views-widgets-samples/ sub path as well as the /assets/.
        val assetLoader = WebViewAssetLoader.Builder()
            .setDomain("raw.githubusercontent.com")
            .addPathHandler(
                "/views-widgets-samples/assets/",
                WebViewAssetLoader.AssetsPathHandler(this)
            )
            .addPathHandler(
                "/views-widgets-samples/res/",
                WebViewAssetLoader.ResourcesPathHandler(this)
            )
            .build()

        // Set clients
        binding.webview.webViewClient = MyWebViewClient(assetLoader)

        // Set Title
        title = getString(R.string.app_name)

        // Setup debugging; See https://developers.google.com/web/tools/chrome-devtools/remote-debugging/webviews for reference
        if (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        // Enable Javascript
        binding.webview.settings.javaScriptEnabled = true

        // Create a JS object to be injected into frames; Determines if WebMessageListener
        // or WebAppInterface should be used
        createJsObject(
            binding.webview,
            jsObjName,
            allowedOriginRules
        ) { message -> invokeShareIntent(message) }

        // Load the content
        binding.webview.loadUrl("https://raw.githubusercontent.com/views-widgets-samples/assets/index.html")
    }
}