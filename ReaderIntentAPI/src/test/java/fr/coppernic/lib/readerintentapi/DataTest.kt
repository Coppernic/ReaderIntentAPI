package fr.coppernic.lib.readerintentapi

import org.junit.Test

/**
 * Created by Michael Reynier.
 * Date : 15/01/2021.
 */
class DataTest {

    @Test
    fun string() {
        val d = Data("Test OK".toByteArray())
        assert(d.string() == "Test OK")
    }

    @Test
    fun hexa() {
        val d = Data(bytes = byteArrayOf(0x10, 0xFE.toByte(), 0xA2.toByte()))
        assert(d.hexa() == "10FEA2")
    }

}