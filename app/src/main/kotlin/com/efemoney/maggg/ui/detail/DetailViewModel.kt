package com.efemoney.maggg.ui.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.efemoney.maggg.data.Repository
import com.efemoney.maggg.data.model.Movie
import com.efemoney.maggg.rx.RxSchedulers
import com.efemoney.maggg.ui.detail.DetailViewModel.DetailViewIntents.LoadMovieIntent
import com.efemoney.maggg.ui.detail.DetailViewModel.DetailViewResults.LoadMovieResult
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import timber.log.Timber
import javax.inject.Inject

class DetailViewModel
@Inject constructor(private val repository: Repository,
                    private val schedulers: RxSchedulers) : ViewModel() {

    private val disposable: Disposable

    private val _state = MutableLiveData<DetailViewState>()
    private val _intents = PublishRelay.create<DetailViewIntents>()

    val state   : LiveData<DetailViewState>   get() = _state
    val intents : Consumer<DetailViewIntents> get() = _intents

    init {

        disposable = _intents.publish p@ { shared ->

            val merged = // Observable.merge<DetailViewResults>(
                    shared.ofType(LoadMovieIntent::class.java)
                            .doOnNext { Timber.tag("StateLogs").d("<--- Intent: load movie ") }
                            .flatMap(::loadMovie)
            // )

            return@p merged.scan(DetailViewState(), this::reduce)
                    .doOnNext { Timber.tag("StateLogs").d("---> State : loading=${it.loading}, error=${it.error}, movie=${if (it.movie == null) "null" else "Movie(...)" }, ") }

        }.observeOn(schedulers.main).subscribe(_state::setValue)
    }

    override fun onCleared() = disposable.dispose()

    private fun loadMovie(intent: LoadMovieIntent): Observable<LoadMovieResult> {

        return repository.movieDetails(intent.id)
                .subscribeOn(schedulers.network)
                .observeOn(schedulers.main)
                .map { LoadMovieResult(movie = it) }
                .startWith(LoadMovieResult(loading = true))
                .onErrorReturn { LoadMovieResult(error = it) }
    }

    private fun reduce(previous: DetailViewState, next: DetailViewResults): DetailViewState {

        return when (next) {
            is LoadMovieResult -> {
                when {
                    next.loading -> previous.copy(loading = true)
                    next.error != null -> previous.copy(loading = false, error = next.error)
                    else -> previous.copy(loading = false, error = null, movie = next.movie)
                }
            }
        }
    }

    sealed class DetailViewIntents {

        data class LoadMovieIntent(val id: Int) : DetailViewIntents()
    }

    private sealed class DetailViewResults {

        data class LoadMovieResult(val loading: Boolean = false,
                                   val error: Throwable? = null,
                                   val movie: Movie? = null) : DetailViewResults()
    }

    data class DetailViewState(
            val loading: Boolean = false,
            val error: Throwable? = null,
            val movie: Movie? = null
    )
}