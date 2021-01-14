package fr.coppernic.lib.readerintentapi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build



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
        if(this.broadcastReceiver == null) unregisterReceiver()
        // Registers iClass wedge intent receiver
        this.broadcastReceiver = broadcastReceiver
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_SCAN_SUCCESS)
        intentFilter.addAction(ACTION_SCAN_ERROR)
        context.registerReceiver(broadcastReceiver, intentFilter)
    }

    /**
     * Registers receiver
     * @param scanListener scan listener
     */
    fun setListener(scanListener: ScanListener) {
        if(this.broadcastReceiver == null) unregisterReceiver()
        // Registers iClass wedge intent receiver
        this.broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == ACTION_SCAN_SUCCESS) {
                    val data = intent.getByteArrayExtra(KEY_DATA_BYTES)
                    if (data != null) scanListener.onSuccess(
                        Data(
                            data,
                            intent.getStringExtra(KEY_DATA_CARD_NUMBER) ?: "",
                            intent.getStringExtra(KEY_HID_DATA_COMPANY_CODE) ?: "",
                            intent.getStringExtra(KEY_HID_DATA_FACILITY_CODE) ?: "",
                            intent.getStringExtra(KEY_HID_DATA_PACS) ?: "",
                            intent.getStringExtra(KEY_DATA_TYPE) ?: ""
                        )
                    ) else scanListener.onFailed(ReaderIntentAPIException("Data received is null"))
                } else if (intent.action == ACTION_SCAN_ERROR) {
                    val message = intent.getStringExtra(KEY_DATA_ERROR_MESSAGE)
                    if (message != null) scanListener.onFailed(ReaderIntentAPIException(message))
                }
            }
        }
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
        broadcastReceiver = null
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
