package com.efemoney.maggg.glide

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import com.efemoney.maggg.data.model.TmdbImagePath
import com.efemoney.maggg.ext.build
import java.io.InputStream

class TmdbImageModelLoader(
        val config: TmdbImageUrlConfig,
        delegate: ModelLoader<GlideUrl, InputStream>
) : BaseGlideUrlLoader<TmdbImagePath>(delegate) {

    override fun handles(model: TmdbImagePath): Boolean = true

    override fun getUrl(model: TmdbImagePath, w: Int, h: Int, opt: Options) = config.url(model, w)

    class Factory(val config: TmdbImageUrlConfig)
        : ModelLoaderFactory<TmdbImagePath, InputStream> {

        override fun build(factory: MultiModelLoaderFactory): TmdbImageModelLoader {

            return TmdbImageModelLoader(config, delegate = factory.build<GlideUrl, InputStream>())
        }

        override fun teardown() = Unit
    }
}
