package cn.crane.flutter.flutter_ad_example

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast

import io.flutter.app.FlutterActivity
import io.flutter.plugins.GeneratedPluginRegistrant
import java.util.ArrayList

class MainActivity: FlutterActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GeneratedPluginRegistrant.registerWith(this)

    if (Build.VERSION.SDK_INT >= 23) {
      checkAndRequestPermission()
    }

  }

  /**
   * ----------非常重要----------
   *
   *
   * Android6.0以上的权限适配简单示例：
   *
   *
   * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
   *
   *
   * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
   * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
   */
  @TargetApi(Build.VERSION_CODES.M)
  fun checkAndRequestPermission() {
    val lackedPermission = ArrayList<String>()
    if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) === PackageManager.PERMISSION_GRANTED)) {
      lackedPermission.add(Manifest.permission.READ_PHONE_STATE)
    }

    if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED)) {
      lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED)) {
      lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    if (lackedPermission.size != 0) {
      // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
      val requestPermissions = arrayOfNulls<String>(lackedPermission.size)
      lackedPermission.toTypedArray()
      requestPermissions(requestPermissions, 1024)
    }
  }

  private fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
    for (grantResult in grantResults) {
      if (grantResult == PackageManager.PERMISSION_DENIED) {
        return false
      }
    }
    return true
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == 1024 && !hasAllPermissionsGranted(grantResults)) {
      Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show()
      // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
      val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
      intent.data = Uri.parse("package:$packageName")
      startActivity(intent)
    }
  }
}
