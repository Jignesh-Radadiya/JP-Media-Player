 package com.jpmediaplayer.videoPlayer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaMetadataRetriever
import androidx.appcompat.app.AlertDialog
import com.google.android.material.color.MaterialColors
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess


data class Music(val id:String, val title:String, val album:String ,val artist:String, val duration: Long = 0, val path: String,
val artUri:String)

class Playlist{
    lateinit var name: String
    lateinit var playlist: ArrayList<Music>
    lateinit var createdBy: String
    lateinit var createdOn: String
}
class MusicPlaylist{
    var ref: ArrayList<Playlist> = ArrayList()
}

fun formatDuration(duration: Long):String{
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) -
            minutes*TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
    return String.format("%02d:%02d", minutes, seconds)
}
fun getImgArt(path: String): ByteArray? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}
fun setSongPosition(increment: Boolean){
    if(!PlayerActivity2.repeat){
        if(increment)
        {
            if(PlayerActivity2.musicListPA.size - 1 == PlayerActivity2.songPosition)
                PlayerActivity2.songPosition = 0
            else ++PlayerActivity2.songPosition
        }else{
            if(0 == PlayerActivity2.songPosition)
                PlayerActivity2.songPosition = PlayerActivity2.musicListPA.size-1
            else --PlayerActivity2.songPosition
        }
    }
}


fun exitApplication(){
    if(PlayerActivity2.musicService != null){
        PlayerActivity2.musicService!!.audioManager.abandonAudioFocus(PlayerActivity2.musicService)
        PlayerActivity2.musicService!!.stopForeground(true)
        PlayerActivity2.musicService!!.mediaPlayer!!.release()
        PlayerActivity2.musicService = null}
    exitProcess(1)
}

fun favouriteChecker(id: String): Int{
    PlayerActivity2.isFavourite = false
    FavouriteActivity.favouriteSongs.forEachIndexed { index, music ->
        if(id == music.id){
            PlayerActivity2.isFavourite = true
            return index
        }
    }
    return -1
}
 fun checkPlaylist(playlist: ArrayList<Music>): ArrayList<Music>{
     playlist.forEachIndexed { index, music ->
         val file = File(music.path)
         if(!file.exists())
             playlist.removeAt(index)
     }
     return playlist
 }

 fun setDialogBtnBackground(context: Context, dialog: AlertDialog){
     //setting button text
     dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)?.setTextColor(
         MaterialColors.getColor(context, R.attr.dialogTextColor, Color.WHITE)
     )
     dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
         MaterialColors.getColor(context, R.attr.dialogTextColor, Color.WHITE)
     )

     //setting button background
     dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)?.setBackgroundColor(
         MaterialColors.getColor(context, R.attr.dialogBtnBackground, Color.RED)
     )
     dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)?.setBackgroundColor(
         MaterialColors.getColor(context, R.attr.dialogBtnBackground, Color.RED)
     )
 }

 fun getMainColor(img: Bitmap): Int {
     val newImg = Bitmap.createScaledBitmap(img, 1,1 , true)
     val color = newImg.getPixel(0, 0)
     newImg.recycle()
     return color
 }