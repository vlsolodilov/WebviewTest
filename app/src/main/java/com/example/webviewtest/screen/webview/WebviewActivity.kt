package com.example.webviewtest.screen.webview

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import com.example.webviewtest.databinding.ActivityWebviewBinding
import com.example.webviewtest.util.PickPhotoActivityContract

class WebviewActivity : AppCompatActivity() {

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

        val url = intent.extras?.let { WebviewActivityArgs.fromBundle(it).url }

        url?.let { startWebView(it) }
    }

    private fun startWebView(url: String) {
        binding.webView.apply {
            settings.allowContentAccess = true
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    view?.loadUrl(url)
                    return true
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

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}