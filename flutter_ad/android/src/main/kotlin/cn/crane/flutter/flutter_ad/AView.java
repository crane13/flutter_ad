package cn.crane.flutter.flutter_ad;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;

import java.util.Locale;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

public class AView implements PlatformView, MethodChannel.MethodCallHandler {

    public static final String TAG = AView.class.getSimpleName();

    public static final String AVIEW = "plugins.crane.view/AView";

    private final MethodChannel methodChannel;
    private final EventChannel eventChannel;
    private UnifiedBannerView mBannerAd;
    private AdView adView;

    private LinearLayout linearLayout;

    private String gdt_appId;
    private String gdt_bannerId;
    private String admob_appId;
    private String admob_bannerId;

    AView(Context context, BinaryMessenger messenger, int id, Map<String, Object> params) {
        linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (params != null) {
            if (params.containsKey("gdt_appId")) {
                gdt_appId = params.get("gdt_appId").toString();
            }
            if (params.containsKey("gdt_bannerId")) {
                gdt_bannerId = params.get("gdt_bannerId").toString();
            }
            if (params.containsKey("admob_appId")) {
                admob_appId = params.get("admob_appId").toString();
            }
            if (params.containsKey("admob_bannerId")) {
                admob_bannerId = params.get("admob_bannerId").toString();
            }
        }


        if(isGDT(context))
        {
            getBanner((Activity) context).loadAD();
        }else {
            MobileAds.initialize(context, admob_appId);
            AdRequest adRequest = new AdRequest.Builder().build();
            getAdBanner((Activity) context).loadAd(adRequest);
        }





        methodChannel = new MethodChannel(messenger, AVIEW + "_$id");
        eventChannel = new EventChannel(messenger, AVIEW);
        methodChannel.setMethodCallHandler(this);
    }


    @Override
    public View getView() {
        return linearLayout;
    }

    @Override
    public void dispose() {
        if (mBannerAd != null) {
            try {
                mBannerAd.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private AdView getAdBanner(Activity activity)
    {
        if(adView != null)
        {
            linearLayout.removeAllViews();
            adView.destroy();
        }
        adView = new AdView(activity);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(admob_bannerId);
        adView.setAdListener(new AdListener(){
            public void onAdClosed() {
                onEventAdmob("onAdClosed");
            }

            public void onAdFailedToLoad(int var1) {
                onEventAdmob("onAdFailedToLoad");
            }

            public void onAdLeftApplication() {
                onEventAdmob("onAdLeftApplication");
            }

            public void onAdOpened() {
                onEventAdmob("onAdOpened");
            }

            public void onAdLoaded() {
                onEventAdmob("onAdLoaded");
            }

            public void onAdClicked() {
                onEventAdmob("onAdClicked");
            }

            public void onAdImpression() {
                onEventAdmob("onAdImpression");
            }
        });

        return adView;
    }

    private UnifiedBannerView getBanner(Activity context) {
        if(this.mBannerAd != null){
            linearLayout.removeAllViews();
            mBannerAd.destroy();
        }
        this.mBannerAd = new UnifiedBannerView(context, gdt_appId, gdt_bannerId,  new UnifiedBannerADListener() {
            @Override
            public void onNoAD(AdError adError) {
                onEventGDT("onNoAD");
            }

            @Override
            public void onADReceive() {
                onEventGDT("onADReceive");
            }

            @Override
            public void onADExposure() {
                onEventGDT("onADExposure");
            }

            @Override
            public void onADClosed() {
                onEventGDT("onADClosed");
            }

            @Override
            public void onADClicked() {
                onEventGDT("onADClicked");
            }

            @Override
            public void onADLeftApplication() {
                onEventGDT("onADLeftApplication");
            }

            @Override
            public void onADOpenOverlay() {
                onEventGDT("onADOpenOverlay");
            }

            @Override
            public void onADCloseOverlay() {
                onEventGDT("onADCloseOverlay");
            }
        });
        mBannerAd.setRefresh(30);
        linearLayout.removeAllViews();
        linearLayout.addView(mBannerAd);
        return this.mBannerAd;
    }

    private Context getActivity() {
        if (getView() != null) {
            return getView().getContext();
        }
        return null;
    }


    private void  chooseWhich()
    {
//        canGDT = zh && hasPermission && hasId;
//        canAdmob = hasappid && hasbannerId;
//
//        if(zh)
//        {
//            if(canGDT)
//            {
//                return gdt;
//            }else {
//                return Admob;
//            }
//        }else {
//            if(canAdmob)
//            {
//                return Admob;
//            }else {
//                return gdt;
//            }
//        }
    }

    private boolean isGDT(Context context)
    {
        boolean isGDT = true;

        boolean canGDT = !TextUtils.isEmpty(gdt_appId)
                && !TextUtils.isEmpty(gdt_bannerId)
                && hasPermission(context);

        boolean canAdmob =!TextUtils.isEmpty(admob_appId)
            && !TextUtils.isEmpty(admob_bannerId);

        if(Locale.getDefault().getLanguage().contains("zh"))
        {
            isGDT = canGDT;
        }else {
            isGDT = !canAdmob;
        }
        Log.v(TAG, "isGDT ==== " + isGDT);
        return isGDT;
    }

    private boolean hasPermission(Context context)
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if(context != null)
            {
                return context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                        && context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            }else {
                return false;
            }

        }else {
            return true;
        }
    }

    private void onEventGDT(String event) {
        if(!TextUtils.isEmpty(event))
        {
            if (!event.contains("GDT_banner_")) {
                event = "GDT_banner_" + event;
            }
            onEvent(event);
        }
    }
    private void onEventAdmob(String event) {
        if(!TextUtils.isEmpty(event))
        {
            if (!event.contains("Admob_banner_")) {
                event = "Admob_banner_" + event;
            }
            onEvent(event);
        }
    }
    private void onEvent(String event) {
        if (!TextUtils.isEmpty(event) && eventChannel != null) {
            final String finalEvent = event;
            eventChannel.setStreamHandler(new EventChannel.StreamHandler() {
                @Override
                public void onListen(Object o, EventChannel.EventSink eventSink) {
                    if (eventSink != null) {
                        eventSink.success(finalEvent);
                    }
                }

                @Override
                public void onCancel(Object o) {

                }
            });
        }
    }


    @Override
    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {

    }
}
