package cn.crane.flutter.flutter_ad.reward;

import android.content.Context;
import android.os.SystemClock;
import android.widget.Toast;

import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;

public class RewardUtils {

    private static RewardVideoAD rewardVideoAD;
    private static boolean adLoaded;
    private static boolean videoCached;

    private static String appId = "1101152570";
    private static String rewardId = "2090845242931421";

    public static void setAppAndRewardId(String appId, String rewardId)
    {
        RewardUtils.appId = appId;
        RewardUtils.rewardId = rewardId;
    }

    public static RewardVideoAD getRewardVideoAD(Context context) {

        if(rewardVideoAD == null)
        {
            rewardVideoAD = initReward(context);
        }
        return rewardVideoAD;
    }

    public static RewardVideoAD initReward(Context context)
    {
        rewardVideoAD = new RewardVideoAD(context, appId, rewardId, new RewardVideoADListener() {
            @Override
            public void onADLoad() {
                adLoaded = true;
                show();
            }

            @Override
            public void onVideoCached() {
                videoCached = true;
            }

            @Override
            public void onADShow() {

            }

            @Override
            public void onADExpose() {

            }

            @Override
            public void onReward() {

            }

            @Override
            public void onADClick() {

            }

            @Override
            public void onVideoComplete() {

            }

            @Override
            public void onADClose() {

            }

            @Override
            public void onError(AdError adError) {

            }
        });

        return rewardVideoAD;
    }

    public static void loadAd(Context context)
    {
        adLoaded = false;
        videoCached = false;
        getRewardVideoAD(context).loadAD();
    }

    public static void show()
    {
        if (adLoaded && rewardVideoAD != null) {//广告展示检查1：广告成功加载，此处也可以使用videoCached来实现视频预加载完成后再展示激励视频广告的逻辑
            if (!rewardVideoAD.hasShown()) {//广告展示检查2：当前广告数据还没有展示过
                long delta = 1000;//建议给广告过期时间加个buffer，单位ms，这里demo采用1000ms的buffer
                //广告展示检查3：展示广告前判断广告数据未过期
                if (SystemClock.elapsedRealtime() < (rewardVideoAD.getExpireTimestamp() - delta)) {
                    rewardVideoAD.showAD();
                    adLoaded = false;
                } else {
//                    Toast.makeText(this, "激励视频广告已过期，请再次请求广告后进行广告展示！", Toast.LENGTH_LONG).show();
                }
            } else {
//                Toast.makeText(this, "此条广告已经展示过，请再次请求广告后进行广告展示！", Toast.LENGTH_LONG).show();
            }
        } else {
//            Toast.makeText(this, "成功加载广告后再进行广告展示！", Toast.LENGTH_LONG).show();
        }
    }
}
