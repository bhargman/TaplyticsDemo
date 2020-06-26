package com.grubhub.taplyticsdemo

import android.app.Application
import android.util.Log
import com.taplytics.sdk.SessionInfoRetrievedListener
import com.taplytics.sdk.Taplytics
import com.taplytics.sdk.TaplyticsVar
import org.json.JSONObject

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i("Taplytics", "onCreate in SB?: ${isSunburst()}")
        Taplytics.setUserAttributes(getAttributes())

        // Replace with sdk key!
        Taplytics.startTaplytics(this, "YOUR_SDK_KEY", getOptions()) {
            Log.i("Taplytics", "after started in SB?: ${isSunburst()}")
            reload()
        }
    }

    private fun getAttributes(): JSONObject {
        val attributes = JSONObject()
        attributes.put("isGrubhubEmployee", true)
        return attributes
    }

    private fun getOptions(): HashMap<String, Any> = hashMapOf(
            "debugLogging" to true,
            // Setting this to false (like in production) shows the issue!
            "liveUpdate" to true
    )

    private fun reload() {
        Taplytics.getSessionInfo(object : SessionInfoRetrievedListener {
            override fun sessionInfoRetrieved(map: HashMap<Any, Any>) {
                Taplytics.getRunningExperimentsAndVariations {
                    Log.i("Taplytics", "Experiments: $it")
                    Taplytics.getRunningFeatureFlags {
                        Log.i("Taplytics", "on end in SB?: ${isSunburst()}")
                    }
                }
            }

            override fun onError(map: HashMap<Any, Any>) {

            }

        })
    }

    private fun isSunburst() = TaplyticsVar("sunburst", false).get()

}
