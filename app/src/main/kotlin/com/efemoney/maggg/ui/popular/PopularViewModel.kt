package com.efemoney.maggg.ui.popular

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.efemoney.maggg.Navigator
import com.efemoney.maggg.data.Repository
import com.efemoney.maggg.data.model.MovieOverview
import com.efemoney.maggg.data.model.Paged
import com.efemoney.maggg.rx.RxSchedulers
import com.efemoney.maggg.ui.popular.PopularViewModel.PopularViewIntents.*
import com.efemoney.maggg.ui.popular.PopularViewModel.PopularViewResults.*
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import timber.log.Timber
import javax.inject.Inject

class PopularViewModel
@Inject constructor(private val repository: Repository,
                    private val navigator: Navigator,
                    private val schedulers: RxSchedulers) : ViewModel() {

    private val disposable: Disposable

    private val _state = MutableLiveData<PopularViewState>()
    private val _intents = PublishRelay.create<PopularViewIntents>()

    val state: LiveData<PopularViewState> get() = _state
    val intents: Consumer<PopularViewIntents> get() = _intents

    /* */

    private var page: Int = 1
    private var total: Int = 1

    init {
        disposable = _intents.publish p@ { shared ->

            val merged = Observable.merge<PopularViewResults>(

                    shared.ofType(LoadInitialIntent::class.java)
                            .doOnNext { Timber.tag("StateLogs").d("<--- Intent: load init") }
                            .flatMap(::loadInitial),

                    shared.ofType(LoadNextPageIntent::class.java)
                            .doOnNext { Timber.tag("StateLogs").d("<--- Intent: load next page (${page + 1})") }
                            .flatMap(::loadNextPage),

                    shared.ofType(MovieClickedIntent::class.java)
                            .doOnNext { Timber.tag("StateLogs").d("<--- Intent: click movie(${it.id})") }
                            .flatMap(::movieClicked)
            )

            return@p merged.scan(PopularViewState(), this::reduce)
                    .distinctUntilChanged()
                    .doOnNext { Timber.tag("StateLogs").d("---> State : loading=${it.loading}, error=${it.error}, npLoading=${it.nextPageLoading}, npError=${it.nextPageError}, list=[${if (it.list.isNotEmpty()) it.list.size.toString() else ""}]") }

        }.observeOn(schedulers.main).subscribe(_state::setValue)
    }

    override fun onCleared() = disposable.dispose()

    private fun loadInitial(ignored: LoadInitialIntent): Observable<LoadInitialResult> {

        return repository.popularMovies(1)
                .subscribeOn(schedulers.network)
                .map { LoadInitialResult(paged = it) }
                .startWith(LoadInitialResult(loading = true))
                .onErrorReturn { LoadInitialResult(error = it) }
    }

    private fun loadNextPage(ignored: LoadNextPageIntent): Observable<LoadNextPageResult> {

        val nextPageIsLoading = _state.value!!.nextPageLoading
        val pageIsLast = page == total

        return when {
            nextPageIsLoading || pageIsLast -> // if already loading or exhausted pages
                Observable.just(LoadNextPageResult(wontLoad = true))

            else -> repository.popularMovies(page + 1)
                    .subscribeOn(schedulers.network)
                    .map { LoadNextPageResult(paged = it) }
                    .startWith(LoadNextPageResult(loading = true))
                    .onErrorReturn { LoadNextPageResult(error = it) }
        }

    }

    private fun movieClicked(intent: MovieClickedIntent): Observable<MovieClickedResult> {
        navigator.showDetails(intent.id)
        return Observable.just(MovieClickedResult)
    }

    private fun reduce(previous: PopularViewState, next: PopularViewResults): PopularViewState {

        return when (next) {

            is LoadInitialResult -> {
                when {
                    next.loading -> previous.copy(loading = true, error = null)
                    next.error != null -> previous.copy(loading = false, error = next.error)
                    else -> {
                        val paged = next.paged!!
                        page = paged.page
                        total = paged.totalPages
                        previous.copy(loading = false, error = null, list = paged.results)
                    }
                }
            }

            is LoadNextPageResult -> {
                when {
                    next.wontLoad -> previous // state remains same
                    next.loading -> previous.copy(nextPageLoading = true, nextPageError = null)
                    next.error != null -> previous.copy(nextPageLoading = false, nextPageError = next.error)
                    else -> {
                        val paged = next.paged!!
                        page = paged.page
                        total = paged.totalPages
                        previous.copy(nextPageLoading = false, nextPageError = null, list = previous.list + next.paged.results)
                    }
                }
            }

            is MovieClickedResult -> previous
        }
    }

    sealed class PopularViewIntents {

        object LoadInitialIntent : PopularViewIntents()

        object LoadNextPageIntent : PopularViewIntents()

        data class MovieClickedIntent(val id: Int) : PopularViewIntents()
    }

    private sealed class PopularViewResults {

        data class LoadInitialResult(val loading: Boolean = false,
                                     val error: Throwable? = null,
                                     val paged: Paged<MovieOverview>? = null) : PopularViewResults()

        data class LoadNextPageResult(val wontLoad: Boolean = false,
                                      val loading: Boolean = false,
                                      val error: Throwable? = null,
                                      val paged: Paged<MovieOverview>? = null) : PopularViewResults()

        object MovieClickedResult : PopularViewResults()
    }

    data class PopularViewState(
            val loading: Boolean = false,
            val error: Throwable? = null,
            val nextPageLoading: Boolean = false,
            val nextPageError: Throwable? = null,
            val list: List<MovieOverview> = listOf()
    )
}