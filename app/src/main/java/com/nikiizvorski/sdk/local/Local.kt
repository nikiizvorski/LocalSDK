package com.nikiizvorski.sdk.local

import android.app.Activity
import android.content.Context
import com.nikiizvorski.sdk.local.base._Local
import com.nikiizvorski.sdk.local.callbacks.LocalCallback

class Local {

    /**
     * SDK Status
     */
    enum class LocalStatus private constructor() {
        SUCCESS,
        ERROR_PUBLISHABLE_KEY,
        ERROR_USER_ID,
        ERROR_PERMISSIONS,
        ERROR_LOCATION,
        ERROR_NETWORK,
        ERROR_UNAUTHORIZED,
        ERROR_SERVER,
        ERROR_UNKNOWN,
        UNKNOWN;
    }

    /**
     * Tracking Status
     */
    val isTracking: Boolean
        get() = _Local.sharedInstance.isTracking

    /**
     * Wifi Status
     */
    val isWifiEnabled: Boolean
        get() = _Local.sharedInstance.isWifiEnabled

    /**
     * Tracking Priority
     */
    enum class LocalPriority(val priority: Int) {
        RESPONSIVE(100),
        EFFICIENT(102)
    }

    /**
     *
     * @param context Context
     */
    fun initialize(context: Context) {
        _Local.sharedInstance.initialize(context, null)
    }

    /**
     *
     * @param context Context
     * @param publishableKey String
     */
    fun initialize(context: Context, publishableKey: String) {
        _Local.sharedInstance.initialize(context, publishableKey)
    }

    /**
     *
     * @param userId String
     */
    fun setUserId(userId: String) {
        _Local.sharedInstance.setUserId(userId)
    }

    /**
     *
     * @param priority LocalPriority
     */
    fun setTrackingPriority(priority: LocalPriority){
        _Local.sharedInstance.setTrackingPriority(priority)
    }

    /**
     *
     * @return Boolean
     */
    fun checkSelfPermissions(): Boolean {
        return _Local.sharedInstance.checkSelfPermissions()
    }

    /**
     *
     * @param activity Activity
     */
    fun requestPermissions(activity: Activity) {
        _Local.sharedInstance.requestPermissions(activity)
    }

    /**
     *
     * @param callback LocalCallback
     */
    fun trackOnce(callback: LocalCallback) {
        _Local.sharedInstance.trackOnce(callback)
    }

    /**
     *
     * @param callback LocalCallback
     */
    fun startTracking(callback: LocalCallback) {
        _Local.sharedInstance.startTracking(callback)
    }

    /**
     * Stop Tracking
     */
    fun stopTracking() {
        _Local.sharedInstance.stopTracking()
    }
}