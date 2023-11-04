package com.jpmediaplayer.videoPlayer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jpmediaplayer.videoPlayer.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(MainActivity.themesList[MainActivity.themeIndex])
        val binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "About"
        binding.aboutText.text = "Developed By: JP Media Player \n\n JP Media Player is a powerful and user-friendly media player application designed to provide you with an exceptional media playback experience.\n\n" +
                "\n\nContact Us \n\n We value your feedback and are here to assist you with any questions or issues you may encounter while using our app.\n`" +
                "\nEmail : \n -->ppipaliya446@rku.ac.in \n -->jradadiya480@rku.ac.in\n" + "\n\nCopyright © 2023 JP Media Player.\n All Rights Reserved."
    }
}