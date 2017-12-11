package com.efemoney.maggg.ui.popular

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.efemoney.maggg.Navigator
import com.efemoney.maggg.data.Repository
import com.efemoney.maggg.data.model.BackdropPath
import com.efemoney.maggg.data.model.MovieOverview
import com.efemoney.maggg.data.model.Paged
import com.efemoney.maggg.data.model.PosterPath
import com.efemoney.maggg.ui.popular.PopularViewModel.PopularViewIntents.LoadInitialIntent
import com.efemoney.maggg.ui.popular.PopularViewModel.PopularViewIntents.LoadNextPageIntent
import com.efemoney.maggg.ui.popular.PopularViewModel.PopularViewState
import com.efemoney.maggg.ui.rx.TestRxSchedulers
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PopularViewModelTest {

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    private val testMovieList = listOf(
            MovieOverview(
                    id = 346364,
                    adult = false,
                    backdropPath = BackdropPath("/tcheoA2nPATCm2vvXw2hVQoaEFD.jpg"),
                    originalLanguage = "en",
                    originalTitle = "It",
                    overview = "In a small town in Maine, seven children known as The Losers Club come face to face with life problems, bullies and a monster that takes the shape of a clown called Pennywise.",
                    popularity = 994.18693,
                    posterPath = PosterPath("/9E2y5Q7WlCVNEhP5GiVTjhEhx1o.jpg"),
                    releaseDate = "2017-09-05",
                    title = "It",
                    video = false,
                    voteAverage = 7.2,
                    voteCount = 4380,
                    genreIds = listOf()
            )
    )

    private val repo: Repository = mock {
        on { popularMovies(1) } doReturn Observable.just(Paged(
                testMovieList,
                page = 1,
                totalPages = 2,
                totalResults = 2
        ))

        on { popularMovies(2) } doReturn Observable.just(Paged(
                testMovieList,
                page = 2,
                totalPages = 2,
                totalResults = 2
        ))
    }

    private val navigator: Navigator = mock()

    private lateinit var vm: PopularViewModel

    @Before
    fun setUp() {
        vm = PopularViewModel(repo, navigator, TestRxSchedulers())
    }

    @Test
    fun loadInitialThenLoadNext() {

        val observer = mock<Observer<PopularViewState>>()
        vm.state.observeForever(observer)

        vm.intents.accept(LoadInitialIntent)

        verify(observer).onChanged(PopularViewState(loading = true))
        verify(observer).onChanged(PopularViewState(loading = false, list = testMovieList))

        vm.intents.accept(LoadNextPageIntent)

        verify(observer).onChanged(PopularViewState(nextPageLoading = true, list = testMovieList))
        verify(observer).onChanged(PopularViewState(nextPageLoading = false, list = testMovieList + testMovieList))
    }
}
