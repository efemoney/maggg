package com.efemoney.maggg.data.model

import com.efemoney.maggg.ext.Json

data class Movie(
        @Json("id") val id: Int,
        @Json("imdb_id") val imdbId: String?,
        @Json("title") val title: String,
        @Json("tagline") val tagline: String?,
        @Json("overview") val overview: String?,
        @Json("homepage") val homepage: String?,
        @Json("popularity") val popularity: Double,
        @Json("release_date") val releaseDate: String,

        @Json("budget") val budget: Int,
        @Json("revenue") val revenue: Int,

        @Json("status") val status: String,
        @Json("runtime") val runtime: Int?,
        @Json("vote_count") val voteCount: Int,
        @Json("vote_average") val voteAverage: Double,
        @Json("belongs_to_collection") val belongsToCollection: BelongsToCollection?,

        @Json("poster_path") val posterPath: PosterPath?,
        @Json("backdrop_path") val backdropPath: BackdropPath?,

        @Json("original_title") val originalTitle: String,
        @Json("original_language") val originalLanguage: String,
        @Json("spoken_languages") val spokenLanguages: List<SpokenLanguage>,
        @Json("production_companies") val productionCompanies: List<ProductionCompany>,
        @Json("production_countries") val productionCountries: List<ProductionCountry>,
        @Json("genres") val genres: List<Genre>,
        @Json("adult") val adult: Boolean,
        @Json("video") val video: Boolean
)