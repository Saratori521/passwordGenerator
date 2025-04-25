package com.cyberlock.safepasspro

import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import kotlin.math.roundToInt

class AppOpenStickyOne(
    private val context: Context,
    private val adContainer: View
) {
    private var bannerAd: BannerAdView? = null

    fun loadAd() {
        adContainer.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                adContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
                bannerAd = createAndLoadBanner()
            }
        })
    }

    private fun createAndLoadBanner(): BannerAdView {
        return (adContainer as BannerAdView).apply {
            setAdSize(calculateAdSize())
            setAdUnitId("R-M-15260243-1")
            setBannerAdEventListener(createAdListener())
            loadAd(AdRequest.Builder().build())
        }
    }

    private fun calculateAdSize(): BannerAdSize {
        val adWidthPixels = if (adContainer.width == 0) {
            context.resources.displayMetrics.widthPixels
        } else {
            adContainer.width
        }

        val adWidth = (adWidthPixels / context.resources.displayMetrics.density).roundToInt()
        return BannerAdSize.stickySize(context, adWidth)
    }

    private fun createAdListener(): BannerAdEventListener {
        return object : BannerAdEventListener {
            override fun onAdLoaded() {
                if (context is MainActivity && context.isDestroyed) {
                    bannerAd?.destroy()
                    return
                }
                println(">>> YandexAds StickyBanner onAdLoaded")
            }

            override fun onAdFailedToLoad(error: AdRequestError) {
                println(">>> YandexAds StickyBanner onAdFailedToLoad")
            }

            override fun onAdClicked() {
                println(">>> YandexAds StickyBanner onAdClicked")
            }

            override fun onLeftApplication() {
                println(">>> YandexAds StickyBanner onLeftApplication")
            }

            override fun onReturnedToApplication() {
                println(">>> YandexAds StickyBanner onReturnedToApplication")
            }

            override fun onImpression(impressionData: ImpressionData?) {
                impressionData?.let {
                    println(">>> YandexAds StickyBanner onImpression ${it.rawData}")
                }
            }
        }
    }
}