package fr.coppernic.sdk.scan

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build


const val ACTION_RFID_SUCCESS = "com.islog.rfidguard.thirdparty.scansuccess"
const val ACTION_RFID_ERROR = "com.islog.rfidguard.thirdparty.iclassfailed"
const val ACTION_HID_ICLASS_SUCCESS = "fr.coppernic.intent.hid.iclasssuccess"
const val ACTION_HID_ICLASS_ERROR = "fr.coppernic.intent.hid.iclassfailed"

const val SERVICE_PACKAGE_NAME = "fr.coppernic.tools.hidiclasswedge"
const val INTENT_ACTION_SCAN = "fr.coppernic.intent.action.hid.iclass.SCAN"
const val INTENT_ACTION_SCAN_STOP = "fr.coppernic.intent.action.hid.iclass.scan.STOP"

const val KEY_RFID_DATA_BYTES = "RfidDataBytes"
const val KEY_RFID_DATA_CARD_NUMBER = "RfidDataCardNumber"
const val KEY_RFID_DATA_COMPANY_CODE = "RfidDataCompanyCode"
const val KEY_RFID_DATA_FACILITY_CODE = "RfidDataFacilityCode"
const val KEY_RFID_DATA_PACS = "RfidDataPacs"
const val KEY_RFID_DATA_TYPE = "RfidDataType"
const val KEY_RFID_DATA_ERROR_MESSAGE = "RfidDataErrorMessage"


/**
 * Created by Michael Reynier.
 * Date : 06/01/2021.
 */
class Scan(private var context: Context) {

    fun startScan(){
        val scanIntent = Intent()
        scanIntent.setPackage(SERVICE_PACKAGE_NAME)
        scanIntent.action = INTENT_ACTION_SCAN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(scanIntent) ?: throw Exception("Service not found")
        } else {
            context.startService(scanIntent) ?: throw Exception("Service not found")
        }
    }

    fun stopScan(){
        val scanIntent = Intent()
        scanIntent.setPackage(SERVICE_PACKAGE_NAME)
        scanIntent.action = INTENT_ACTION_SCAN_STOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(scanIntent) ?: throw Exception("Service not found")
        } else {
            context.startService(scanIntent) ?: throw Exception("Service not found")
        }
    }

    /**
     * Register receiver
     * @param broadcastReceiver Broadcast Receiver which will receive the result
     */
    fun registerReceiver(broadcastReceiver: BroadcastReceiver) {
        // Registers iClass wedge intent receiver
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_RFID_SUCCESS)
        intentFilter.addAction(ACTION_RFID_ERROR)
        context.registerReceiver(broadcastReceiver, intentFilter)

    }

}
