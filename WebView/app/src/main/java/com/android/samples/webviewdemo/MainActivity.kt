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
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
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
import java.lang.invoke.LambdaConversionException

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

    private fun createJsObject(
        webview: WebView,
        mContext: Context,
        jsObjName: String,
        rules: Set<String>,
        sendAndroidMessage: (message: String, mContext: Context) -> Unit
    ) {
        if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) {
            WebViewCompat.addWebMessageListener(
                webview, jsObjName, rules,
                object : WebViewCompat.WebMessageListener {
                    override fun onPostMessage(
                        webview: WebView,
                        message: WebMessageCompat,
                        sourceOrigin: Uri,
                        isMainFrame: Boolean,
                        replyProxy: JavaScriptReplyProxy
                    ) {
                        sendAndroidMessage(message.data, mContext)
                    }
                })
        } else {
            webview.addJavascriptInterface(object {
                @JavascriptInterface
                fun postMessage(message: String?) {
                    sendAndroidMessage(message, mContext)
                }
            }, jsObjName)
        }
    }

    // Invokes native android sharing
    val sendAndroidMessage = { message: String?, mContext: Context ->
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(mContext, shareIntent, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val jsObjName = "jsObject"
        val rules = setOf<String>("https://gcoleman799.github.io")


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
        createJsObject(binding.webview, this, jsObjName, rules, sendAndroidMessage)

        // Load the content
        binding.webview.loadUrl("https://gcoleman799.github.io/Asset-Loader/assets/index.html")
    }
}