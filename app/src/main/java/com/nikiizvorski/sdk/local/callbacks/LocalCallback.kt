package com.nikiizvorski.sdk.local.callbacks

import android.location.Location
import com.nikiizvorski.sdk.local.Local.LocalStatus

/**
 * Local Callback
 */
abstract class LocalCallback {
    /**
     *
     * @param paramLocalStatus LocalStatus
     * @param paramLocation Location?
     * @param paramLocalUser LocalUser?
     */
    abstract fun onCallback(paramLocalStatus: LocalStatus, paramLocation: Location?, paramLocalUser: String?)
}