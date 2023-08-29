package com.example.android_method_channel

import android.util.Log
import android.widget.Toast
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject
import kotlin.concurrent.timer

class MainActivity : FlutterActivity() {
    var eventChannelHandler: EventChannelHandler? = null
    var count = 0

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        eventChannelHandler = EventChannelHandler(flutterEngine.dartExecutor.binaryMessenger, "androidEventChannel")

        timer(period = 2000) {
            count++
            eventChannelHandler?.sink(count)
        }
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "channelName").setMethodCallHandler { call, result ->
            when (call.method) {
                "checkMethodChannel" -> {
                    Log.e("TAG", "configureFlutterEngine: arguments : ${call.arguments}")
                    Toast.makeText(this, "호출 성공 ", Toast.LENGTH_SHORT).show()
                }
            }
        }


        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "channelName").invokeMethod(
            "checkResult",
            JSONObject(mapOf("result" to true)).toString(),
        )
    }

    override fun onDestroy() {
        eventChannelHandler?.onCancel(null)
        eventChannelHandler = null
        super.onDestroy()
    }

}
