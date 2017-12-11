package com.efemoney.maggg.data.model

import android.os.Parcel
import android.os.Parcelable

interface TmdbImagePath {
    val type: String
    val path: String
}

data class PosterPath(override val path: String) : TmdbImagePath, Parcelable {
    override val type = "poster"

    constructor(source: Parcel) : this(source.readString())

    override fun writeToParcel(dest: Parcel, flags: Int) = dest.writeString(path)
    override fun describeContents() = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PosterPath> = object : Parcelable.Creator<PosterPath> {
            override fun createFromParcel(source: Parcel): PosterPath = PosterPath(source)
            override fun newArray(size: Int): Array<PosterPath?> = arrayOfNulls(size)
        }
    }
}

data class BackdropPath(override val path: String) : TmdbImagePath, Parcelable {
    override val type = "backdrop"

    constructor(source: Parcel) : this(source.readString())

    override fun writeToParcel(dest: Parcel, flags: Int) = dest.writeString(path)
    override fun describeContents() = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BackdropPath> = object : Parcelable.Creator<BackdropPath> {
            override fun createFromParcel(source: Parcel): BackdropPath = BackdropPath(source)
            override fun newArray(size: Int): Array<BackdropPath?> = arrayOfNulls(size)
        }
    }
}