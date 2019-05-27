package cn.crane.flutter.flutter_ad

import cn.crane.flutter.flutter_ad.view.AView
import cn.crane.flutter.flutter_ad.view.MyViewFactory
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

class FlutterAdPlugin: MethodCallHandler {
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "flutter_ad")
      channel.setMethodCallHandler(FlutterAdPlugin())


      registrar.platformViewRegistry().registerViewFactory(AView.AVIEW, MyViewFactory(registrar.messenger(), registrar.activity()))
    }
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else {
      result.notImplemented()
    }
  }
}
