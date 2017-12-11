package com.efemoney.maggg.ext

import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewCompat
import android.support.v7.content.res.AppCompatResources
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView

fun View.doOnLayout(onLayout: (View) -> Boolean) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {

        override fun onLayoutChange(view: View, left: Int, top: Int, right: Int, bottom: Int,
                                    oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {

            if (onLayout(view)) view.removeOnLayoutChangeListener(this)
        }
    })
}

fun View.doWhenLaidOut(a: (View) -> Unit) {

    if (ViewCompat.isLaidOut(this))
        a(this)
    else doOnLayout {
        a(it)
        false
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

val View.isShowing
    get() = visibility == View.VISIBLE

fun View.snackbar(msg: CharSequence, length: Int) {
    Snackbar.make(this, msg, length).show()
}

fun View.enableIf(predicate: Boolean) = if (predicate) enable() else disable()

fun View.showIf(predicate: Boolean) = if (predicate) show() else hide()

fun View.centerX() = (left + right) / 2f

fun View.centerY() = (top + bottom) / 2f

fun showAll(vararg views: View) = views.forEach { it.show() }

fun hideAll(vararg views: View) = views.forEach { it.hide() }


fun TextView.setTextColorRes(@ColorRes resId: Int) = setTextColor(context.color(resId))

fun TextView.onTextChange(action: () -> Unit) = addTextChangedListener(object : TextWatcher {
    override fun afterTextChanged(p0: Editable?) = action()
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
})

fun TextView.drawableLeft(@DrawableRes resId: Int)
        = drawableLeft(AppCompatResources.getDrawable(context, resId))

fun TextView.drawableLeft(drawable: Drawable?) = compoundDrawables(left = drawable)
fun TextView.drawableTop(drawable: Drawable?) = compoundDrawables(top = drawable)
fun TextView.drawableRight(drawable: Drawable?) = compoundDrawables(right = drawable)
fun TextView.drawableBottom(drawable: Drawable?) = compoundDrawables(bottom = drawable)

fun TextView.compoundDrawables(left: Drawable? = null, top: Drawable? = null,
                               right: Drawable? = null, bottom: Drawable? = null) {

    val d = compoundDrawables

    val l = left ?: d[0]
    val t = top ?: d[1]
    val r = right ?: d[2]
    val b = bottom ?: d[3]

    setCompoundDrawablesWithIntrinsicBounds(l, t, r, b)
}