package com.jpmediaplayer.videoPlayer.cloude

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.jpmediaplayer.videoPlayer.R
import com.jpmediaplayer.videoPlayer.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private  lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mail1: EditText
    private lateinit var pass1: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        mail1 = findViewById(R.id.semail)
        pass1 = findViewById(R.id.spassword)

        binding.isignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.button2.setOnClickListener {

            mail1 = findViewById(R.id.semail)
            pass1 = findViewById(R.id.spassword)

            val emailid=mail1.text.toString()
            val Password=pass1.text.toString()

            if (emailid.isNotEmpty() && Password.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(emailid, Password)
                    .addOnCompleteListener(this) {task->
                        if (task.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }




}