package cn.crane.flutter.flutter_ad

import cn.crane.flutter.flutter_ad.reward.RewardUtils
import cn.crane.flutter.flutter_ad.view.AView
import cn.crane.flutter.flutter_ad.view.MyViewFactory
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import android.content.Context
import cn.crane.flutter.flutter_ad.reward.RewardUtils_admob
import com.google.android.gms.ads.MobileAds
import java.util.*


class FlutterAdPlugin : MethodCallHandler {
    companion object {
        private var context: Context? = null

        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "flutter_ad")
            channel.setMethodCallHandler(FlutterAdPlugin())
            context = registrar.activity()
            registrar.platformViewRegistry().registerViewFactory(AView.AVIEW, MyViewFactory(registrar.messenger(), registrar.activity()))
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "registerAdmobId") {
            MobileAds.initialize(context, call.argument("admob_appId"))
        } else if (call.method == "showRewardAd") {
            if(Locale.getDefault().language.contains("zh"))
            {
                RewardUtils.setAppAndRewardId(call.argument("gdt_appId"), call.argument("gdt_rewardId"))
                RewardUtils.loadAd(context)
            }else{
                RewardUtils_admob.setAppAndRewardId(call.argument("admob_appId"), call.argument("admob_rewardId"))
                RewardUtils_admob.loadAd(context)
            }

        } else {
            result.notImplemented()
        }
    }
}
