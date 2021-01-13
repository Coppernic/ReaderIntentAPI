package fr.coppernic.lib.readerintentapi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


/**
 * Created by Michael Reynier.
 * Date : 06/01/2021.
 */
@RunWith(AndroidJUnit4::class)
class ScanTest {

    lateinit var scan: Scan

    @Before
    fun setUp() {
        scan = Scan(InstrumentationRegistry.getInstrumentation().context, Type.RFID_ICLASS)
    }


    @Test
    fun testStartScan() {
        scan.startScan()
    }

    /**
     * Test without iclass wedge installed
     */
    @Test(expected = Exception::class)
    fun testStartScanException() {
        scan.startScan()
    }

    @Test
    fun testStopScan() {
        scan.stopScan()
    }

    /**
     * Test without iclass wedge installed
     */
    @Test(expected = Exception::class)
    fun testStopScanException() {
        scan.stopScan()
    }

    @Test
    fun testRegisterReceiver() {
        val countDownLatch = CountDownLatch(1)

        scan.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                assert(
                    intent.action == ACTION_SCAN_SUCCESS ||
                            intent.action == ACTION_SCAN_ERROR
                )
                if (intent.action == ACTION_SCAN_SUCCESS) {
                    assert(!intent.getStringExtra(KEY_DATA_CARD_NUMBER).isNullOrEmpty())
                    assert(!intent.getStringExtra(KEY_RFID_DATA_COMPANY_CODE).isNullOrEmpty())
                    assert(!intent.getStringExtra(KEY_RFID_DATA_FACILITY_CODE).isNullOrEmpty())
                    assert(!intent.getStringExtra(KEY_RFID_DATA_PACS).isNullOrEmpty())
                    assert(!intent.getStringExtra(KEY_DATA_TYPE).isNullOrEmpty())
                    val data = intent.getByteArrayExtra(KEY_DATA_BYTES)
                    assert(data != null && data.isNotEmpty())
                } else {
                    assert(!intent.getStringExtra(KEY_DATA_ERROR_MESSAGE).isNullOrEmpty())
                }
                countDownLatch.countDown()
            }
        })
        scan.startScan()
        countDownLatch.await(5, TimeUnit.SECONDS)
        assert(countDownLatch.count == 0L)
        scan.unregisterReceiver()
    }
}
