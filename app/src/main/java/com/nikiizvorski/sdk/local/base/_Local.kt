package com.nikiizvorski.sdk.local.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.app.Activity
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.nikiizvorski.sdk.local.Local
import com.nikiizvorski.sdk.local.callbacks.LocalCallback
import com.nikiizvorski.sdk.local.settings.LocalSettings
import com.nikiizvorski.sdk.local.util.LocalUtils
import java.lang.ref.WeakReference


class _Local : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private val TAG: String = "LocalSDK"
    lateinit var mContext: WeakReference<Context>
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mRequestLocationListener: LocationCallback? = null

    companion object {
        private var sInstance: _Local? = null

        val sharedInstance: _Local
            @Synchronized get() {
                if (sInstance == null) {
                    sInstance = _Local()
                }
                return sInstance as _Local
            }
    }

    val isTracking: Boolean
        get() = LocalSettings(mContext.get()!!).getTracking()

    val isWifiEnabled: Boolean
        get() = LocalUtils().isWifiEnabled(mContext.get()!!)

    /**
     *
     * @param context Context?
     * @param publishableKey String?
     */
    fun initialize(context: Context?, publishableKey: String?) {
        if (context == null) {
             return
        }

        this.mContext = WeakReference(context.applicationContext)

        if (publishableKey == null) {
           LocalSettings(mContext.get()!!).readPublishableKey()
        } else {
           LocalSettings(mContext.get()!!).setPublishableKey(publishableKey)
        }

        if (this.mGoogleApiClient == null) {
            this.mGoogleApiClient = GoogleApiClient.Builder(mContext.get()!!).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build()
        }
    }

    /**
     *
     * @param userId String
     */
    fun setUserId(userId: String) {
        LocalSettings(mContext.get()!!).setUserId(userId)
    }

    /**
     *
     * @return Boolean
     */
    fun checkSelfPermissions(): Boolean {
        return Build.VERSION.SDK_INT < 23 || mContext.get() != null && ContextCompat.checkSelfPermission(
            mContext.get()!!, "android.permission.ACCESS_FINE_LOCATION") == 0 &&  ContextCompat.checkSelfPermission(
            mContext.get()!!, "android.permission.ACCESS_COARSE_LOCATION") == 0
    }

    /**
     *
     * @param activity Activity
     */
    fun requestPermissions(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_FINE_LOCATION") != 0 && ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            val requestCode = 0
            ActivityCompat.requestPermissions(activity, arrayOf("android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"), requestCode)
        }
    }

    /**
     *
     * @param callback LocalCallback
     */
    @SuppressLint("MissingPermission")
    fun trackOnce(callback: LocalCallback) {
        val publishableKey = LocalSettings(mContext.get()!!).getPublishableKey()
        if (publishableKey == null || publishableKey.isEmpty()) {
            callback.onCallback(Local.LocalStatus.ERROR_PUBLISHABLE_KEY, null, null)
            Log.d(TAG, "ERROR: " + Local.LocalStatus.ERROR_PUBLISHABLE_KEY)
            return
        }

        val userId = LocalSettings(mContext.get()!!).getUserId()
        if (userId == null || userId.isEmpty()) {
            callback.onCallback(Local.LocalStatus.ERROR_USER_ID, null, null)
            Log.d(TAG, "ERROR: " + Local.LocalStatus.ERROR_USER_ID)
            return
        }

        if (!checkSelfPermissions()) {
            callback.onCallback(Local.LocalStatus.ERROR_PERMISSIONS, null, null)
            Log.d(TAG, "ERROR: " + Local.LocalStatus.ERROR_PERMISSIONS)
            return
        }

        LocationServices.getFusedLocationProviderClient(mContext.get()!!).lastLocation.addOnSuccessListener { location: Location? ->
            run {
                callback.onCallback(Local.LocalStatus.SUCCESS, location, LocalSettings(mContext.get()!!).getUserId())
            }
        }
    }

    /**
     *
     * @param priority Local.LocalPriority
     */
    fun setTrackingPriority(priority: Local.LocalPriority) {
        LocalSettings(mContext.get()!!).setTrackingPriority(priority)
    }

    /**
     *
     * @param callback LocalCallback
     */
    @SuppressLint("MissingPermission")
    fun startTracking(callback: LocalCallback) {
        LocalSettings(mContext.get()!!).setTracking(true)

        val publishableKey = LocalSettings(mContext.get()!!).getPublishableKey()

        if (publishableKey == null || publishableKey.isEmpty()) {
            Log.d(TAG, "ERROR: " + Local.LocalStatus.ERROR_PUBLISHABLE_KEY)
            return
        }

        val userId = LocalSettings(mContext.get()!!).getUserId()
        if (userId == null || userId.isEmpty()) {
            Log.d(TAG, "ERROR: " + Local.LocalStatus.ERROR_USER_ID)
            return
        }

        if (!checkSelfPermissions()) {
            Log.d(TAG, "ERROR: " + Local.LocalStatus.ERROR_PERMISSIONS)
            return
        }

        val locationRequest = LocationRequest().setInterval(5000L).setFastestInterval(1000L)
            .setPriority(LocalSettings(mContext.get()!!).getTrackingPriority())

        mRequestLocationListener = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                result ?: return
                for (location in result.locations){
                    callback.onCallback(Local.LocalStatus.SUCCESS, location, LocalSettings(mContext.get()!!).getUserId())
                }
            }
        }

        LocationServices.getFusedLocationProviderClient(mContext.get()!!).requestLocationUpdates(locationRequest, mRequestLocationListener, null)
    }

    /**
     * Stop Tracking
     */
    fun stopTracking() {
        LocalSettings(mContext.get()!!).setTracking(false)

        LocationServices.getFusedLocationProviderClient(mContext.get()!!).removeLocationUpdates(mRequestLocationListener)
    }

    /**
     *
     * @param p0 Bundle?
     */
    override fun onConnected(p0: Bundle?) {
        Log.d(TAG, "Play Services connected")
    }

    /**
     *
     * @param result ConnectionResult
     */
    override fun onConnectionFailed(result: ConnectionResult) {
        Log.d(TAG, "Play Services connection failed")
    }

    /**
     *
     * @param cause Int
     */
    override fun onConnectionSuspended(cause: Int) {
        Log.d(TAG, "Play Services connection suspended")
    }
}