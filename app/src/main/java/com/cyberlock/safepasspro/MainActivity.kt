package com.cyberlock.safepasspro

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cyberlock.safepasspro.databinding.ActivityMainBinding
import com.yandex.mobile.ads.common.MobileAds
import kotlin.random.Random

class MainActivity : AppCompatActivity()
{
    private lateinit var binding : ActivityMainBinding
    private lateinit var appOpenAd: AppOpenAd
    private lateinit var appOpenStickyOne: AppOpenStickyOne

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {
            println(">>> YandexAds initialize")
        }

        appOpenAd = AppOpenAd()
        appOpenAd.initialize(this)

        appOpenStickyOne = AppOpenStickyOne(this, binding.banner)
        appOpenStickyOne.loadAd()

        binding.apply {
            generateButton.setOnClickListener {
                val selectedOptions = mutableListOf<Char>()
                if (checkboxLowercase.isChecked){
                    selectedOptions.addAll(('a'..'z'))
                }
                if (checkboxNumbers.isChecked){
                    selectedOptions.addAll(('0'..'9'))
                }
                if (checkboxUppercase.isChecked){
                    selectedOptions.addAll(('A'..'Z'))
                }
                if (checkboxSymbol.isChecked){
                    selectedOptions.addAll("!@#$%^&*()_-[]{}|:;,.<>?".toList())
                }
                if (selectedOptions.isEmpty())
                {
                    //Handle case when no options are selected
                    Toast.makeText(this@MainActivity , "please select at least one option." , Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val passwordLength = 8 //set your desired password length
                val randomPassword = buildString {
                    repeat(passwordLength){
                        val randomIndex = Random.nextInt(0,selectedOptions.size)
                        append(selectedOptions[randomIndex])
                    }
                }
                tvPassword.text = randomPassword
            }
            tvPassword.setOnClickListener {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("TextViewText",tvPassword.text)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this@MainActivity , "copied." , Toast.LENGTH_SHORT).show()
            }
        }
    }
}













