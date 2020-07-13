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
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.content.ContextCompat.startActivity
import androidx.webkit.JavaScriptReplyProxy
import androidx.webkit.WebMessageCompat
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebSettingsCompat.DARK_STRATEGY_USER_AGENT_DARKENING_ONLY
import androidx.webkit.WebSettingsCompat.DARK_STRATEGY_WEB_THEME_DARKENING_ONLY
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import com.android.samples.webviewdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Create a handler that runs on the UI thread
    private val handler: Handler = Handler(Looper.getMainLooper())

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

    /**
     * Injects a JavaScript object which supports a {@code postMessage()} method.
     * A feature check is used to determine if the preferred API, WebMessageListener, is supported.
     * If it is, then WebMessageListener will be used to create a JavaScript object. The object will be
     * injected into all of the frames that have an origin matching those in {@code allowedOriginRules}.
     * <p>
     * If WebMessageListener is not supported then the method will defer to using JavascriptInterface
     * to create the JavaScript object.
     * <p>
     * The {@code postMessage()} methods in the Javascript objects created by WebMessageListener and
     * JavascriptInterface both make calls to the same callback, {@code onMessageReceived()}.
     * In this case, the callback invokes native Android sharing.
     * <p>
     * The WebMessageListener invokes callbacks on the UI thread by default. However,
     * JavascriptInterface invokes callbacks on a background thread by default. In order to
     * guarantee thread safety and that the caller always gets consistent behavior the the callback
     * should always be called on the UI thread. To change the default behavior of JavascriptInterface,
     * the callback is wrapped in a handler which will tell it to run on the UI thread instead of the default
     * background thread it would otherwise be invoked on.
     * <p>
     * @param webview the component that WebMessageListener or JavascriptInterface will be added to
     * @param jsObjName the name that will be given to the Javascript objects created by either
     *        WebMessageListener or JavascriptInterface
     * @param allowedOriginRules a set of origins used only by WebMessageListener, if a frame matches an
     * origin in this set then it will have the JS object injected into it
     * @param onMessageReceived invoked on UI thread with message passed in from JavaScript postMessage() call
     */
    private fun createJsObject(
        webview: WebView,
        jsObjName: String,
        allowedOriginRules: Set<String>,
        onMessageReceived: (message: String) -> Unit
    ) {
        if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) {
            WebViewCompat.addWebMessageListener(
                webview, jsObjName, allowedOriginRules,
                object : WebViewCompat.WebMessageListener {
                    override fun onPostMessage(
                        webview: WebView,
                        message: WebMessageCompat,
                        sourceOrigin: Uri,
                        isMainFrame: Boolean,
                        replyProxy: JavaScriptReplyProxy
                    ) {
                        onMessageReceived(message.data!!)
                    }
                })
        } else {
            webview.addJavascriptInterface(object {
                @JavascriptInterface
                fun postMessage(message: String) {
                    // Use the handler to invoke method on UI thread
                    handler.post(Runnable { onMessageReceived(message) })
                }
            }, jsObjName)
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
        val nightModeFlag1 = resources.configuration.uiMode
        val nightModeFlag2 = Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlag1.and(nightModeFlag2) == Configuration.UI_MODE_NIGHT_YES) {
            // Switch WebView to dark mode; uses default dark theme
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                WebSettingsCompat.setForceDark(
                    binding.webview.settings,
                    WebSettingsCompat.FORCE_DARK_ON
                )
            }
        }

        // Configure asset loader with custom domain
        // * NOTE THAT *:
        // The assets path handler is set with the sub path /Asset-Loader/ here because we are tyring to ensure
        // that the address loaded with loadUrl("https://gcoleman799.github.io/Asset-Loader/assets/index.html") does
        // not conflict with a real web address. In this case, if the path were only /assests/ we would need to load
        // "https://gcoleman799.github.io/assets/index.html" in order to access our local index.html file.
        // However we cannot guarantee "https://gcoleman799.github.io/assets/index.html" is not a valid web address.
        // Therefore we must let the AssetLoader know to expect the /Asset-Loader/ sub path as well as the /assets/.
        val assetLoader = WebViewAssetLoader.Builder()
            .setDomain("gcoleman799.github.io")
            .addPathHandler("/Asset-Loader/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .addPathHandler("/res/", WebViewAssetLoader.ResourcesPathHandler(this))
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
        binding.webview.loadUrl("https://gcoleman799.github.io/Asset-Loader/assets/index.html")
    }
}