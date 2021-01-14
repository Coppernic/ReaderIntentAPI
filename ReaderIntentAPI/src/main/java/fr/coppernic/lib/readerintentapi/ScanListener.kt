package fr.coppernic.lib.readerintentapi

import java.lang.Exception

/**
 * Created by Michael Reynier.
 * Date : 14/01/2021.
 */
interface ScanListener {
    fun onSuccess(data: Data)
    fun onFailed(exception: Exception)
}
