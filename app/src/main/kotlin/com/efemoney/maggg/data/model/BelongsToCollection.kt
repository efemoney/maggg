package com.efemoney.maggg.data.model

import com.efemoney.maggg.ext.Json

data class BelongsToCollection(@Json("id") val id: Int,
                               @Json("name") val name: String,
                               @Json("poster_path") val posterPath: String,
                               @Json("backdrop_path") val backdropPath: String
)