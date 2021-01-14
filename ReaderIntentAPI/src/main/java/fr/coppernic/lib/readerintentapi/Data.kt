package fr.coppernic.lib.readerintentapi

/**
 * Created by Michael Reynier.
 * Date : 14/01/2021.
 */
data class Data(
    var data: ByteArray,
    var cardNumber: String = "",
    var companyCode: String = "",
    var facilityCode: String ="",
    var pacs: String="",
    var dataType: String=""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Data) return false

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }
}
