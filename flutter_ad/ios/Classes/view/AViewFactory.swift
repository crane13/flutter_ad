//
//  AViewFactory.swift
//  Runner
//
//  Created by ruifeng.yu on 2019/5/8.
//  Copyright © 2019 The Chromium Authors. All rights reserved.
//

import Flutter
import UIKit



public class AViewFactory : NSObject, FlutterPlatformViewFactory {
    
    static func registerWith(registry:FlutterPluginRegistry, viewController:UIViewController) {
        let pluginKey = Constant.VIEW_NAME;
        
        if (registry.hasPlugin(pluginKey)) {return};
        let registrar = registry.registrar(forPlugin: pluginKey);
        let messenger = registrar.messenger() as! (NSObject & FlutterBinaryMessenger)
        registrar.register(UIActivityIndicatorFactory(messenger:messenger, viewController: viewController),withId: pluginKey);
        
    }
    
    var messenger: FlutterBinaryMessenger!
    var controller: UIViewController!
    
    public func create(withFrame frame: CGRect, viewIdentifier viewId: Int64, arguments args: Any?) -> FlutterPlatformView {
        let uiActivityIndicatorController = UIActivityIndicatorController(withFrame: frame, viewIdentifier: viewId, arguments: args, binaryMessenger: messenger)
         var appId: String = args["appId"];
                  var bannerPosID: String = args["bannerPosID"];
                  uiActivityIndicatorController.setAppIds(appId:appId,bannerPosID:bannerPosID );
        uiActivityIndicatorController.loadAd(viewController: self.controller)

        return uiActivityIndicatorController
    }
    
    @objc public init(messenger: (NSObject & FlutterBinaryMessenger)?, viewController: UIViewController) {
        super.init()
        self.messenger = messenger
        self.controller = viewController
    }
    
    public func createArgsCodec() -> FlutterMessageCodec & NSObjectProtocol {
        return FlutterStandardMessageCodec.sharedInstance()
    }
}

public class UIActivityIndicatorController: NSObject, FlutterPlatformView, FlutterStreamHandler, GDTUnifiedBannerViewDelegate {
    
    var controller: UIViewController!

     var appId: String!;
     var bannerPosID: String!;
    
    fileprivate var channel: FlutterMethodChannel!
    fileprivate var eventChannel: FlutterEventChannel!
    private var bannerView: GDTUnifiedBannerView!
    
    private var event : NSString!
    
    public init(withFrame frame: CGRect, viewIdentifier viewId: Int64, arguments args: Any?, binaryMessenger: FlutterBinaryMessenger) {
        super.init()
        
        //        let params = args as! NSDictionary
        //        let hexColor = params["hexColor"] as! String
        //        let hidesWhenStopped = params["hidesWhenStopped"] as! Bool
        //
        //        self.indicator = UIActivityIndicatorView()
        //        self.indicator.activityIndicatorViewStyle = .whiteLarge
        //        self.indicator.color = UIColor(hex: hexColor)
        //        self.indicator.hidesWhenStopped = hidesWhenStopped
        //
        //        self.viewId = viewId
        //        self.channel = FlutterMethodChannel(name: "plugins/activity_indicator_\(viewId)", binaryMessenger: binaryMessenger)
        
        self.channel = FlutterMethodChannel(name: Constant.VIEW_NAME, binaryMessenger: binaryMessenger)
        
        self.eventChannel = FlutterEventChannel(name: Constant.VIEW_NAME, binaryMessenger: binaryMessenger)
        
        self.channel.setMethodCallHandler({
            [weak self]
            (call: FlutterMethodCall, result: @escaping FlutterResult) -> Void in
            if let this = self {
                this.onMethodCall(call: call, result: result)
            }
        })
        
        //        self.loadAd()
    }

      func setAppIds(appId: String, bannerPosID:String) {
            self.appId = appId;
            self.bannerPosID = bannerPosID;
        }

    func loadAd(viewController: UIViewController) {
        self.controller = viewController
        self.removeAdFromSuperview()
        self.initMobBannerView()
        self.bannerView.loadAdAndShow()
        
    }
    
    func initMobBannerView() {
        print(#function)
        if bannerView == nil {
            let rect = CGRect.init(origin: .zero, size:CGSize.init(width: 375, height: 60))
            bannerView = GDTUnifiedBannerView.init(frame: rect, appId: appId, placementId: bannerPosID, viewController: self.controller)
            bannerView.delegate = self
        }
    }
    
    private func removeAdFromSuperview() {
        if let view = bannerView {
            //            bannerView.delegate = nil
            view.removeFromSuperview()
            bannerView = nil
        }
    }
    
    public func view() -> UIView {
        return self.bannerView
    }
    
    private func sendEvent(event : NSString)
    {
        print(event)
        self.event = event
        self.eventChannel.setStreamHandler(self)
    }
    
    public func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        if(self.event != nil)
        {
            events(self.event)
        }
        return nil;
    }
    
    public func onCancel(withArguments arguments: Any?) -> FlutterError? {
        return nil;
    }
    
    func onMethodCall(call: FlutterMethodCall, result: @escaping FlutterResult) {
        let method = call.method
        if method == "start" {
            //            self.indicator.startAnimating()
        } else if method == "stop" {
            //            self.indicator.stopAnimating()
        }
    }
    
    private func unifiedBannerViewDidLoad(_ unifiedBannerView: GDTUnifiedBannerView) {
        self.sendEvent(event: "GDT_Banner_DidLoad")
        
    }
    
    private func unifiedBannerViewFailed(toLoad unifiedBannerView: GDTUnifiedBannerView, error: Error) {
        self.sendEvent(event: "GDT_Banner_Failed")
        
    }
    
    func unifiedBannerViewWillExpose(_ unifiedBannerView: GDTUnifiedBannerView) {
        self.sendEvent(event: "GDT_Banner_WillExpose")
        
    }
    
    func unifiedBannerViewClicked(_ unifiedBannerView: GDTUnifiedBannerView) {
        self.sendEvent(event: "GDT_Banner_Clicked")
        
    }
    
    func unifiedBannerViewWillPresentFullScreenModal(_ unifiedBannerView: GDTUnifiedBannerView) {
        
        self.sendEvent(event: "GDT_Banner_WillPresentFullScreenModal")
        
    }
    
    func unifiedBannerViewDidPresentFullScreenModal(_ unifiedBannerView: GDTUnifiedBannerView) {
        self.sendEvent(event: "GDT_Banner_DidPresentFullScreenModal")
        
    }
    
    func unifiedBannerViewWillDismissFullScreenModal(_ unifiedBannerView: GDTUnifiedBannerView) {
        self.sendEvent(event: "GDT_Banner_WillDismissFullScreenModal")
        
    }
    
    func unifiedBannerViewDidDismissFullScreenModal(_ unifiedBannerView: GDTUnifiedBannerView) {
        self.sendEvent(event: "GDT_Banner_DismissFullScreenModal")
        
    }
    
    func unifiedBannerViewWillLeaveApplication(_ unifiedBannerView: GDTUnifiedBannerView) {
        self.sendEvent(event: "GDT_Banner_WillLeaveApplication")
        
    }
    
    func unifiedBannerViewWillClose(_ unifiedBannerView: GDTUnifiedBannerView) {
        self.sendEvent(event: "GDT_Banner_WillClose")
        
    }
}

