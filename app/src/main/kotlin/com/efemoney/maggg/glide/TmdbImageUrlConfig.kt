package com.efemoney.maggg.glide

import android.content.SharedPreferences
import android.util.SparseArray
import com.efemoney.maggg.data.model.TmdbImagePath
import com.efemoney.maggg.data.remote.Config
import com.efemoney.maggg.data.remote.ImageConfig
import com.efemoney.maggg.data.remote.TmdbApi
import com.efemoney.maggg.inject.qualifier.ImageConfigPref
import com.efemoney.maggg.rx.RxSchedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit.DAYS
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class TmdbImageUrlConfig
@Inject constructor(private val api: TmdbApi,
                    private val schedulers: RxSchedulers,
                    @ImageConfigPref private val prefs: SharedPreferences) {

    private var config = ImageConfig(
            baseUrl = prefs.getString("config.base", DEFAULT_URL),
            posterSizes = prefs.getStringSet("config.poster", setOf()).toList(),
            backdropSizes = prefs.getStringSet("config.backdrop", setOf()).toList()
    )

    // caches
    private val p = SparseArray<String>(2)
    private val b = SparseArray<String>(2)

    init {
        updateConfigIfNecessary()
    }

    private fun updateConfigIfNecessary() {

        val now = System.currentTimeMillis()
        val threeDays = DAYS.toMillis(3) // update every 3 days
        val lastRetrieved = prefs.getLong("config.retrieved", 0)

        if (now - lastRetrieved < threeDays) return

        val ignored = api.config()
                .subscribeOn(schedulers.network)
                .subscribe(::updateConfig)
    }

    private fun updateConfig(c: Config) {

        config = c.imageConfig

        // clear caches
        p.clear()
        b.clear()

        // Persist
        prefs.edit()
                .putString("config.base", config.baseUrl)
                .putStringSet("config.poster", config.posterSizes.toSet())
                .putStringSet("config.backdrop", config.backdropSizes.toSet())
                .putLong("config.retrieved", System.currentTimeMillis())
                .apply()
    }

    fun url(model: TmdbImagePath, width: Int): String {

        return when(model.type) {
            "poster" -> posterUrl(model.path, width)
            "backdrop" -> backdropUrl(model.path, width)
            else -> {
                config.baseUrl + DEFAULT_SIZE + model.path
            }
        }
    }

    private fun posterUrl(path: String, width: Int) = kotlin.run {

        val size = p.get(width) ?: kotlin.run {
            p.put(width, findBestSize(width, config.posterSizes) ?: DEFAULT_SIZE)
            p.get(width)
        }

        Timber.d("PosterImageRequest: $width -> $size from ${config.posterSizes}")

        config.baseUrl + size + path
    }

    private fun backdropUrl(path: String, width: Int) = kotlin.run {

        val size = p.get(width) ?: kotlin.run {
            p.put(width, findBestSize(width, config.posterSizes) ?: DEFAULT_SIZE)
            p.get(width)
        }

        Timber.d("BackdropImageRequest: $width -> $size from ${config.backdropSizes}")

        config.baseUrl + size + path
    }

    private fun findBestSize(width: Int, list: List<String>): String? {

        return when {

            list.isEmpty() -> null

            else -> {

                val index = list.asSequence() // sequences!
                        .map { it.substringAfter('w', missingDelimiterValue = "") } // remove 'w'
                        .map { it.toIntOrNull() } // Convert to int or null
                        .map { it?.minus(width) } // map to 'deviation' from required width
                        .mapIndexed { i, v -> i to v } // capture index and deviation
                        .sortedBy { abs(it.second ?: Int.MAX_VALUE) } // sort by abs(deviation) smallest to largest
                        .take(2) // take first two items
                        .minBy { it.second?.plus(width) ?: Int.MAX_VALUE } // pick whichever will result in a smaller image download
                        ?.first // get its index

                list[index ?: list.size - 1] // fallback to last size
            }
        }
    }

    companion object {
        @JvmField val DEFAULT_URL = "http://image.tmdb.org/t/p/"
        @JvmField val DEFAULT_SIZE = "original"
    }
}