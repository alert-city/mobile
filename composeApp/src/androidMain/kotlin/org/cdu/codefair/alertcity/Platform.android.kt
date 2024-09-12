package org.cdu.codefair.alertcity

actual val isDebug: Boolean = BuildConfig.DEBUG

actual fun getPlatform() = "Android ${android.os.Build.VERSION.SDK_INT}"