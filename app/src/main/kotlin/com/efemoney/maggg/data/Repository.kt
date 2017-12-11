package com.efemoney.maggg.data

import com.efemoney.maggg.data.model.Movie
import com.efemoney.maggg.data.model.MovieOverview
import com.efemoney.maggg.data.model.Paged
import io.reactivex.Observable

interface Repository {

    fun popularMovies(page: Int): Observable<Paged<MovieOverview>>

    fun movieDetails(id: Int): Observable<Movie>
}