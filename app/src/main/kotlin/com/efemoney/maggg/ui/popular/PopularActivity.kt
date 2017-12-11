package com.efemoney.maggg.ui.popular

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.util.DiffUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.efemoney.maggg.R
import com.efemoney.maggg.data.model.MovieOverview
import com.efemoney.maggg.databinding.ActivityPopularBinding
import com.efemoney.maggg.databinding.ItemLoadingBinding
import com.efemoney.maggg.databinding.ItemPopularBinding
import com.efemoney.maggg.ext.*
import com.efemoney.maggg.glide.GlideApp
import com.efemoney.maggg.rx.RxSchedulers
import com.efemoney.maggg.ui.base.BaseActivity
import com.efemoney.maggg.ui.popular.PopularViewModel.PopularViewIntents.*
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent
import com.jakewharton.rxbinding2.support.v7.widget.scrollEvents
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class PopularActivity : BaseActivity() {

    lateinit var binding: ActivityPopularBinding

    @Inject lateinit var schedulers: RxSchedulers
    @Inject lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: PopularViewModel

    private lateinit var adapter: PopularAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_popular)

        setSupportActionBar(binding.toolbar)

        viewModel = viewModelGetUsing(factory)

        adapter = PopularAdapter(this)

        setupView()
        bindView()
        bindViewModel()
    }

    private fun setupView() {

        val gridSize = resources.getInteger(R.integer.grid_span_count)

        val glm = GridLayoutManager(this, gridSize)
        glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(pos: Int): Int = if (adapter.item(pos) is LoadingItem) gridSize else 1
        }

        binding.grid.adapter = adapter
        binding.grid.layoutManager = glm
    }

    private fun bindView() {

        viewModel.state.observe(this, Observer {

            val state = it ?: throw NullPointerException("Received null state")

            when {
                state.loading -> {
                    binding.grid.hide()
                    binding.progress.show()
                    binding.emptyContent.hide()
                }

                state.error != null -> {
                    binding.grid.hide()
                    binding.progress.hide()
                    binding.emptyContent.show()

                    binding.emptyContent.text = state.error.message ?: string(R.string.error)
                }

                else -> {
                    binding.grid.show()
                    binding.progress.hide()
                    binding.emptyContent.hide()

                    val list: MutableList<PopularItem> = state.list.toMutableList() // copy

                    if (state.nextPageLoading)
                        list += LoadingItem

                    if (state.nextPageError != null)
                        binding.root.snackbar(state.nextPageError.message ?: string(R.string.error), Snackbar.LENGTH_LONG)

                    adapter.itemsRelay.accept(list)
                }
            }
        })
    }

    private fun bindViewModel() {

        disposables += Observable
                .just(LoadInitialIntent)
                .subscribe(viewModel.intents)

        disposables += binding.grid.scrollEvents()
                .throttleFirst(100, MILLISECONDS)
                .filter(::isScrollingUpwards)
                .filter(::adapterNotLoading)
                .filter(::loadMoreThresholdReached)
                .map { LoadNextPageIntent }
                .subscribe(viewModel.intents)
    }

    // dy is +ve when scrolling up
    private fun isScrollingUpwards(event: RecyclerViewScrollEvent): Boolean = event.dy() > 0

    private fun adapterNotLoading(ignored: Any) = adapter.items.lastOrNull() !is LoadingItem

    private fun loadMoreThresholdReached(ignored: Any): Boolean {

        val glm = (binding.grid.layoutManager as GridLayoutManager)
        val loadMoreThreshold = 2 * glm.spanCount
        val lastVisibleItem = glm.findLastVisibleItemPosition()
        val itemCount = glm.itemCount

        return lastVisibleItem + loadMoreThreshold >= itemCount
    }

    inner class PopularAdapter(context: Context) : RecyclerView.Adapter<ViewHolder<*, PopularItem>>() {

        private val inflater = LayoutInflater.from(context)

        val itemsRelay = PublishRelay.create<List<PopularItem>>()

        init {

            val initial = listOf<PopularItem>() to (null as DiffUtil.DiffResult?)

            disposables += itemsRelay
                    .observeOn(schedulers.computation)
                    .scan(initial) { (old, diff), new ->
                        // return a pair of new list to diff result of old list with new
                        new to DiffUtil.calculateDiff(PopularItemDiffCallback(old, new))
                    }
                    .observeOn(schedulers.main)
                    .subscribe {
                        items = it.first
                        it.second?.dispatchUpdatesTo(this)
                    }
        }

        var items = listOf<PopularItem>()

        fun item(position: Int) = items[position]

        override fun getItemId(position: Int): Long {
            val item = items[position]

            return when (item) {
                is MovieOverview -> item.id.toLong()
                is LoadingItem -> -1L
                else -> throw IllegalArgumentException("")
            }
        }

        override fun getItemViewType(position: Int): Int = when (item(position)) {
            is MovieOverview -> 0
            is LoadingItem -> 1
            else -> throw IllegalStateException("Invalid item type ${item(position)::class.java.simpleName}")
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: ViewHolder<*, PopularItem>, position: Int) {

            holder.bind(item(position))
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder<*, PopularItem> {

            return when (viewType) {
                0 -> {
                    ViewHolder.Popular(
                            this@PopularActivity,
                            ItemPopularBinding.inflate(inflater, parent, false)
                    ) as ViewHolder<*, PopularItem>
                }
                1 -> {
                    ViewHolder.Loading(
                            ItemLoadingBinding.inflate(inflater, parent, false)
                    ) as ViewHolder<*, PopularItem>
                }
                else -> throw IllegalStateException("Unknown view type $viewType")
            }
        }
    }

    sealed class ViewHolder<V : ViewDataBinding, in Data : PopularItem>(binding: V)
        : RecyclerView.ViewHolder(binding.root) {

        abstract fun bind(data: Data)

        class Popular(private val activity: PopularActivity,
                      private val binding: ItemPopularBinding)
            : ViewHolder<ItemPopularBinding, MovieOverview>(binding) {

            private lateinit var data: MovieOverview

            init {
                activity.disposables += binding.root.clicks()
                        .debounce(150, MILLISECONDS)
                        .map { MovieClickedIntent(data.id) }
                        .subscribe(activity.viewModel.intents)
            }

            override fun bind(data: MovieOverview) {
                this.data = data

                if (data.posterPath == null)
                    GlideApp.with(activity)
                            .clear(binding.poster)
                else
                    GlideApp.with(activity)
                            .load(data.posterPath)
                            .transition(withCrossFade())
                            .into(binding.poster)

                binding.executePendingBindings()
            }
        }

        class Loading(binding: ItemLoadingBinding)
            : ViewHolder<ItemLoadingBinding, PopularActivity.LoadingItem>(binding) {

            override fun bind(data: PopularActivity.LoadingItem) = Unit
        }
    }

    interface PopularItem

    object LoadingItem
        : PopularItem

    class PopularItemDiffCallback(val old: List<PopularItem>,
                                  val new: List<PopularItem>) : DiffUtil.Callback() {

        override fun getOldListSize() = old.size

        override fun getNewListSize() = new.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oi = old[oldItemPosition]
            val ni = new[newItemPosition]
            return oi == ni || (oi is MovieOverview && ni is MovieOverview && oi.id == ni.id)
        }

        // Hardcoded for simplicity. If items are same, contents are same too
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true
    }
}
