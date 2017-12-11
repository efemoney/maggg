package com.efemoney.maggg.data.model

import com.efemoney.maggg.ext.Json
import com.efemoney.maggg.ui.popular.PopularActivity

data class MovieOverview(
        @Json("id") val id: Int,
        @Json("title") val title: String,
        @Json("overview") val overview: String?,
        @Json("popularity") val popularity: Double,
        @Json("release_date") val releaseDate: String,
        @Json("vote_count") val voteCount: Int,
        @Json("vote_average") val voteAverage: Double,
        @Json("original_title") val originalTitle: String,
        @Json("original_language") val originalLanguage: String,
        @Json("poster_path") val posterPath: PosterPath?,
        @Json("backdrop_path") val backdropPath: BackdropPath?,
        @Json("genre_ids") val genreIds: List<Int>,
        @Json("adult") val adult: Boolean,
        @Json("video") val video: Boolean

): PopularActivity.PopularItem