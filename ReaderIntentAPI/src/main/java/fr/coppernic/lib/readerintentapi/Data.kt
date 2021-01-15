package fr.coppernic.lib.readerintentapi

import fr.coppernic.sdk.utils.core.CpcBytes
import java.nio.charset.StandardCharsets

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

        if (!bytes.contentEquals(other.bytes) || !cardNumber.contentEquals(other.cardNumber) || !companyCode.contentEquals(
                other.companyCode
            ) || !facilityCode.contentEquals(other.facilityCode) || !pacs.contentEquals(other.pacs) || !dataType.contentEquals(
                other.dataType
            )
        ) return false

        return true
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode() + cardNumber.hashCode() + companyCode.hashCode() +
                facilityCode.hashCode() + pacs.hashCode() + dataType.hashCode()
    }

    fun string(): String {
        return String(bytes, StandardCharsets.UTF_8)
    }

    fun hexa(): String {
        return CpcBytes.byteArrayToString(bytes, bytes.size, false)
    }
}


