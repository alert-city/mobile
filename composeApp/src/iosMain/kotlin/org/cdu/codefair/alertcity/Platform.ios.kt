@file:OptIn(ExperimentalNativeApi::class)

package org.cdu.codefair.alertcity

import platform.UIKit.UIDevice
import kotlin.experimental.ExperimentalNativeApi

actual val isDebug: Boolean = Platform.isDebugBinary

actual fun getPlatform() =
    UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion