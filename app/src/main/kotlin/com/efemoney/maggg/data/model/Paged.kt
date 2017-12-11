package com.efemoney.maggg.data.model

import com.efemoney.maggg.ext.Json

data class Paged<out Item>(
        @Json("results") val results: List<Item>,
        @Json("page") val page: Int,
        @Json("total_pages") val totalPages: Int,
        @Json("total_results") val totalResults: Int
)