package com.efemoney.maggg.inject.module

import android.content.Context
import android.content.SharedPreferences
import com.efemoney.maggg.MagggApp
import com.efemoney.maggg.Navigator
import com.efemoney.maggg.inject.qualifier.ImageConfigPref
import com.efemoney.maggg.ui.detail.DetailActivity
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun context(app: MagggApp): Context = app

    @Provides
    @Singleton
    @ImageConfigPref
    fun pref(context: Context): SharedPreferences = context.getSharedPreferences("tmdb_config", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun navigator(context: Context): Navigator {

        return object : Navigator {
            override fun showDetails(id: Int) {
                context.startActivity(DetailActivity.createIntent(context, id))
            }
        }
    }
}
