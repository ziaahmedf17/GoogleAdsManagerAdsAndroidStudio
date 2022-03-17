package com.ziaahmed17dev.googleadmanagermobileads_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

public class MainActivity extends AppCompatActivity {

    private AdManagerAdView bannerAd;
    private AdManagerInterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadBannerAd();
                loadInterstitialAd();
                loadNativeAd();
            }
        });
    }

    private void loadNativeAd() {
        MobileAds.initialize(this);
        AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.native_id_admob))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().build();
                        TemplateView template = findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);
                    }
                })
                .build();

        adLoader.loadAd(new AdManagerAdRequest.Builder().build());
    }

    private void loadInterstitialAd() {
        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();

        AdManagerInterstitialAd.load(this,getString(R.string.interstitial_id_admob), adRequest,
                new AdManagerInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                        // The mAdManagerInterstitialAd reference will be null until
                        // an ad is loaded.
                        interstitialAd = interstitialAd;
                        final AdManagerInterstitialAd interstitialAds = interstitialAd;

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                Log.i("TAG", "onAdLoaded");
                                if (interstitialAds != null) {
                                    interstitialAds.show(MainActivity.this);
                                } else {
                                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                                }
                            }
                        }, 1000*30);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("TAG", loadAdError.getMessage());
                        interstitialAd = null;
                    }
                });
    }

    private void loadBannerAd() {
        bannerAd = findViewById(R.id.bannerAd);
        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        bannerAd.loadAd(adRequest);
    }
}