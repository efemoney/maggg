package com.efemoney.maggg.ext

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.arch.persistence.room.ColumnInfo
import android.content.Context
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.content.res.AppCompatResources
import android.widget.Toast
import com.google.gson.annotations.SerializedName
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

typealias Room = ColumnInfo
typealias NoWildcards = JvmSuppressWildcards
typealias Json = SerializedName

inline fun <reified T: ViewModel> FragmentActivity.viewModelGet(): T
        = ViewModelProviders.of(this).get(T::class.java)

inline fun <reified T: ViewModel> FragmentActivity.viewModelGetUsing(factory: ViewModelProvider.Factory): T
        = ViewModelProviders.of(this, factory).get(T::class.java)

inline fun <reified T: ViewModel> Fragment.viewModelGet(): T
        = ViewModelProviders.of(this).get(T::class.java)

inline fun <reified T: ViewModel> Fragment.viewModelGetUsing(factory: ViewModelProvider.Factory): T
        = ViewModelProviders.of(this, factory).get(T::class.java)

operator fun CompositeDisposable.plusAssign(d: Disposable) { add(d) }

fun isAtLeastApi(level: Int): Boolean = Build.VERSION.SDK_INT >= level


fun Context.dpToPx(dp: Int) = (dpToPxFloat(dp) + 0.5f).toInt()

fun Context.dpToPxFloat(dp: Int) = dp * resources.displayMetrics.density

fun Context.spToPx(sp: Int) = (spToPxFloat(sp) + 0.5f).toInt()

fun Context.spToPxFloat(sp: Int) = sp * resources.displayMetrics.scaledDensity


fun Context.string(@StringRes id: Int): String = getString(id)

fun Context.string(@StringRes id: Int, vararg args: Any): String = getString(id, args)

fun Fragment.string(@StringRes id: Int): String = getString(id)

fun Context.color(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun Fragment.color(@ColorRes id: Int) = context?.color(id)

fun Context.drawable(@DrawableRes id: Int) = AppCompatResources.getDrawable(this, id)

fun Fragment.drawable(@DrawableRes id: Int) = context?.drawable(id)


fun Context.toast(msg: CharSequence?, duration: Int) = Toast.makeText(this, msg, duration).show()

fun toastShort(context: Context, msg: CharSequence?) = toastWith(context, msg, Toast.LENGTH_LONG)

fun toastLong(context: Context, msg: CharSequence?) = toastWith(context, msg, Toast.LENGTH_SHORT)

fun toastWith(context: Context, msg: CharSequence?, duration: Int) = context.toast(msg, duration)
