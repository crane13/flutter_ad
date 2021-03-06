import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

const AVIEW = 'plugins.crane.view/AView';

class BannerView extends StatefulWidget {

  final void Function(String) listener;

  String title;

  dynamic params;

  BannerView({Key key, this.title, this.params, this.listener}) : super(key: key);

  BannerViewState createState() => new BannerViewState();
}

class BannerViewState extends State<BannerView> {
  var _eventChannel = const EventChannel(AVIEW, const StandardMethodCodec());

  StreamSubscription _subscription = null;

  @override
  void initState() {
    super.initState();
    if (_subscription == null) {
      _subscription = _eventChannel
          .receiveBroadcastStream()
          .listen(_onEvent, onError: _onError);
    }
  }

  void _onEvent(Object value) {
    String event = value.toString();
    if (event != null && event.length > 0) {
      print('_onEvent == $event');
      widget.listener(event);
    }
  }

  void _onError(dynamic) {}

  @override
  Widget build(BuildContext context) {
    if (Platform.isAndroid) {
      return getAdViewAndroid();
    } else if (Platform.isIOS) {
      return getAdViewIOS();
    }
    return null;
  }

  Widget getAdViewAndroid() {
    return new ConstrainedBox(
      constraints: BoxConstraints(maxHeight: 60),
      child: Padding(
          padding: EdgeInsets.fromLTRB(10, 0, 10, 0),
          child: new AndroidView(
            viewType: AVIEW,
            creationParams: widget.params,
            creationParamsCodec: const StandardMessageCodec(),
          )),
    );
  }

  Widget getAdViewIOS() {
    return new ConstrainedBox(
      constraints: BoxConstraints(maxHeight: 60),
      child: Padding(
          padding: EdgeInsets.fromLTRB(10, 0, 10, 0),
          child: new UiKitView(
            viewType: AVIEW,
            creationParams: widget.params,
            creationParamsCodec: const StandardMessageCodec(),
          )),
    );
  }

  @override
  void dispose() {
    if (_subscription != null) {
      _subscription.cancel();
    }
    super.dispose();
  }
}
