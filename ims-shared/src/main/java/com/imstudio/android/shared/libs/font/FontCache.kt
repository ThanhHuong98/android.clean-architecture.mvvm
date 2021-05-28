package com.imstudio.android.shared.libs.font

import android.content.Context
import android.graphics.Typeface
import com.imstudio.android.shared.common.functional.tryOrNull
import java.util.*

object FontCache {

    private val fontCache = Hashtable<String, Typeface?>()

    operator fun get(context: Context, path: String?): Typeface? {

        path ?: return null

        var typeface = fontCache[path]
        if (typeface == null) {
            typeface = tryOrNull { Typeface.createFromAsset(context.assets, path) }
            fontCache[path] = typeface
        }
        return typeface
    }
}