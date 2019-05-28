package cn.crane.flutter.flutter_ad.reward;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class RewardUtils_admob {

    private static RewardedAd rewardVideoAD;
    private static boolean adLoaded;
    private static boolean videoCached;

    private static String appId = "1101152570";
    private static String rewardId = "ca-app-pub-3940256099942544/5224354917";

    public static void setAppAndRewardId(String appId, String rewardId)
    {
        RewardUtils_admob.appId = appId;
        RewardUtils_admob.rewardId = rewardId;
    }

    public static RewardedAd getRewardVideoAD(Context context) {

        if(rewardVideoAD == null)
        {
            rewardVideoAD = initReward(context);
        }
        return rewardVideoAD;
    }

    public static RewardedAd initReward(final Context context)
    {
        rewardVideoAD = new RewardedAd(context, rewardId);

        return rewardVideoAD;
    }

    public static void loadAd(final Context context)
    {
        adLoaded = false;
        videoCached = false;

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                show(context);
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };

        getRewardVideoAD(context).loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    public static void show(Context context)
    {
        if(rewardVideoAD != null && rewardVideoAD.isLoaded())
        {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                public void onRewardedAdClosed() {
                    // Ad closed.
                }

                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                }

                public void onRewardedAdFailedToShow(int errorCode) {
                    // Ad failed to display
                }
            };
            rewardVideoAD.show((Activity) context, adCallback);
        }
    }
}
