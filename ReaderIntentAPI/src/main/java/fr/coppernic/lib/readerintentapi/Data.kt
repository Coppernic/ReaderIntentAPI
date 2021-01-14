package fr.coppernic.lib.readerintentapi

import fr.coppernic.sdk.utils.core.CpcBytes

/**
 * Created by Michael Reynier.
 * Date : 14/01/2021.
 */
data class Data(
    val bytes: ByteArray,
    val cardNumber: String = "",
    val companyCode: String = "",
    val facilityCode: String = "",
    val pacs: String = "",
    val dataType: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Data) return false

        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }

    fun string(): String {
        return CpcBytes.byteArrayToUtf8String(bytes)
    }

    fun hexa(): String {
        return CpcBytes.byteArrayToAsciiString(bytes)
    }
}


