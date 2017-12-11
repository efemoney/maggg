package com.efemoney.maggg.ui.detail

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.efemoney.maggg.data.Repository
import com.efemoney.maggg.data.model.BackdropPath
import com.efemoney.maggg.data.model.Movie
import com.efemoney.maggg.data.model.PosterPath
import com.efemoney.maggg.ui.detail.DetailViewModel.DetailViewIntents.LoadMovieIntent
import com.efemoney.maggg.ui.detail.DetailViewModel.DetailViewState
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
import kotlin.LazyThreadSafetyMode.NONE

@RunWith(JUnit4::class)
class DetailViewModelTest {

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    private val testMovie: Movie by lazy(NONE) {

        Movie(
                id = 346364,
                adult = false,
                backdropPath = BackdropPath("/tcheoA2nPATCm2vvXw2hVQoaEFD.jpg"),
                belongsToCollection = null,
                budget = 35000000,
                genres = listOf(),
                homepage = "http://itthemovie.com/",
                imdbId = "tt1396484",
                originalLanguage = "en",
                originalTitle = "It",
                overview = "In a small town in Maine, seven children known as The Losers Club come face to face with life problems, bullies and a monster that takes the shape of a clown called Pennywise.",
                popularity = 994.18693,
                posterPath = PosterPath("/9E2y5Q7WlCVNEhP5GiVTjhEhx1o.jpg"),
                productionCompanies = listOf(),
                productionCountries = listOf(),
                releaseDate = "2017-09-05",
                revenue = 555575232,
                runtime = 135,
                spokenLanguages = listOf(),
                status = "Released",
                tagline = "Your fears are unleashed",
                title = "It",
                video = false,
                voteAverage = 7.2,
                voteCount = 4380
        )
    }
    private val testMovieId = 346364

    private val repo: Repository = mock {
        on { movieDetails(testMovieId) } doReturn Observable.just(testMovie)
    }

    private lateinit var vm: DetailViewModel

    @Before
    fun setUp() {
        vm = DetailViewModel(repo, TestRxSchedulers())
    }

    @Test
    fun loadMovie() {

        val observer = mock<Observer<DetailViewState>>()
        vm.state.observeForever(observer)

        vm.intents.accept(LoadMovieIntent(testMovieId))

        verify(observer).onChanged(DetailViewState(loading = true))
        verify(observer).onChanged(DetailViewState(loading = false, movie = testMovie))
    }
}
