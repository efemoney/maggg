package com.efemoney.maggg.ext

import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

fun <T> RequestBuilder<T>.onResource(
        failed: () -> Unit = {},
        loaded: () -> Unit
): RequestBuilder<T> {

    return listener(object : RequestListener<T> {

        override fun onLoadFailed(e: GlideException?,
                                  model: Any?,
                                  target: Target<T>?,
                                  isFirstResource: Boolean): Boolean {
            failed()
            return false
        }

        override fun onResourceReady(resource: T,
                                     model: Any?,
                                     target: Target<T>?,
                                     dataSource: DataSource?,
                                     isFirstResource: Boolean): Boolean {
            loaded()
            return false
        }
    })
}

inline fun <reified Model, reified Data> MultiModelLoaderFactory.build() = build(Model::class.java, Data::class.java)
