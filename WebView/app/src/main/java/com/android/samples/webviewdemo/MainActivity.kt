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
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.webkit.JavaScriptReplyProxy
import androidx.webkit.WebMessageCompat
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

    /** Instantiate the interface and set the context  */
    class WebAppInterface(private val mContext: Context) {
        /** Send a message from the web page  */
        @JavascriptInterface
        fun sendMessage(message: String) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(mContext, shareIntent, null)
        }

    }

    /** Instantiate the Listener  */
    class MyListener() : WebViewCompat.WebMessageListener {
        override fun onPostMessage(
            view: WebView,
            message: WebMessageCompat,
            sourceOrigin: Uri,
            isMainFrame: Boolean,
            replyProxy: JavaScriptReplyProxy
        ) {
            replyProxy.postMessage("Got it!")
            Log.i("grcoleman", "received onPostMessage in app")
        }
    }

    /** Instantiate a class to determine which JS-Java API to use based on the version the application is running on */
    class ApiVersion() {
        fun determineVersion(webview : WebView , context: Context) {
            //Checks to see if the API the applicatoin is running on is high enough to support the new API
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // The JavaScript object will be injected in any frame whose origin matches one in the list created below.
                // We call the list rules because this is a set of allowed origin rules
                val rules = setOf<String>("https://gcoleman799.github.io")

                // Adds myListener to webview and injects a JavaScript object into each frame that myListener will listen on
                if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) WebViewCompat.addWebMessageListener(
                    webview,
                    "myObject",
                    rules,
                    MyListener()
                )
            } else {
            //Falls back to JavascriptInterface if WebMessageListener is not available
            webview.addJavascriptInterface(WebAppInterface(context), "myObject") }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        //create JS object to be injected into frames
        val myObject = ApiVersion().determineVersion(binding.webview, this)

        // Load the content
        binding.webview.loadUrl("https://gcoleman799.github.io/Asset-Loader/assets/index.html")
    }
}