package com.jpmediaplayer.videoPlayer

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.SpannableStringBuilder
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.jpmediaplayer.videoPlayer.MusicAdapter.MyHolder
import com.jpmediaplayer.videoPlayer.databinding.DetailsViewBinding
import com.jpmediaplayer.videoPlayer.databinding.MoreFeatures2Binding
import com.jpmediaplayer.videoPlayer.databinding.MusicViewBinding

class MusicAdapter(private val context: Context, private var musicList: ArrayList<Music>, private val playlistDetails: Boolean = false,
                   private val selectionActivity: Boolean = false)
    : RecyclerView.Adapter<MyHolder>() {

    class MyHolder(binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.songNameMV
        val album = binding.songAlbumMV
        val image = binding.imageMV
        val duration = binding.songDuration
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(MusicViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.title.text = musicList[position].title
        holder.album.text = musicList[position].album
        holder.duration.text = formatDuration(musicList[position].duration)
        Glide.with(context)
            .load(musicList[position].artUri)
            .apply(RequestOptions().placeholder(R.mipmap.ic_video_player_foreground).centerCrop())
            .into(holder.image)

////request for delete
//             fun requestDeleteR(position: Int){
//            //list of videos to delete
//            val uriList: List<Uri> = listOf(Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, musicList[position].id))
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//
//                //requesting for delete permission
//                val pi = MediaStore.createDeleteRequest(context.contentResolver, uriList)
//                (context as Activity).startIntentSenderForResult(pi.intentSender, 123,
//                    null, 0, 0, 0, null)
//            }
//            else{
//                //for devices less than android 11
//                val file = File(musicList[position].path)
//                val builder = MaterialAlertDialogBuilder(context)
//                builder.setTitle("Delete Video?")
//                    .setMessage(musicList[position].title)
//                    .setPositiveButton("Yes"){ self, _ ->
//                        if(file.exists() && file.delete()){
//                            MediaScannerConnection.scanFile(context, arrayOf(file.path), null, null)
////                            updateDeleteUI(position = position)
//                        }
//                        self.dismiss()
//                    }
//                    .setNegativeButton("No"){self, _ -> self.dismiss() }
//                val delDialog = builder.create()
//                delDialog.show()
//                delDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setBackgroundColor(
//                    MaterialColors.getColor(context, R.attr.themeColor, Color.RED)
//                )
//                delDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(
//                    MaterialColors.getColor(context, R.attr.themeColor, Color.RED)
//                )
//            }
//        }
        //for play next feature
        if(!selectionActivity)
            holder.root.setOnLongClickListener {
                val customDialog = LayoutInflater.from(context).inflate(R.layout.more_features2, holder.root, false)
                val bindingMF = MoreFeatures2Binding.bind(customDialog)
                val dialog = MaterialAlertDialogBuilder(context).setView(customDialog)
                    .create()
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(0x99000000.toInt()))

                bindingMF.AddToPNBtn.setOnClickListener {
                    try {
                        if(PlayNext.playNextList.isEmpty()){
                            PlayNext.playNextList.add(PlayerActivity2.musicListPA[PlayerActivity2.songPosition])
                            PlayerActivity2.songPosition = 0
                        }

                        PlayNext.playNextList.add(musicList[position])
                        PlayerActivity2.musicListPA = ArrayList()
                        PlayerActivity2.musicListPA.addAll(PlayNext.playNextList)
                    }catch (e: Exception){
                        Snackbar.make(context, holder.root,"Play A Song First!!", 3000).show()
                    }
                    dialog.dismiss()
                }

                bindingMF.infoBtn.setOnClickListener {
                    dialog.dismiss()
                    val detailsDialog = LayoutInflater.from(context).inflate(R.layout.details_view, bindingMF.root, false)
                    val binder = DetailsViewBinding.bind(detailsDialog)
                    binder.detailTV.setTextColor(Color.WHITE)
                    binder.root.setBackgroundColor(Color.TRANSPARENT)
                    val dDialog = MaterialAlertDialogBuilder(context).setBackground(ColorDrawable(0x99000000.toInt()))
                        .setView(detailsDialog)
                        .setPositiveButton("OK"){self, _ -> self.dismiss()}
                        .setCancelable(false)
                        .create()
                    dDialog.show()
                    dDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
                    setDialogBtnBackground(context, dDialog)
                    dDialog.window?.setBackgroundDrawable(ColorDrawable(0x99000000.toInt()))
                    val str = SpannableStringBuilder().bold { append("DETAILS\n\nName: ") }
                        .append(musicList[position].title)
                        .bold { append("\n\nDuration: ") }.append(DateUtils.formatElapsedTime(musicList[position].duration/1000))
                        .bold { append("\n\nLocation: ") }.append(musicList[position].path)
                    binder.detailTV.text = str
                }

//                bindingMF.recycle.setOnClickListener {
//                    val builder = MaterialAlertDialogBuilder(context)
//
//                        .setMessage("Do you want to delete playlist?")
//                        .setPositiveButton("Yes") { dialog, _ ->
//                            MusicAdapter.music.ref.removeAt(position)
//                            dialog.dismiss()
//                        }
//                        .setNegativeButton("No") { dialog, _ ->
//                            dialog.dismiss()
//                        }
//                }


                return@setOnLongClickListener true
            }

        when{
            playlistDetails ->{
                holder.root.setOnClickListener {
                    sendIntent(ref = "PlaylistDetailsAdapter", pos = position)
                }
            }
            selectionActivity ->{
                holder.root.setOnClickListener {
                    if(addSong(musicList[position]))
                        holder.root.setBackgroundColor(ContextCompat.getColor(context, R.color.cool_pink))
                    else
                        holder.root.setBackgroundColor(ContextCompat.getColor(context, R.color.white))

                }
            }
            else ->{
                holder.root.setOnClickListener {
                when{
                    MainActivity2.search -> sendIntent(ref = "MusicAdapterSearch", pos = position)
                    musicList[position].id == PlayerActivity2.nowPlayingId ->
                        sendIntent(ref = "NowPlaying", pos = PlayerActivity2.songPosition)
                    else->sendIntent(ref="MusicAdapter", pos = position) } }
        }

         }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

//    private fun updateDeleteUI(position: Int){
//        when{
//            MainActivity2.search -> {
////                MainActivity.dataChanged = true
//                MainActivity2.musicListSearch = checkPlaylist(context)
//                musicList.removeAt(position)
//                notifyDataSetChanged()
//            }
////            refreshPlaylist() -> {
//////                MainActivity.dataChanged = true
////                MainActivity2.musicListSearch = checkPlaylist(context)
////                FoldersActivity.currentFolderVideos.removeAt(position)
////                notifyDataSetChanged()
////            }
//            else -> {
//                MainActivity2.musicListSearch.removeAt(position)
//                notifyDataSetChanged()
//            }
//        }
//    }
    fun updateMusicList(searchList : ArrayList<Music>){
        musicList = ArrayList()
        musicList.addAll(searchList)
        notifyDataSetChanged()
    }
    private fun sendIntent(ref: String, pos: Int){
        val intent = Intent(context, PlayerActivity2::class.java)
        intent.putExtra("index", pos)
        intent.putExtra("class", ref)
        ContextCompat.startActivity(context, intent, null)
    }
    private fun addSong(song: Music): Boolean{
        PlaylistActivity.musicPlaylist.ref[PlaylistDetails.currentPlaylistPos].playlist.forEachIndexed { index, music ->
            if(song.id == music.id){
                PlaylistActivity.musicPlaylist.ref[PlaylistDetails.currentPlaylistPos].playlist.removeAt(index)
                return false
            }
        }
        PlaylistActivity.musicPlaylist.ref[PlaylistDetails.currentPlaylistPos].playlist.add(song)
        return true
    }
    fun refreshPlaylist(){
        musicList = ArrayList()
        musicList = PlaylistActivity.musicPlaylist.ref[PlaylistDetails.currentPlaylistPos].playlist
        notifyDataSetChanged()
    }
}