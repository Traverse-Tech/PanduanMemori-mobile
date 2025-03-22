package com.traverse.panduanmemori.util

import com.traverse.panduanmemori.BuildConfig

object AssetUtil {
    private const val BASE_URL = "${BuildConfig.CDN_URL}/assets"

    fun getAssetUrl(assetName: String): String {
        return "$BASE_URL/$assetName"
    }
}
