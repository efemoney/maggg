package com.efemoney.maggg.inject

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.efemoney.maggg.ext.NoWildcards
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class DaggerViewModelFactory
@Inject constructor(private val creators: @NoWildcards Map<Class<out ViewModel>, Provider<ViewModel>>)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        var creator: Provider<out ViewModel>? = creators[modelClass]

        if (creator == null) for ((key, value) in creators) if (modelClass.isAssignableFrom(key)) {
            creator = value
            break
        }

        if (creator == null) throw IllegalArgumentException("unknown model class " + modelClass)

        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
