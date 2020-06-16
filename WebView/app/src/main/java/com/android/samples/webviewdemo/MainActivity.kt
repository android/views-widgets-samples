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
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.webkit.*
import androidx.webkit.R
import com.android.samples.webviewdemo.databinding.ActivityMainBinding
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.FileInputStream

class MainActivity : AppCompatActivity() {

    //Creating the custom WebView Client Class
    private class MyWebViewClient(private val assetLoader: WebViewAssetLoader) :
        WebViewClientCompat() {


        fun createResponse(request: WebResourceRequest): WebResourceResponse {
            val path = request.url.path
            val file = request.url.lastPathSegment
            if (file.isNullOrBlank()) {
                return WebResourceResponse(
                    "text/plain", "utf-8", 404, "Not Found", emptyMap(),
                    ByteArrayInputStream(ByteArray(0))
                )
            }
            return WebResourceResponse(
                "text/plain", "utf-8", 200, "OK", emptyMap(),
                FileInputStream(file)
            )
        }

        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {

            return assetLoader.shouldInterceptRequest(request.getUrl());

            //return createResponse(request)

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //configure the asset loader with default domain and path for res and assets
        //with the asset loader we are just resetting the
        val assetLoader = WebViewAssetLoader.Builder()
            .setDomain("gcoleman799.github.io")
            .addPathHandler("/Asset-Loader/", WebViewAssetLoader.AssetsPathHandler(this))
            .addPathHandler("/res/", WebViewAssetLoader.ResourcesPathHandler(this))
            .build();

        //Set clients
        binding.webview.webViewClient = MyWebViewClient(assetLoader)

        //Set Title
        title = "WebViewWeather"

        //Enable Javascript
        binding.webview.settings.javaScriptEnabled = true

        binding.webview.addJavascriptInterface(WebAppInterface(this), "Weather")

        binding.webview.loadUrl("https://gcoleman799.github.io/Asset-Loader/index.html")


//        val (port1, port2) = androidx.webkit.WebViewCompat.createWebMessageChannel(binding.webview)
//        port1.setWebMessageCallback(object : WebMessagePortCompat.WebMessageCallbackCompat() {
//            //de serialize the data you get in
//            override fun onMessage(port: WebMessagePortCompat, message: WebMessageCompat?) {
//                message?.data?.also {
//                    val data = JSONObject(it)
//                    //handleWebEvent(data)
//                }
//            }
//        })
//
//
//        //send the second port over to the javascript side
//
//        val initMsg = WebMessageCompat("""{type: "init"}""", arrayOf(port2))
//
//        //send a message to the webview with the message above and the origin that this message relates to (you can pass a star if you are only loading local content)
//        WebViewCompat.postWebMessage(binding.webview, initMsg, Uri.parse("*"))


    }


    //allow users to navigate back to a previous page
    override fun onBackPressed() {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (binding.webview.canGoBack()) {
            binding.webview.goBack()
        } else {
            super.onBackPressed()
        }


    }


}
