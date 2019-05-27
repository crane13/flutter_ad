import 'dart:async';

import 'package:flutter/services.dart';

class FlutterAd {
  static const MethodChannel _channel = const MethodChannel('flutter_ad');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future register(
      {String appId,
      bool doOnIOS: true,
      doOnAndroid: true,
      enableMTA: false}) async {
    return await _channel.invokeMethod("registerApp", {
      "appId": appId,
      "iOS": doOnIOS,
      "android": doOnAndroid,
      "enableMTA": enableMTA
    });
  }
}
