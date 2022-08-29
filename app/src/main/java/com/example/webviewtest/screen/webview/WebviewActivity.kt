package com.example.webviewtest.screen.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import com.example.webviewtest.MainActivity
import com.example.webviewtest.databinding.ActivityWebviewBinding
import com.example.webviewtest.util.PickPhotoActivityContract
import com.onesignal.OneSignal

class WebviewActivity : AppCompatActivity() {

    companion object {
        private const val BASE_URL = "https://slotdevapp.site/Gj26bD9k"
        private const val ONESIGNAL_APP_ID = "208a79e7-6f7d-456a-bde9-960dcef5f66a"
    }

    private var _binding: ActivityWebviewBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("ActivityWebviewBinding is null")

    private var uploadMessage: ValueCallback<Array<Uri>>? = null

    private val pickPhotoLauncher = registerForActivityResult(PickPhotoActivityContract()) { uri ->
        uri?.let {
            uploadMessage?.onReceiveValue(arrayOf(it))
        }
        uploadMessage = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPushNotification()
        startWebView(BASE_URL)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun startWebView(url: String) {
        binding.webView.apply {
            settings.allowContentAccess = true
            settings.javaScriptEnabled = true
            addJavascriptInterface(this@WebviewActivity, "jsInterface")
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    view?.loadUrl(url)
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    view?.loadUrl("javascript: var count = document.body.childNodes; jsInterface.childCount(count.length);")
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    uploadMessage?.onReceiveValue(null)
                    uploadMessage = filePathCallback
                    pickPhotoLauncher.launch("image/*")
                    return true
                }
            }
            CookieManager.getInstance().setAcceptThirdPartyCookies(this,true)
            loadUrl(url)
        }
    }

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    fun childCount(count: Int) {
        if (count <= 1) {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun initPushNotification() {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        OneSignal.promptForPushNotifications()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}