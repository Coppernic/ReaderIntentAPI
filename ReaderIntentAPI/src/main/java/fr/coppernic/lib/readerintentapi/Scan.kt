package fr.coppernic.lib.readerintentapi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import fr.coppernic.sdk.core.Defines.IntentDefines.*


const val AGRIDENT_SERVICE_PACKAGE_NAME = "fr.coppernic.tools.cpcagridentwedge"
const val BARCODE_SERVICE_PACKAGE_NAME = "fr.coppernic.features.barcode"
const val HID_SERVICE_PACKAGE_NAME = "fr.coppernic.tools.hidiclasswedge"

//TODO to put in CpcCore
const val KEY_DATA_BYTES = "DataBytes"
const val KEY_DATA_CARD_NUMBER = "DataCardNumber"
const val KEY_RFID_DATA_COMPANY_CODE = "RfidDataCompanyCode"
const val KEY_RFID_DATA_FACILITY_CODE = "RfidDataFacilityCode"
const val KEY_RFID_DATA_PACS = "RfidDataPacs"
const val KEY_DATA_TYPE = "DataType"
const val KEY_DATA_ERROR_MESSAGE = "DataErrorMessage"
const val ACTION_SCAN_SUCCESS = "org.scan.read.success"
const val ACTION_SCAN_ERROR = "org.scan.read.failed"

/**
 * Created by Michael Reynier.
 * Date : 06/01/2021.
 */
class Scan(private var context: Context, private var readerType: Type) {

    private var broadcastReceiver: BroadcastReceiver? = null

    fun startScan() {
        val scanIntent = getScanIntent(readerType, true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(scanIntent) ?: throw Exception("Service not found")
        } else {
            context.startService(scanIntent) ?: throw Exception("Service not found")
        }
    }

    fun stopScan() {
        val scanIntent = getScanIntent(readerType, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(scanIntent) ?: throw ReaderIntentAPIException("Service not found")
        } else {
            context.startService(scanIntent) ?: throw ReaderIntentAPIException("Service not found")
        }
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

}

enum class Type {
    BARCODE,
    RFID_ICLASS,
    RFID_AGRIDENT
}

/**
 * Retrieves scan intent depending on reader type
 * @param readerType reader type (RFID ICLASS, BARCODE...)
 * @param scan true : start scan, false : stop scan
 */
fun getScanIntent(readerType: Type, scan: Boolean): Intent {
    val scanIntent = Intent()
    return when (readerType) {
        Type.BARCODE -> {
            scanIntent.setPackage(BARCODE_SERVICE_PACKAGE_NAME)
            scanIntent.action = if (scan) INTENT_ACTION_SCAN else INTENT_ACTION_STOP_SCAN
            scanIntent
        }
        Type.RFID_ICLASS -> {
            scanIntent.setPackage(HID_SERVICE_PACKAGE_NAME)
            scanIntent.action = if (scan) ACTION_HID_ICLASS_SCAN else ACTION_HID_ICLASS_SCAN_STOP
            scanIntent
        }
        Type.RFID_AGRIDENT -> {
            scanIntent.setPackage(AGRIDENT_SERVICE_PACKAGE_NAME)
            scanIntent.action = if (scan) ACTION_AGRIDENT_READ else ACTION_AGRIDENT_READ_STOP
            scanIntent
        }
    }
}
