package com.nphau.android.shared.libs

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.pm.PackageInfoCompat
import com.nphau.android.shared.R
import java.util.*

object CommonUtils {

    private const val URL_GOOGLE_PLAY_URI = "market://details?id=com.google.android.gms&hl=en"
    private const val URL_GOOGLE_PLAY =
        "https://play.google.com/store/apps/details?id=com.google.android.gms&hl=en"

    private const val ACTION_VIEW = Intent.ACTION_VIEW

    private val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)

    @JvmStatic
    fun playTone() {
        if (toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD)) {
            toneG.stopTone()
        }
    }

    @JvmStatic
    fun playToneAndVibrate(context: Context, milliseconds: Int) {
        if (toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD))
            toneG.stopTone()
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.let {
            if (it.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    it.vibrate(
                        VibrationEffect.createOneShot(
                            milliseconds.toLong(),
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                else
                    it.vibrate(milliseconds.toLong())
            }
        }
    }

    @JvmStatic
    fun getAppFullVersion(context: Context): String {
        return String.format(
            "%s (%s)A",
            getAppVersionName(context),
            getAppVersionCode(context)
        )
    }

    @JvmStatic
    fun getAppVersionName(context: Context): String {
        var versionName = ""
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            versionName = packageInfo.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return versionName
    }

    @JvmStatic
    fun getAppVersionCode(context: Context): String {
        var versionCode = ""
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            versionCode = PackageInfoCompat.getLongVersionCode(packageInfo).toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return versionCode
    }

    @JvmStatic
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    @JvmStatic
    fun formatWithFont(content: String, fontName: String, fontExt: String): String {
        val addBodyEnd: Boolean = content.toLowerCase(Locale.getDefault()).contains("</body")
        val addBodyStart: Boolean = content.toLowerCase(Locale.getDefault()).contains("<body>")
        val font = "$fontName.$fontExt"
        return "<style type=\"text/css\">@font-face {font-family: $fontName;" +
                "src: url(\"file:///android_asset/fonts/$font\")}" +
                "body {font-family: $fontName;text-align: justify;}</style>" +
                (if (addBodyStart) "<body>" else "") + content + if (addBodyEnd) "</body>" else ""
    }

    @JvmStatic
    fun makeVibrator(context: Context) {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v?.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v?.vibrate(200)
        }
    }

    @JvmStatic
    fun openGpsSetting(activity: Activity, requestCode: Int) {
        try {
            activity.startActivityForResult(
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                requestCode
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun openAppSetting(activity: Activity, requestCode: Int) {
        try {
            activity.startActivityForResult(Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("package:" + activity.packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            }, requestCode)
        } catch (e: Exception) {
            activity.startActivityForResult(
                Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS),
                requestCode
            )
        }
    }

    @JvmStatic
    fun makeVibrator(context: Context, pattern: LongArray) {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v?.vibrate(VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v?.vibrate(1000)
        }
    }

    fun isChromeOs(context: Context): Boolean {
        return context.packageManager.hasSystemFeature("org.chromium.arc.device_management")
    }

    fun hasTelephony(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
    }

    fun hasMicrophone(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun hasFingerprint(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }

    fun hasBluetooth(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
    }

    @JvmStatic
    fun copyTextToClipboard(context: Context, text: String?) {
        (context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)
            ?.setPrimaryClip(ClipData.newPlainText("code", text))
        Toast.makeText(context, R.string.common_copied, Toast.LENGTH_LONG).show()
    }

    @JvmStatic
    fun openApp(activity: Activity, packageClass: String) {
        if (isAppInstalled(activity, packageClass)) {
            activity.startActivity(activity.packageManager.getLaunchIntentForPackage(packageClass))
        } else {
            openGooglePlayIfAppNotBeInstalled(activity, packageClass)
        }
    }

    @JvmStatic
    fun openGooglePlayIfAppNotBeInstalled(activity: Activity, packageClass: String) {
        try {
            activity.startActivity(
                Intent(
                    ACTION_VIEW, Uri.parse("market://details?id=$packageClass")
                )
            )
        } catch (e: Exception) {
            activity.startActivity(
                Intent(
                    ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageClass")
                )
            )
        }
    }

    @JvmStatic
    fun isAppInstalled(mActivity: Activity, packageClass: String): Boolean {
        val pm = mActivity.packageManager
        var isInstalled = false
        try {
            pm.getPackageInfo(packageClass, PackageManager.GET_ACTIVITIES)
            isInstalled = true
        } catch (e: Exception) {
        }
        return isInstalled
    }

    fun openNetworkSetting(activity: Activity) {
        activity.startActivity(Intent(Settings.ACTION_SETTINGS))
    }

    @JvmStatic
    fun updateGooglePlayServicesIfOutDated(context: Context) {
        try {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(URL_GOOGLE_PLAY_URI))
            )
        } catch (e: Exception) {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(URL_GOOGLE_PLAY))
            )
        }
    }

    @JvmStatic
    fun getFullAction(context: Context, name: String): String {
        return context.packageName + ".action.$name"
    }

    fun isIntentAvailable(i: Intent, ctx: Context): Boolean {
        return ctx.packageManager.queryIntentActivities(
            i,
            PackageManager.MATCH_DEFAULT_ONLY
        ).size > 0
    }

}
