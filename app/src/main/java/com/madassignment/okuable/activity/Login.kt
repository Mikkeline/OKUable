package com.madassignment.okuable.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.madassignment.okuable.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class Login : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    lateinit var auth: FirebaseAuth
    //var user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()




        val backRegister: TextView = findViewById(R.id.tvBackreg)
        backRegister.setOnClickListener() {

            val intent = Intent(this, Register::class.java)
            startActivity(intent)

        }

        val forgetPassword: TextView = findViewById(R.id.tvForget)
        forgetPassword.setOnClickListener(){
            if (email_edittext_receiver_login.text.toString().isEmpty()){
                email_edittext_receiver_login.error = "Please enter email."
                email_edittext_receiver_login.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email_edittext_receiver_login.text.toString()).matches()){
                email_edittext_receiver_login.error = "Please enter a valid email."
                email_edittext_receiver_login.requestFocus()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email_edittext_receiver_login.text.toString()).addOnCompleteListener{
                    task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Email is sent!", Toast.LENGTH_SHORT).show()
                }

            }
        }

        val loginButton: Button = findViewById(R.id.btnRegister_receiver)
        loginButton.setOnClickListener() {

            when {
                TextUtils.isEmpty(
                    email_edittext_receiver_login.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@Login,
                        "Please enter email.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                TextUtils.isEmpty(
                    password_edittext_receiver_login.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@Login,
                        "Please enter password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val receiverEmail: String =
                        email_edittext_receiver_login.text.toString().trim { it <= ' ' }
                    val receiverPassword: String =
                        password_edittext_receiver_login.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(receiverEmail, receiverPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = FirebaseAuth.getInstance().currentUser

                                val uid = user!!.uid
                                val firebaseUser: FirebaseUser = task.result!!.user!!

                                mDatabase = FirebaseDatabase.getInstance().getReference("Users")
                                mDatabase.child(uid).child("userType").get().addOnSuccessListener {

                                    var userType = it.value.toString()

                                    if (userType == "Care Receiver") {
                                        Toast.makeText(
                                            this@Login,
                                            "You are logging as Care Receiver!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(
                                            this@Login,
                                            MainActivity2::class.java
                                        )
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        intent.putExtra("user_id", firebaseUser.uid)
                                        intent.putExtra("receiverEmail_id", receiverEmail)
                                        startActivity(intent)
                                    } else if (userType == "Care Giver") {
                                        Toast.makeText(
                                            this@Login,
                                            "You are logging as Care Giver or Others!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(
                                            this@Login,
                                            MainActivity::class.java
                                        )
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        intent.putExtra("user_id", firebaseUser.uid)
                                        intent.putExtra("receiverEmail_id", receiverEmail)
                                        startActivity(intent)
                                    } else if (userType == "Admin") {
                                        Toast.makeText(
                                            this@Login,
                                            "You are logging in as Admin!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(
                                            this@Login,
                                            MainActivity3::class.java
                                        )
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        intent.putExtra("user_id", firebaseUser.uid)
                                        intent.putExtra("receiverEmail_id", receiverEmail)
                                        startActivity(intent)
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    this@Login,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }
}