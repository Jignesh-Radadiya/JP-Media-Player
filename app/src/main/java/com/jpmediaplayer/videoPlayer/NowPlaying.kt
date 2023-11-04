package com.jpmediaplayer.videoPlayer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jpmediaplayer.videoPlayer.databinding.FragmentNowPlayingBinding

class NowPlaying : Fragment() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: FragmentNowPlayingBinding
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
     requireContext().theme.applyStyle(MainActivity2.currentTheme[MainActivity2.themeIndex], true)
        val view = inflater.inflate(R.layout.fragment_now_playing, container, false)
        binding = FragmentNowPlayingBinding.bind(view)
        binding.root.visibility = View.INVISIBLE
        binding.playPauseBtnNP.setOnClickListener {
            if(PlayerActivity2.isPlaying) pauseMusic() else playMusic()
        }
        binding.nextBtnNP.setOnClickListener {
            setSongPosition(increment = true)
            PlayerActivity2.musicService!!.createMediaPlayer()
            Glide.with(requireContext())
                .load(PlayerActivity2.musicListPA[PlayerActivity2.songPosition].artUri)
                .apply(RequestOptions().placeholder(R.mipmap.ic_video_player_foreground).centerCrop())
                .into(binding.songImgNP)
            binding.songNameNP.text = PlayerActivity2.musicListPA[PlayerActivity2.songPosition].title
            PlayerActivity2.musicService!!.showNotification(R.drawable.pause_icon)
            playMusic()
        }
        binding.backBtnNP.setOnClickListener {
            setSongPosition(increment = false)
            PlayerActivity2.musicService!!.createMediaPlayer()
            Glide.with(requireContext())
                .load(PlayerActivity2.musicListPA[PlayerActivity2.songPosition].artUri)
                .apply(RequestOptions().placeholder(R.mipmap.ic_video_player_foreground).centerCrop())
                .into(binding.songImgNP)
            binding.songNameNP.text = PlayerActivity2.musicListPA[PlayerActivity2.songPosition].title
            PlayerActivity2.musicService!!.showNotification(R.drawable.pause_icon)
            playMusic()
        }
        binding.root.setOnClickListener {
            val intent = Intent(requireContext(), PlayerActivity2::class.java)
            intent.putExtra("index", PlayerActivity2.songPosition)
            intent.putExtra("class", "NowPlaying")
            ContextCompat.startActivity(requireContext(), intent, null)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        if(PlayerActivity2.musicService != null){
            binding.root.visibility = View.VISIBLE
            binding.songNameNP.isSelected = true
            Glide.with(requireContext())
                .load(PlayerActivity2.musicListPA[PlayerActivity2.songPosition].artUri)
                .apply(RequestOptions().placeholder(R.drawable.music_player_icon_slash_screen).centerCrop())
                .into(binding.songImgNP)
            binding.songNameNP.text = PlayerActivity2.musicListPA[PlayerActivity2.songPosition].title
            if(PlayerActivity2.isPlaying) binding.playPauseBtnNP.setIconResource(R.drawable.pause_icon)
            else binding.playPauseBtnNP.setIconResource(R.drawable.play_icon)
        }
    }

    private fun playMusic(){
        PlayerActivity2.isPlaying = true
        PlayerActivity2.musicService!!.mediaPlayer!!.start()
        binding.playPauseBtnNP.setIconResource(R.drawable.pause_icon)
        PlayerActivity2.musicService!!.showNotification(R.drawable.pause_icon)
    }
    private fun pauseMusic(){
        PlayerActivity2.isPlaying = false
        PlayerActivity2.musicService!!.mediaPlayer!!.pause()
        binding.playPauseBtnNP.setIconResource(R.drawable.play_icon)
        PlayerActivity2.musicService!!.showNotification(R.drawable.play_icon)
    }
}