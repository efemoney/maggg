package com.efemoney.maggg.data.remote

import com.efemoney.maggg.ext.Json

data class Config(@Json("images") val imageConfig: ImageConfig)

data class ImageConfig(
        @Json("base_url") val baseUrl: String,
        @Json("poster_sizes") val posterSizes: List<String>,
        @Json("backdrop_sizes") val backdropSizes: List<String>
)
