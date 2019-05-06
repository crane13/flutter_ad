#import "FlutterAdPlugin.h"
#import <flutter_ad/flutter_ad-Swift.h>

@implementation FlutterAdPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterAdPlugin registerWithRegistrar:registrar];
}
@end
