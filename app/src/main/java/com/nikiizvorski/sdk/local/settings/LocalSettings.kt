package com.nikiizvorski.sdk.local.settings

import android.content.Context
import android.content.res.Resources
import com.nikiizvorski.sdk.local.Local


/**
 *
 * @property context Context
 * @property TAG String
 * @constructor
 */
class LocalSettings(private val context: Context) {
    private val TAG: String = "LocalSettings"

    /**
     *
     * @param publishableKey String
     */
    fun setPublishableKey(publishableKey: String) {
        val preferences = context.getSharedPreferences("LocalSDK", 0)
        val editor = preferences.edit()
        editor.putString("local-publishableKey", publishableKey)
        editor.commit()
    }

    /**
     *
     * @return String?
     */
    fun readPublishableKey(): String? {
        try {
            val publishableKey = context.getResources().getString(context.getResources().getIdentifier("local_pk", "string", context.getPackageName()))
            setPublishableKey(publishableKey)
            return publishableKey
        } catch (e: Resources.NotFoundException) {}
        return null
    }

    /**
     *
     * @return String?
     */
    fun getPublishableKey(): String? {
        val preferences = context.getSharedPreferences("LocalSDK", 0)
        return preferences.getString("local-publishableKey", null)
    }

    /**
     *
     * @param userId String
     */
    fun setUserId(userId: String) {
        val preferences = context.getSharedPreferences("LocalSDK", 0)
        val editor = preferences.edit()
        editor.putString("local-userId", userId)
        editor.commit()
    }

    /**
     *
     * @return String?
     */
    fun getUserId(): String? {
        val preferences = context.getSharedPreferences("LocalSDK", 0)
        return preferences.getString("local-userId", null)
    }

    /**
     *
     * @param priority Local.LocalPriority
     */
    fun setTrackingPriority(priority: Local.LocalPriority) {
        val preferences = context.getSharedPreferences("LocalSDK", 0)
        val editor = preferences.edit()
        editor.putInt("local-trackingPriority", priority.priority)
        editor.commit()
    }

    /**
     *
     * @return Local.LocalPriority?
     */
    fun getTrackingPriority(): Int {
        val preferences = context.getSharedPreferences("LocalSDK", 0)
        return preferences.getInt("local-trackingPriority", 100)
    }

    /**
     *
     * @param tracking Boolean
     */
    fun setTracking(tracking: Boolean) {
        val preferences = context.getSharedPreferences("LocalSDK", 0)
        val editor = preferences.edit()
        editor.putBoolean("local-tracking", tracking)
        editor.commit()
    }

    /**
     *
     * @return Boolean
     */
    fun getTracking(): Boolean {
        val preferences = context.getSharedPreferences("LocalSDK", 0)
        return preferences.getBoolean("local-tracking", false)
    }
}