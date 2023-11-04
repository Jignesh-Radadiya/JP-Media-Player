package com.jpmediaplayer.videoPlayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class NotificationReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            //only play next or prev song, when music list contains more than one song
            ApplicationClass.PREVIOUS -> if(PlayerActivity2.musicListPA.size > 1) prevNextSong(increment = false, context = context!!)
            ApplicationClass.PLAY -> if(PlayerActivity2.isPlaying) pauseMusic() else playMusic()
            ApplicationClass.NEXT -> if(PlayerActivity2.musicListPA.size > 1) prevNextSong(increment = true, context = context!!)
            ApplicationClass.EXIT ->{
                exitApplication()
            }
        }
    }
    private fun playMusic(){
        PlayerActivity2.isPlaying = true
        PlayerActivity2.musicService!!.mediaPlayer!!.start()
        PlayerActivity2.musicService!!.showNotification(R.drawable.pause_icon)
        PlayerActivity2.binding.playPauseBtnPA.setIconResource(R.drawable.pause_icon)
        //for handling app crash during notification play - pause btn (While app opened through intent)
        try{ NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.pause_icon) }catch (_: Exception){}
    }

    private fun pauseMusic(){
        PlayerActivity2.isPlaying = false
        PlayerActivity2.musicService!!.mediaPlayer!!.pause()
        PlayerActivity2.musicService!!.showNotification(R.drawable.play_icon)
        PlayerActivity2.binding.playPauseBtnPA.setIconResource(R.drawable.play_icon)
        //for handling app crash during notification play - pause btn (While app opened through intent)
        try{ NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.play_icon) }catch (_: Exception){}
    }

    private fun prevNextSong(increment: Boolean, context: Context){
        setSongPosition(increment = increment)
        PlayerActivity2.musicService!!.createMediaPlayer()
        Glide.with(context)
            .load(PlayerActivity2.musicListPA[PlayerActivity2.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.mipmap.ic_video_player_foreground).centerCrop())
            .into(PlayerActivity2.binding.songImgPA)
        PlayerActivity2.binding.songNamePA.text = PlayerActivity2.musicListPA[PlayerActivity2.songPosition].title
        Glide.with(context)
            .load(PlayerActivity2.musicListPA[PlayerActivity2.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.mipmap.ic_video_player_foreground).centerCrop())
            .into(NowPlaying.binding.songImgNP)
        NowPlaying.binding.songNameNP.text = PlayerActivity2.musicListPA[PlayerActivity2.songPosition].title
        playMusic()
        PlayerActivity2.fIndex = favouriteChecker(PlayerActivity2.musicListPA[PlayerActivity2.songPosition].id)
        if(PlayerActivity2.isFavourite) PlayerActivity2.binding.favouriteBtnPA.setImageResource(R.drawable.favourite_icon)
        else PlayerActivity2.binding.favouriteBtnPA.setImageResource(R.drawable.favourite_empty_icon)
    }
}