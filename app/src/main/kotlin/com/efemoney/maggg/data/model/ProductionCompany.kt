package com.efemoney.maggg.data.model

import com.efemoney.maggg.ext.Json

data class ProductionCompany(@Json("id") val id: Int,
                             @Json("name") val name: String
)