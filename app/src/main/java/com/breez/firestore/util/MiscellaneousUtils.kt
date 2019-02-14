package com.breez.firestore.util

import timber.log.Timber
import java.io.Closeable
import java.io.IOException

/**
 * Helper for miscellaneous operations
 */
object MiscellaneousUtils {

    var packageName = ""

    private val EXTRA_BASE = "EXTRA_"
    private val ACTION_BASE = "ACTION_"

    fun getExtra(extraName: String = "") = "$packageName $EXTRA_BASE ${extraName.toUpperCase()}"

    fun getAction(actionName: String) = "$packageName $ACTION_BASE ${actionName.toUpperCase()}"

    /**
     * Close any closeable silently
     * @param closeable any closeable
     */
    fun close(closeable: Closeable?) {
        try {
            closeable?.close()
        } catch (e: IOException) {
            Timber.e("Error MISCELLANEOUS: $e")
        }

    }

}