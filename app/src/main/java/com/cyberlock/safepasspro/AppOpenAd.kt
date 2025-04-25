package com.cyberlock.safepasspro

import android.app.Activity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.yandex.mobile.ads.appopenad.AppOpenAd
import com.yandex.mobile.ads.appopenad.AppOpenAdEventListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoader
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

class AppOpenAd {

    private var appOpenAd: AppOpenAd? = null
    private var isAdShownOnColdStart = false

    fun initialize(activity: Activity) {
        loadAppOpenAd(activity)

        val processLifecycleObserver = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                showAppOpenAd(activity)
            }
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(processLifecycleObserver)
    }

    private fun loadAppOpenAd(activity: Activity) {
        val appOpenAdLoader = AppOpenAdLoader(activity)
        val appOpenAdLoadListener = object : AppOpenAdLoadListener {
            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                this@AppOpenAd.appOpenAd = appOpenAd

                if (!isAdShownOnColdStart) {
                    showAppOpenAd(activity)
                    isAdShownOnColdStart = true
                }

                println(">>> YandexAds onAdLoaded")
            }

            override fun onAdFailedToLoad(error: AdRequestError) {
                println(">>> YandexAds onAdFailedToLoad")
            }
        }
        appOpenAdLoader.setAdLoadListener(appOpenAdLoadListener)

        val adUnitId = "R-M-15260243-2"
        val adRequestConfiguration = AdRequestConfiguration.Builder(adUnitId).build()
        appOpenAdLoader.loadAd(adRequestConfiguration)

    }

    fun showAppOpenAd(activity: Activity) {
        val appOpenAdEventListener = object : AppOpenAdEventListener {
            override fun onAdShown() {
                println(">>> YandexAds onAdShown()")
            }

            override fun onAdFailedToShow(adError: AdError) {
                clearAppOpenAd()
                loadAppOpenAd(activity)
                println(">>> YandexAds onAdFailedToShow")
            }

            override fun onAdDismissed() {
                clearAppOpenAd()
                loadAppOpenAd(activity)
                println(">>> YandexAds onAdDismissed()")
            }

            override fun onAdClicked() {
                println(">>> YandexAds onAdClicked()")
            }

            override fun onAdImpression(impressionData: ImpressionData?) {
                impressionData?.rawData?.let {
                    println(">>> YandexAds onAdImpression() $it")
                }
            }
        }

        appOpenAd?.setAdEventListener(appOpenAdEventListener)
        appOpenAd?.show(activity)
    }

    private fun clearAppOpenAd() {
        appOpenAd?.setAdEventListener(null)
        appOpenAd = null
    }
}