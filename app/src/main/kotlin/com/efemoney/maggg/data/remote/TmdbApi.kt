package com.efemoney.maggg.data.remote

import com.efemoney.maggg.data.model.Movie
import com.efemoney.maggg.data.model.MovieOverview
import com.efemoney.maggg.data.model.Paged
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("movie/popular")
    fun popularMovies(@Query("page") page: Int): Single<Paged<MovieOverview>>

    @GET("movie/{id}")
    fun movieDetails(@Path("id") id: Int): Single<Movie>

    @GET("configuration")
    fun config(): Single<Config>

    companion object { const val URL = "https://api.themoviedb.org/3/" }
}
