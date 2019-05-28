import 'dart:async';

import 'package:flutter/services.dart';
//import 'package:permission_handler/permission_handler.dart';

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

//  static showRewardAd(String appId, String rewardId) async {
//    return await _channel.invokeMethod("showRewardAd", {
//      "appId": appId,
//      "rewardId": rewardId,
//    });
//  }
  static showRewardAd(dynamic arguments) async {
    return await _channel.invokeMethod("showRewardAd", arguments);
  }

  static registerAppId(String admob_appId) async {
    return await _channel.invokeMethod("registerAdmobId", {
      'admob_appId':admob_appId
    });
  }

//  static Future<PermissionStatus> requestPermission(PermissionGroup permission) async {
//    final List<PermissionGroup> permissions = <PermissionGroup>[permission];
//    final Map<PermissionGroup, PermissionStatus> permissionRequestResult =
//    await PermissionHandler().requestPermissions(permissions);
//
//    return permissionRequestResult[permission];
////    setState(() {
////      print(permissionRequestResult);
////      _permissionStatus = permissionRequestResult[permission];
////      print(_permissionStatus);
////    });
//  }
}
