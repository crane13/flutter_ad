import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_ad/flutter_ad.dart';
import 'package:flutter_ad/view/BannerView.dart';
//import 'package:permission_handler/permission_handler.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();

//    FlutterAd.requestPermission(PermissionGroup.phone).then((permissionStatus) {
//      if (permissionStatus == PermissionStatus.granted) {
//        FlutterAd.showRewardAd("1101152570", "2090845242931421");
//      }
//    });

    new Timer(Duration(seconds: 1), () {
//      FlutterAd.showRewardAd("1101152570", "2090845242931421");

      FlutterAd.showRewardAd({
        'gdt_appId':'1101152570',
        'gdt_rewardId':'2090845242931421',
        'admob_appId':'ca-app-pub-3940256099942544~3347511713',
        'admob_rewardId':'ca-app-pub-3940256099942544/5224354917'
      });
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await FlutterAd.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });

    FlutterAd.registerAppId("ca-app-pub-3940256099942544~3347511713");
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: <Widget>[
              BannerView(
                params: {
                  'gdt_appId': '1101152570',
                  'gdt_bannerId': '4080052898050840',
                  'admob_appId': 'ca-app-pub-3940256099942544~3347511713',
                  'admob_bannerId': 'ca-app-pub-3940256099942544/6300978111',
                },
              ),
              Text('Running on: $_platformVersion\n')
            ],
          ),
        ),
      ),
    );
  }
}
