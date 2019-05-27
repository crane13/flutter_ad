package cn.crane.flutter.flutter_ad.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;

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

    private View linearLayout;

    private String appId;
    private String bannerPosID;

    AView(Context context, BinaryMessenger messenger, int id, Map<String, Object> params) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (params != null) {
            if (params.containsKey("appId")) {
                appId = params.get("appId").toString();
            }
            if (params.containsKey("bannerPosID")) {
                bannerPosID = params.get("bannerPosID").toString();
            }
        }

        loadMiAd(context, linearLayout);
        this.linearLayout = linearLayout;

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
//                mBannerAd.recycle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadMiAd(Context context, LinearLayout linearLayout) {
        try {
            mBannerAd = new UnifiedBannerView((Activity) context, appId, bannerPosID, new UnifiedBannerADListener() {
                @Override
                public void onNoAD(AdError adError) {
                    onEvent("onNoAD");
                }

                @Override
                public void onADReceive() {
                    onEvent("onADReceive");
                }

                @Override
                public void onADExposure() {
                    onEvent("onADExposure");
                }

                @Override
                public void onADClosed() {
                    onEvent("onADClosed");
                }

                @Override
                public void onADClicked() {
                    onEvent("onADClicked");
                }

                @Override
                public void onADLeftApplication() {
                    onEvent("onADLeftApplication");
                }

                @Override
                public void onADOpenOverlay() {
                    onEvent("onADOpenOverlay");
                }

                @Override
                public void onADCloseOverlay() {
                    onEvent("onADCloseOverlay");
                }
            });
            //设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
            mBannerAd.setRefresh(30);
            /* 发起广告请求，收到广告数据后会展示数据     */
            mBannerAd.loadAD();
            linearLayout.addView(mBannerAd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Context getActivity() {
        if (getView() != null) {
            return getView().getContext();
        }
        return null;
    }


    private void onEvent(String event) {
        if (!TextUtils.isEmpty(event) && eventChannel != null) {
            if (!event.contains("GDT_banner_")) {
                event = "GDT_banner_" + event;
            }
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
