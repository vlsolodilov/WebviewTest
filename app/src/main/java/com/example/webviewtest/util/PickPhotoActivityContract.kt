package com.example.webviewtest.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

class PickPhotoActivityContract : ActivityResultContract<String, Uri?>() {

    override fun createIntent(context: Context, input: String) =
        Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).apply { type = input }

    override fun parseResult(resultCode: Int, intent: Intent?) =
        intent?.data?.takeIf { resultCode == Activity.RESULT_OK }
}