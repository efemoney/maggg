package com.efemoney.maggg.glide

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.efemoney.maggg.BuildConfig
import com.efemoney.maggg.MagggApp
import com.efemoney.maggg.data.model.TmdbImagePath
import java.io.InputStream

@GlideModule
@Excludes(OkHttpLibraryGlideModule::class) // Exclude the default Okhttp module
class MagggAppGlideModule : AppGlideModule() {

    override fun isManifestParsingEnabled(): Boolean = false

    override fun applyOptions(context: Context, builder: GlideBuilder) {

        builder.setLogLevel(if (BuildConfig.DEBUG) Log.DEBUG else Log.ERROR) // log more in debug
        builder.setDefaultRequestOptions(RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)  // cache all
                .centerCrop() // center crop, we are loading smaller images so this makes sense
        )
        builder.setDefaultTransitionOptions(Drawable::class.java, withCrossFade())
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {

        // https://github.com/bumptech/glide/issues/2002
        val component = (context.applicationContext as MagggApp).component

        // Register a custom okhttp loader that logs request urls in debug
        registry.replace(
                GlideUrl::class.java,
                InputStream::class.java,
                OkHttpUrlLoader.Factory(component.glideClient()))

        // Register a model loader for tmdb image paths
        registry.replace(
                TmdbImagePath::class.java,
                InputStream::class.java,
                TmdbImageModelLoader.Factory(component.imageUrlConfig()))
    }
}