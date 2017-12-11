package com.efemoney.maggg.data.model

import com.efemoney.maggg.ext.Json

data class SpokenLanguage(@Json("iso_639_1") val iso6391: String,
                          @Json("name") val name: String
)