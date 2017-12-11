package com.efemoney.maggg.ui.base

import android.annotation.SuppressLint
import android.view.MenuItem
import com.efemoney.maggg.MagggApp
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable

@SuppressLint("Registered")
open class BaseActivity : DaggerAppCompatActivity() {

    protected val disposables = CompositeDisposable()

    internal val app
        get() = application as MagggApp

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}
