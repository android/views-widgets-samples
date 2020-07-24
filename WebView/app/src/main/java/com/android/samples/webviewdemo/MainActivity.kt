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

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.webkit.JavaScriptReplyProxy
import androidx.webkit.WebMessageCompat
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import androidx.webkit.WebViewCompat
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val jsObjName = "jsObject"
        val allowedOriginRules = setOf<String>("https://gcoleman799.github.io")

        // Check if the system is set to light or dark mode
        val nightModeFlag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlag == Configuration.UI_MODE_NIGHT_YES) {
            // Switch WebView to dark mode; uses default dark theme
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                WebSettingsCompat.setForceDark(
                    binding.webview.settings,
                    WebSettingsCompat.FORCE_DARK_ON
                )
            }
            // TODO: set darkening strategy and use custom CSS for dark theme
        }

        // Configure asset loader with custom domain
        // * NOTE THAT *:
        // The assets path handler is set with the sub path /views-widgets-samples/ here because we are tyring to ensure
        // that the address loaded with loadUrl("https://raw.githubusercontent.com/views-widgets-samples/assets/index.html") does
        // not conflict with a real web address. In this case, if the path were only /assests/ we would need to load
        // "https://raw.githubusercontent.com/assets/index.html" in order to access our local index.html file.
        // However we cannot guarantee "https://raw.githubusercontent.com/assets/index.html" is not a valid web address.
        // Therefore we must let the AssetLoader know to expect the /views-widgets-samples/ sub path as well as the /assets/.
        val assetLoader = WebViewAssetLoader.Builder()
            .setDomain("raw.githubusercontent.com")
            .addPathHandler("/views-widgets-samples/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .addPathHandler("/views-widgets-samples/res/", WebViewAssetLoader.ResourcesPathHandler(this))
            .build()

        // Set clients
        binding.webview.webViewClient = MyWebViewClient(assetLoader)

        // Set Title
        title = getString(R.string.app_name)

        // Setup debugging; See https://developers.google.com/web/tools/chrome-devtools/remote-debugging/webviews for reference
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
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