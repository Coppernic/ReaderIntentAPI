package fr.coppernic.lib.readerintentapi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import fr.coppernic.sdk.core.Defines.IntentDefines.*


//TODO to put in CpcCore
const val KEY_DATA_BYTES = "DataBytes"
const val KEY_DATA_CARD_NUMBER = "DataCardNumber"
const val KEY_HID_DATA_COMPANY_CODE = "HidDataCompanyCode"
const val KEY_HID_DATA_FACILITY_CODE = "HidDataFacilityCode"
const val KEY_HID_DATA_PACS = "HidDataPacs"
const val KEY_DATA_TYPE = "DataType"
const val KEY_DATA_ERROR_MESSAGE = "DataErrorMessage"
const val ACTION_SCAN_SUCCESS = "org.reader.intent.api.read.success"
const val ACTION_SCAN_ERROR = "org.reader.intent.api.read.failed"
const val ACTION_SCAN = "org.reader.intent.api.scan"
const val ACTION_SCAN_ABORT = "org.reader.intent.api.abort"
const val ACTION_START_SERVICE = "org.reader.intent.api.start.service"
const val ACTION_STOP_SERVICE = "org.reader.intent.api.stop.service"

/**
 * Created by Michael Reynier.
 * Date : 06/01/2021.
 */
class Scan(private var context: Context, private var packageName: String) {

    private var broadcastReceiver: BroadcastReceiver? = null

    /**
     * Start data scan
     */
    fun startScan() {
        val scanIntent = Intent().apply {
            setPackage(packageName)
            action = ACTION_SCAN
        }
        sendIntent(scanIntent)
    }

    /**
     * Stop scanning data
     */
    fun stopScan() {
        val abortScanIntent = Intent().apply {
            setPackage(packageName)
            action = ACTION_SCAN_ABORT
        }
        sendIntent(abortScanIntent)
    }

    /**
     * Starts service
     */
    fun startService() {
        val startService = Intent().apply {
            setPackage(packageName)
            action = ACTION_START_SERVICE
        }
        sendIntent(startService)
    }

    /**
     * Stops service
     */
    fun stopService() {
        val stopService = Intent().apply {
            setPackage(packageName)
            action = ACTION_STOP_SERVICE
        }
        sendIntent(stopService)
    }

    /**
     * Registers receiver
     * @param broadcastReceiver Broadcast Receiver which will receive the result
     */
    fun registerReceiver(broadcastReceiver: BroadcastReceiver) {
        // Registers iClass wedge intent receiver
        this.broadcastReceiver = broadcastReceiver
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_SCAN_SUCCESS)
        intentFilter.addAction(ACTION_SCAN_ERROR)
        context.registerReceiver(broadcastReceiver, intentFilter)
    }

    /**
     * Unregisters broadcast receiver
     */
    fun unregisterReceiver() {
        context.unregisterReceiver(broadcastReceiver)
    }

    private fun sendIntent(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
                ?: throw ReaderIntentAPIException("Service not found")
        } else {
            context.startService(intent) ?: throw ReaderIntentAPIException("Service not found")
        }
    }

}
