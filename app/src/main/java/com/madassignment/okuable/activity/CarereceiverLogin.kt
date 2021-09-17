package com.madassignment.okuable.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.madassignment.okuable.R
import kotlinx.android.synthetic.main.activity_carereceiver_login.*
import kotlinx.android.synthetic.main.activity_carereceiver_register.*

class CarereceiverLogin : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    // var user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carereceiver_login)



        val backRegister: TextView = findViewById(R.id.tvBackreg)
        backRegister.setOnClickListener() {

            val intent = Intent(this, CarereceiverRegister::class.java)
            startActivity(intent)

        }

        val loginButton: Button = findViewById(R.id.btnRegister_receiver)
        loginButton.setOnClickListener() {

            when {
                TextUtils.isEmpty(
                    email_edittext_receiver_login.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@CarereceiverLogin,
                        "Please enter email.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                TextUtils.isEmpty(
                    password_edittext_receiver_login.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@CarereceiverLogin,
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
                                            this@CarereceiverLogin,
                                            "You are logging as Care Receiver!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(
                                            this@CarereceiverLogin,
                                            MainActivity2::class.java
                                        )
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        intent.putExtra("user_id", firebaseUser.uid)
                                        intent.putExtra("receiverEmail_id", receiverEmail)
                                        startActivity(intent)
                                    } else if (userType == "Care Giver") {
                                        Toast.makeText(
                                            this@CarereceiverLogin,
                                            "You are logging as Care Giver!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(
                                            this@CarereceiverLogin,
                                            MainActivity::class.java
                                        )
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        intent.putExtra("user_id", firebaseUser.uid)
                                        intent.putExtra("receiverEmail_id", receiverEmail)
                                        startActivity(intent)
                                    } else if (userType == "Admin") {
                                        Toast.makeText(
                                            this@CarereceiverLogin,
                                            "You are logging in as Admin!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(
                                            this@CarereceiverLogin,
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
                                    this@CarereceiverLogin,
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

