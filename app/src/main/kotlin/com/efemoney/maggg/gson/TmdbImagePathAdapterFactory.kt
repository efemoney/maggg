package com.efemoney.maggg.gson

import com.efemoney.maggg.data.model.BackdropPath
import com.efemoney.maggg.data.model.PosterPath
import com.efemoney.maggg.data.model.TmdbImagePath
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

@Suppress("UNCHECKED_CAST")
object TmdbImagePathAdapterFactory : TypeAdapterFactory {

    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {

        if (!TmdbImagePath::class.java.isAssignableFrom(type.rawType)) return null

        return (TmdbImagePathAdapter(type as TypeToken<TmdbImagePath>) as TypeAdapter<T>).nullSafe()
    }

    class TmdbImagePathAdapter(val type: TypeToken<TmdbImagePath>) : TypeAdapter<TmdbImagePath>() {
        override fun write(writer: JsonWriter, value: TmdbImagePath) {
            writer.value(value.path) // write path to json
        }
        override fun read(reader: JsonReader): TmdbImagePath {

            val path = reader.nextString()

            return when {
                type.rawType == PosterPath::class.java -> PosterPath(path)
                type.rawType == BackdropPath::class.java -> BackdropPath(path)
                else -> throw IllegalArgumentException()
            }
        }
    }
}
