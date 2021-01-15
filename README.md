# Scan

This is a library to communicate with service handling capture peripherals (RFID, Barcode, etc.)

## Set Up

You will need to set the following repository :

```groovy
repositories {
    maven { url 'https://dl.bintray.com/coppernic/maven/'}
}
```

Add the following dependency :

``` groovy
dependencies {
    implementation 'fr.coppernic.lib.readerintentapi:readerintentapi:0.1.0'
}
```

## Use scan API

- Create Scan class

For HID RFID

```kotlin
var scan = Scan(context, "fr.coppernic.tools.hidiclasswedge")
```

For Barcode

```kotlin
var scan = Scan(context, "fr.coppernic.features.barcode")
```

- Register Receiver or set Listener

You can choose between providing a BroadcastReceiver or setting a listener

With a BroadcastReceiver :

```kotlin
scan.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == ACTION_SCAN_SUCCESS) {
                    val data = intent.getByteArrayExtra(KEY_DATA_BYTES)
                    //handle data
                } else if(intent.action == ACTION_SCAN_ERROR) {
                    val errorMessage = intent.getStringExtra(KEY_DATA_ERROR_MESSAGE)
                    //handle error
                }              
            }
        })
```

with a listener:
```kotlin
 scan.setListener(object : ScanListener {
            override fun onSuccess(data: Data) {
                val dataBytes = data.bytes
                val dataCardNUmber = data.cardNumber
                //handle data
            }

            override fun onFailed(exception: java.lang.Exception) {
                val ex = exception.message
                //handle error
            }

        })
```

- Scan

```kotlin
scan.startScan()
```

- Do not forget to unregister receiver when you leave your activity

```kotlin
scan.unregisterReceiver()
```

## License

    Copyright (C) 2021 Coppernic

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

