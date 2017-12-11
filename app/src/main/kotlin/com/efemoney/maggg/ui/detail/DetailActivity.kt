package com.efemoney.maggg.ui.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.efemoney.maggg.R
import com.efemoney.maggg.databinding.ActivityDetailBinding
import com.efemoney.maggg.ext.*
import com.efemoney.maggg.glide.GlideApp
import com.efemoney.maggg.ui.base.BaseActivity
import com.efemoney.maggg.ui.detail.DetailViewModel.DetailViewIntents.LoadMovieIntent
import io.reactivex.Observable
import javax.inject.Inject

class DetailActivity : BaseActivity() {

    lateinit var binding: ActivityDetailBinding

    @Inject lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val id = intent.getIntExtra(EXTRA_MOVIE_ID, -1)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel = viewModelGetUsing(factory)

        setupView()
        bindView()
        bindViewModel(id)
    }

    private fun setupView() {

    }

    private fun bindView() {

        viewModel.state.observe(this, Observer {

            val state = it ?: throw NullPointerException("Received null state")

            when {
                state.loading -> {
                    binding.progress.show()
                    binding.emptyContent.hide()
                }

                state.error != null -> {
                    binding.progress.hide()
                    binding.emptyContent.show()

                    binding.emptyContent.text = state.error.message ?: string(R.string.error)
                }

                else -> {
                    val movie = state.movie ?: return@Observer

                    binding.progress.hide()
                    binding.emptyContent.hide()

                    val rm = GlideApp.with(this)
                    movie.posterPath?.let { rm.load(it).into(binding.poster) }
                    movie.backdropPath?.let { rm.load(it).into(binding.backdrop) }

                    binding.title.text = getString(R.string.format_movie_title, movie.title, movie.releaseDate.take(4))
                    binding.genres.text = movie.genres.joinToString { it.name.capitalize() }

                    binding.rating.text = movie.voteAverage.toString()
                    binding.rating.drawableLeft(R.drawable.ic_star)

                    binding.overview.text = movie.overview

                    when {
                        movie.tagline.isNullOrBlank() -> binding.tagline.hide()
                        else -> binding.tagline.text = getString(R.string.format_tagline, movie.tagline) // if not null
                    }
                }
            }
        })
    }

    private fun bindViewModel(id: Int) {

        disposables += Observable
                .just(LoadMovieIntent(id))
                .subscribe(viewModel.intents)
    }

    companion object {

        val EXTRA_MOVIE_ID = "id"

        fun createIntent(context: Context, id: Int) = Intent(context, DetailActivity::class.java)
                .apply { putExtra(EXTRA_MOVIE_ID, id) }
    }
}
