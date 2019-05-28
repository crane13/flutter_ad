import Flutter
import UIKit

public class SwiftFlutterAdPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flutter_ad", binaryMessenger: registrar.messenger())
    let instance = SwiftFlutterAdPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)

    //AViewFactory.registerWith(registry: registrar, viewController:currentViewController()
  let pluginKey = Constant.VIEW_NAME
 let messenger = registrar.messenger() as! (NSObject & FlutterBinaryMessenger)
    registrar.register(UIActivityIndicatorFactory(messenger:messenger, viewController: UIApplication.shared.keyWindow?.rootViewController,withId: pluginKey);

                                                                    )
  }




  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
