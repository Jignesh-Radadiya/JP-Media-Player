package com.jpmediaplayer.videoPlayer.cloude

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.jpmediaplayer.videoPlayer.R

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    val AUDIO : Int = 0
    val VIDEO : Int = 1
    lateinit var uri : Uri
    lateinit var mStorage : StorageReference

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        val vidup = findViewById<View>(R.id.vidup) as Button
        val audup = findViewById<View>(R.id.audup) as Button

        mStorage = FirebaseStorage.getInstance().getReference("Uploads")

        audup.setOnClickListener(View.OnClickListener {
                view: View? -> val intent = Intent()
            intent.setType ("audio/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Audio"), AUDIO)
        })

        vidup.setOnClickListener(View.OnClickListener {
                view: View? -> val intent = Intent()
            intent.setType ("video/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Video"), VIDEO)
        })
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val uriTxt = findViewById<View>(R.id.uriTxt) as TextView
        if (resultCode == RESULT_OK) {
           if (requestCode == AUDIO) {
               if (data != null) {
                   uri = data.data!!
               }
                uriTxt.text = uri.toString()
                upload ()
            }else if (requestCode == VIDEO) {
                uri = data!!.data!!
                uriTxt.text = uri.toString()
                upload ()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun upload() {
        var mReference = uri.lastPathSegment?.let { mStorage.child(it) }
        try {
            mReference?.putFile(uri)?.addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? -> var url = taskSnapshot!!
                val dwnTxt = findViewById<View>(R.id.dwnTxt) as TextView
                dwnTxt.text = url.toString()
                Toast.makeText(this, "Successfully Uploaded :)", Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }

    }

}