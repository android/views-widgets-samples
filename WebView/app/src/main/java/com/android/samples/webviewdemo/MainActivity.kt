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
import androidx.webkit.*
import com.android.samples.webviewdemo.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    //Creating the custom WebViewClient Class
    private class MyWebViewClient(private val assetLoader: WebViewAssetLoader) :
        WebViewClientCompat() {
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(request.getUrl());
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

    class MyListener() : WebViewCompat.WebMessageListener {
        override fun onPostMessage(
            view: WebView,
            message: WebMessageCompat,
            sourceOrigin: Uri,
            isMainFrame: Boolean,
            replyProxy: JavaScriptReplyProxy
        ) {
            // do something about view, message, sourceOrigin and isMainFrame.
            replyProxy.postMessage("Got it!")
            android.util.Log.i("grcoleman", "received onPostMessage in app")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure asset loader with custom domain
        val assetLoader = WebViewAssetLoader.Builder()
            .setDomain("gcoleman799.github.io")
            .addPathHandler("/Asset-Loader/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .addPathHandler("/res/", WebViewAssetLoader.ResourcesPathHandler(this))
            .build();

        //Set clients
        binding.webview.webViewClient = MyWebViewClient(assetLoader)

        //Set Title
        title = "WebView Weather"

        //Setup debugging
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
        }
        //Enable Javascript
        binding.webview.settings.javaScriptEnabled = true

        //Connect to Javascript Interface
        binding.webview.addJavascriptInterface(WebAppInterface(this), "Weather")

        // The JavaScript object will be injected in any frame whose origin matches one in the list created below.
        // We call the list rules because this is a set of allowed origin rules
        val rules = setOf<String>("https://gcoleman799.github.io" )

        // Adds myListener to webview and injects a JavaScript object into each frame that myListener will listen on
//        Log.i ("INFO", "Made it 1")
//        if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) Log.i ("INFO", "Made it 2")
//        if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) WebViewCompat.addWebMessageListener(binding.webview, "myObject", rules, MyListener() )

        //Load the content
        binding.webview.loadUrl("https://gcoleman799.github.io/Asset-Loader/assets/index.html")
    }
}