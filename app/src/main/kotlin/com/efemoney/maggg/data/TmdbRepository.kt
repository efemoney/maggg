package com.efemoney.maggg.data

import com.efemoney.maggg.data.model.Movie
import com.efemoney.maggg.data.model.MovieOverview
import com.efemoney.maggg.data.model.Paged
import com.efemoney.maggg.data.remote.TmdbApi
import io.reactivex.Observable
import javax.inject.Inject

class TmdbRepository
@Inject constructor(private val api: TmdbApi): Repository {

    override fun popularMovies(page: Int): Observable<Paged<MovieOverview>> = api.popularMovies(page).toObservable()

    override fun movieDetails(id: Int): Observable<Movie> = api.movieDetails(id).toObservable()
}