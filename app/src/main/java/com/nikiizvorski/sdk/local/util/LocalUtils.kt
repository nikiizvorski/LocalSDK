package com.nikiizvorski.sdk.local.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Build
import android.net.wifi.WifiManager
import org.json.JSONException
import org.json.JSONObject
import android.net.wifi.SupplicantState
import android.text.TextUtils
import android.support.v4.content.ContextCompat
import android.util.Log
import java.util.*

/**
 *
 * @property facebookClientToken String?
 * @property facebookAccessToken String?
 * @property urbanAirshipChannelId String?
 * @property urbanAirshipNamedUserId String?
 * @property deviceMake String
 * @property deviceModel String
 * @property deviceOS String
 * @property country String
 * @property timeZoneOffset String
 * @property sdkVersion String
 */
class LocalUtils {
    val deviceMake: String
        get() = Build.MANUFACTURER

    val deviceModel: String
        get() = Build.MODEL

    val deviceOS: String
        get() = Build.VERSION.RELEASE

    val country: String
        get() = Locale.getDefault().getCountry()

    val timeZoneOffset: String
        get() {
            val timezone = Calendar.getInstance().getTimeZone()
            var offset = timezone.getRawOffset()
            if (timezone.inDaylightTime(Date())) {
                offset += timezone.getDSTSavings()
            }
            return (offset / 1000).toString()
        }

    /**
     *
     * @param context Context
     * @return String?
     */
    @SuppressLint("WrongConstant")
    fun getWifi(context: Context): String? {
        try {
            val disabled = JSONObject()
            disabled.put("enabled", false)

            if (ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_WIFI_STATE") != 0) {
                Log.d("_LocalSDK", "ACCESS_WIFI_STATE permission not granted")
                return disabled.toString()
            }

            if (!context.getPackageManager().hasSystemFeature("android.hardware.wifi")) {
                return disabled.toString()
            }

            val wifiManager = context.getApplicationContext().getSystemService("wifi") as WifiManager

            val wifiInfo = wifiManager.connectionInfo

            if (wifiInfo == null || TextUtils.isEmpty(wifiInfo.bssid) || wifiInfo.supplicantState != SupplicantState.COMPLETED) {
                return disabled.toString()
            }

            val wifi = JSONObject()
            wifi.put("bssid", wifiInfo.bssid)
            wifi.put("ssid", wifiInfo.ssid)
            wifi.put("rssi", wifiInfo.rssi)
            if (Build.VERSION.SDK_INT >= 21) {
                wifi.put("frequency", wifiInfo.frequency)
            }

            val enabled = JSONObject()
            enabled.put("enabled", true)
            enabled.put("current_connection", wifi)

            return enabled.toString()
        } catch (e: JSONException) {}

        return null
    }

    /**
     *
     * @param context Context
     * @return Boolean
     */
    @SuppressLint("WrongConstant")
    fun isWifiEnabled(context: Context): Boolean {
        if (!context.getPackageManager().hasSystemFeature("android.hardware.wifi")) {
            return false
        }

        val wifiManager = context.getApplicationContext().getSystemService("wifi") as WifiManager
        return wifiManager.isWifiEnabled
    }

    /**
     *
     * @param distanceMeters Double
     * @param bearingDegrees Double
     * @param location Location
     * @return Location
     */
    fun locationByMoving(distanceMeters: Double, bearingDegrees: Int, location: Location): Location {
        val distanceRadians = distanceMeters / 6372797.6
        val bearingRadians = bearingDegrees * 3.141592653589793 / 180.0
        val lat1 = location.getLatitude() * 3.141592653589793 / 180.0
        val lon1 = location.getLongitude() * 3.141592653589793 / 180.0

        val lat2 = Math.asin(
            Math.sin(lat1) * Math.cos(distanceRadians) + Math.cos(lat1) * Math.sin(distanceRadians) * Math.cos(
                bearingRadians
            )
        )

        val lon2 = lon1 + Math.atan2(
            Math.sin(bearingRadians) * Math.sin(distanceRadians) * Math.cos(lat1),
            Math.cos(distanceRadians) - Math.sin(lat1) * Math.sin(lat2)
        )

        val newLocation = Location(location.getProvider())
        newLocation.setLatitude(lat2 * 180.0 / 3.141592653589793)
        newLocation.setLongitude(lon2 * 180.0 / 3.141592653589793)
        newLocation.setAccuracy(location.getAccuracy())
        newLocation.setTime(location.getTime())
        return newLocation
    }

    /**
     *
     * @param hex String
     * @return ByteArray
     */
    fun fromHexString(hex: String): ByteArray {
        /* 222 */
        val len = hex.length
        /* 223 */
        val bytes = ByteArray(len / 2)
        /* 224 */
        var i = 0
        while (i < len) {
            bytes[i / 2] = ((Character.digit(hex[i], 16) shl 4) + Character.digit(hex[i + 1], 16)).toByte()
            i += 2
        }
        return bytes
    }

    /**
     *
     * @param bytes ByteArray
     * @param length Int
     * @return String
     */
    fun toHexString(bytes: ByteArray, length: Int): String {
        var length = length
        val sb = StringBuilder()

        if (length < 0 || length > bytes.size) {
            length = bytes.size
        }

        for (i in 0 until length) {
            val b = bytes[i]
            sb.append(String.format("%02x", *arrayOf<Any>(java.lang.Byte.valueOf(b))))
        }

        return sb.toString()
    }
}