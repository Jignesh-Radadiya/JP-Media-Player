package com.jpmediaplayer.videoPlayer.cloude

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jpmediaplayer.videoPlayer.R
import com.jpmediaplayer.videoPlayer.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var emailid: EditText
    private lateinit var password: EditText


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        firebaseAuth = FirebaseAuth.getInstance()

        val binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ilogin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signup12.setOnClickListener {
            saveData()
        }
    }

    private fun saveData(){
        name=findViewById(R.id.sname)
        emailid=findViewById(R.id.semail)
        password=findViewById(R.id.spassword)


        val name=name.text.toString()
        val emailid=emailid.text.toString()
        val password=password.text.toString()



        if(name.isBlank() && emailid.isBlank() && password.isBlank())
        {
            Toast.makeText(this,"Fill all the fields", Toast.LENGTH_LONG).show()
        }

        else {
            firebaseAuth.createUserWithEmailAndPassword(emailid, password)
                .addOnCompleteListener(this) {task->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                        val storedata = DataModel(name, emailid, password)
                        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                        database = FirebaseDatabase.getInstance().reference.child("user")
                        database.child(uid).setValue(storedata).addOnSuccessListener {
                            Toast.makeText(this, "Successfully saved", Toast.LENGTH_LONG).show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                firebaseAuth.signOut()
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }, 1000)
                        }.addOnFailureListener {
                            Toast.makeText(this, "Failed to save", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
