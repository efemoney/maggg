package com.efemoney.maggg.ui.widget

import android.content.Context
import android.support.annotation.IntDef
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.efemoney.maggg.R

class AspectRatioImageView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {

    // NOTE: These must be kept in sync with the AspectRatioImageView attributes in attrs.xml.
    @IntDef(MEASUREMENT_WIDTH.toLong(), MEASUREMENT_HEIGHT.toLong())
    @Retention(AnnotationRetention.SOURCE)
    annotation class Dominant

    var aspectRatio: Float = 0f
        set(value) {
            field = value

            if (aspectRatioEnabled) requestLayout()
        }

    var dominantMeasurement: Int = 0
        set(value) {
            if (value != MEASUREMENT_HEIGHT && value != MEASUREMENT_WIDTH)
                throw IllegalArgumentException("Invalid measurement type.")

            field = value

            if (aspectRatioEnabled) requestLayout()
        }

    var aspectRatioEnabled: Boolean = false
        set(value) {
            field = value

            requestLayout()
        }

    init {

        val a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView)

        aspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspectRatio, DEFAULT_ASPECT_RATIO)
        dominantMeasurement = a.getInt(R.styleable.AspectRatioImageView_dominantMeasurement, DEFAULT_DOMINANT_MEASUREMENT)
        aspectRatioEnabled = a.getBoolean(R.styleable.AspectRatioImageView_aspectRatioEnabled, DEFAULT_ASPECT_RATIO_ENABLED)

        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!aspectRatioEnabled) return

        val newWidth: Int
        val newHeight: Int

        when (dominantMeasurement) {

            MEASUREMENT_WIDTH -> {
                newWidth = measuredWidth
                newHeight = (newWidth / aspectRatio).toInt()
            }

            MEASUREMENT_HEIGHT -> {
                newHeight = measuredHeight
                newWidth = (newHeight * aspectRatio).toInt()
            }

            else -> throw IllegalStateException("Unknown measurement with ID " + dominantMeasurement)
        }

        setMeasuredDimension(newWidth, newHeight)
    }

    companion object {

        const val MEASUREMENT_WIDTH = 0
        const val MEASUREMENT_HEIGHT = 1

        private val DEFAULT_ASPECT_RATIO = 1.67f
        private val DEFAULT_ASPECT_RATIO_ENABLED = false
        private val DEFAULT_DOMINANT_MEASUREMENT = MEASUREMENT_WIDTH.toInt()
    }
}