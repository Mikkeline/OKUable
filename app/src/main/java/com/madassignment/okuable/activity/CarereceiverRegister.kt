package com.madassignment.okuable.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_carereceiver_register.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.madassignment.okuable.R
import android.content.ContentValues.TAG
import android.text.TextUtils
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.madassignment.okuable.fragment.*

class CarereceiverRegister : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    lateinit var mDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carereceiver_register)


        mDatabase = FirebaseDatabase.getInstance().getReference("Users")

        val haveAccount: TextView = findViewById(R.id.tvHaveaccount)
        haveAccount.setOnClickListener() {
            Log.d("CarereceiverRegister", "Try to show login activity")

            val intent = Intent(this, CarereceiverLogin::class.java)
            startActivity(intent)

        }

        val registerButton: Button = findViewById(R.id.btnRegister_receiver)
        registerButton.setOnClickListener() {

            when {
                TextUtils.isEmpty(
                    email_edittext_receiver_reg.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@CarereceiverRegister,
                        "Please enter email.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                TextUtils.isEmpty(
                    password_edittext_receiver_reg.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@CarereceiverRegister,
                        "Please enter password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(
                    username_edittext_receiver_reg.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@CarereceiverRegister,
                        "Please enter username.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {

                    val receiverEmail: String =
                        email_edittext_receiver_reg.text.toString().trim { it <= ' ' }
                    val receiverPassword: String =
                        password_edittext_receiver_reg.text.toString().trim { it <= ' ' }
                    val receiverUsername: String =
                        username_edittext_receiver_reg.text.toString().trim { it <= ' ' }
                    val Selection: RadioGroup = radioGroup1
                    var userType: String = tvUserType.text.toString().trim { it <= ' '}

                    val cr: RadioButton = findViewById(R.id.radio0)
                    val cg: RadioButton = findViewById(R.id.radio1)
                    val ad: RadioButton = findViewById(R.id.radio2)
                    if (cr.isChecked){
                        userType = "Care Receiver"
                    }
                    else if (cg.isChecked){
                        userType = "Care Giver"
                    }
                    else {
                        userType = "Admin"
                    }


                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(receiverEmail, receiverPassword)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                if (task.isSuccessful) {

                                    val intent = Intent(this, Profile::class.java)
                                    startActivity(intent)
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    val user = mAuth.currentUser
                                    val uid = user!!.uid

                                    mDatabase.child(uid).child("uid").setValue(uid)
                                    mDatabase.child(uid).child("username").setValue(receiverUsername)
                                    mDatabase.child(uid).child("email").setValue(receiverEmail)
                                    mDatabase.child(uid).child("password").setValue(receiverPassword)
                                    mDatabase.child(uid).child("userType").setValue(userType)



                                    Toast.makeText(
                                        this@CarereceiverRegister,
                                        "You are registered successfully!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    /*

                                if (userType == "Care Receiver"){
                                    val intent = Intent(
                                        this@CarereceiverRegister,
                                        MainActivity2::class.java
                                    )
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", firebaseUser.uid)
                                    intent.putExtra("receiverEmail_id", receiverEmail)
                                    startActivity(intent)
                                }
                                else if (userType == "Care Giver"){
                                    val intent = Intent(
                                        this@CarereceiverRegister,
                                        MainActivity::class.java
                                    )
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", firebaseUser.uid)
                                    intent.putExtra("receiverEmail_id", receiverEmail)
                                    startActivity(intent)
                                }
                                else if (userType == "Admin"){
                                    val intent = Intent(
                                        this@CarereceiverRegister,
                                        MainActivity3::class.java
                                    )
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", firebaseUser.uid)
                                    intent.putExtra("receiverEmail_id", receiverEmail)
                                    startActivity(intent)
                                }
*/

                                } else {
                                    Toast.makeText(
                                        this@CarereceiverRegister,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            })


                }
            }
        }

    }
}
