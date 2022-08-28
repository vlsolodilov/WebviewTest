package com.example.webviewtest.screen.start

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.onesignal.OneSignal

class StartViewModel(application: Application) : AndroidViewModel(application) {

    private val _url = MutableLiveData<String?>()
    val url: LiveData<String?> = _url

    private val preferences = application.getSharedPreferences(PREF_MAME, Context.MODE_PRIVATE)

    init {
        getUrl()
        initOpenedPushHandler()
    }

    private fun getUrl() {
        _url.value = preferences.getString(URL, null)
    }

    fun initPushNotification() {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(getApplication())
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        OneSignal.promptForPushNotifications()
    }

    private fun initOpenedPushHandler() {
        OneSignal.setNotificationOpenedHandler { result ->
            val launchUrl = result.notification.launchURL
            launchUrl?.let {
                _url.value = launchUrl
                saveUrl(it)
            }
        }
    }

    private fun saveUrl(url: String) =
        with(preferences.edit()) {
            putString(URL, url)
            apply()
        }

    companion object {
        private const val PREF_MAME = "webviewtest"
        private const val URL = "url"
        private const val ONESIGNAL_APP_ID = "208a79e7-6f7d-456a-bde9-960dcef5f66a"
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StartViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return StartViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}

