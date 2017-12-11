package com.efemoney.maggg.data.model

import com.efemoney.maggg.ext.Json

data class ProductionCountry(@Json("name") val name: String,
                             @Json("iso_3166_1") val iso31661: String
)