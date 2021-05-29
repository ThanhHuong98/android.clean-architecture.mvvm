package com.imstudio.android.shared.libs.theme

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.imstudio.android.shared.R
import com.imstudio.android.shared.common.extensions.px

object ThemeUtils {

    /**
     * Get a resource id from a resource styled according to the the context's theme.
     */
    @JvmStatic
    fun resolveResourceIdFromAttr(context: Context, @AttrRes attr: Int): Int {
        val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
        val attributeResourceId = a.getResourceId(0, 0)
        a.recycle()
        return attributeResourceId
    }

    @JvmStatic
    fun parseHexColor(colorString: String): Int {
        if (colorString.isNotEmpty() && colorString[0] == '#') {
            // Use a long to avoid rollovers on #ffXXXXXX
            var color = java.lang.Long.parseLong(colorString.substring(1), 16)
            if (colorString.length == 7) {
                // Set the alpha value
                color = color or 0x00000000ff000000
            } else if (colorString.length != 9) {
                throw IllegalArgumentException("Unknown color: $colorString")
            }
            return color.toInt()
        }
        throw IllegalArgumentException("Unknown color")
    }

    @JvmStatic
    fun clearLightStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = activity.window.decorView.systemUiVisibility
            flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.decorView.systemUiVisibility = flags
            activity.window.statusBarColor = ContextCompat
                .getColor(activity, R.color.colorPrimaryDark)
        }
    }

    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources
            .getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0)
            result = context.resources.getDimensionPixelSize(resourceId)
        return result
    }

    @JvmStatic
    fun getScreenHeight(paramContext: Context): Int {
        return paramContext.resources.displayMetrics.heightPixels
    }

    @JvmStatic
    fun getScreenWidth(paramContext: Context): Int {
        return paramContext.resources.displayMetrics.widthPixels
    }

    @JvmStatic
    fun setWindowFlag(activity: Activity, bits: Int, isOn: Boolean) {
        val window = activity.window
        val winParams = window.attributes
        if (isOn) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv() // ~bits
        }
        window.attributes = winParams
    }

    @JvmStatic
    fun getScreenSize(context: Context): Point {
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }

    @JvmStatic
    @TargetApi(Build.VERSION_CODES.M)
    fun fullscreenLightStatusBar(activity: Activity) {
        val window = activity.window
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        doFromSdk(Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    @JvmStatic
    @TargetApi(Build.VERSION_CODES.M)
    fun fullscreenDarkStatusBar(activity: Activity) {
        val window = activity.window
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        doFromSdk(Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }

    }

    @JvmStatic
    fun dp2Px(dp: Float): Int {
        return dp.toInt().px
    }

    @JvmStatic
    fun redrawStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = ContextCompat.getColor(activity, R.color.colorPrimaryDark)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
        }
    }

    @JvmStatic
    fun setLightStatusBar(activity: Activity, color: Int = Color.WHITE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                statusBarColor = color
            }
        }
    }

    @JvmStatic
    fun setDarkStatusBar(activity: Activity, color: Int = Color.WHITE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                statusBarColor = color
            }
        }
    }

    @JvmStatic
    fun isPortraitMode(context: Context): Boolean =
        context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    @JvmStatic
    fun isDarkTheme(context: Context) = context.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    /**
     * Return a drawable object associated with a particular resource ID.
     *
     * Starting in [Build.VERSION_CODES.LOLLIPOP], the returned drawable will be styled for the
     * specified Context's theme.
     *
     * @param drawableId The desired resource identifier, as generated by the aapt tool.
     * This integer encodes the package, type, and resource entry.
     * The value 0 is an invalid identifier.
     * @return Drawable An object that can be used to draw this resource.
     */
    @JvmStatic
    fun getDrawable(context: Context, drawableId: Int): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            ContextCompat.getDrawable(context, drawableId)
        else
            ActivityCompat.getDrawable(context, drawableId)
    }

    @JvmStatic
    fun isLightTheme(context: Context) = !isDarkTheme(context)

    @JvmStatic
    fun setNightMode(forceNight: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (forceNight) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}

/**
 * Execute [f] only if the current Android SDK version is [version] or newer.
 * Do nothing otherwise.
 */
inline fun doFromSdk(version: Int, f: () -> Unit) {
    if (Build.VERSION.SDK_INT >= version) f()
}

