package com.madassignment.okuable.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.madassignment.okuable.R
import com.madassignment.okuable.adapter.commentAdapter
import com.madassignment.okuable.data.CaregiverList
import com.madassignment.okuable.data.Comment
import com.madassignment.okuable.databinding.ActivityCommentsBinding
import java.util.*
import kotlin.collections.HashMap


class Comments : AppCompatActivity() {
    private lateinit var commentList: ArrayList<Comment>
    private var mAdapter: commentAdapter? = null


    private lateinit var binding: ActivityCommentsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comments)





        var comment = binding.commentEt
        var btnBack  = binding.cgBtnBack


        btnBack.setOnClickListener{
           finish()
        }

        val intent = intent
        val eventName: String = intent?.getStringExtra("EventName").toString()

        var btnAdd = binding.addBtn
        btnAdd.setOnClickListener{





            ////Get USERNAME from realtime database
            val user = FirebaseAuth.getInstance().currentUser
            val database = Firebase.database
            val ref = database.reference.child("Users")
            val currentUserId = user?.uid



            val uidref= FirebaseAuth.getInstance().currentUser?.uid
            FirebaseDatabase.getInstance().reference.child("Users/$uidref")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val value = dataSnapshot.child("username").getValue(String::class.java)





                        val commentTxt = comment.text.toString()

                        FirebaseFirestore.getInstance().runTransaction{

                                transaction ->


                            val newCommentRef = FirebaseFirestore.getInstance().collection("Event").document(eventName)
                                .collection("comment").document()



                         //upload to firestore
                            val data = HashMap<String, Any>()
                            data.put("comment", commentTxt)
                            data.put("timeStamp", FieldValue.serverTimestamp())
                            if (value != null) {
                                data.put("username" , value)
                            }



                            transaction.set(newCommentRef, data)






                        }
                            .addOnSuccessListener {
                                comment.setText("")
                                finish()
                                overridePendingTransition( 0, 0)
                                startActivity(getIntent())
                                overridePendingTransition( 0, 0)

                            }
                            .addOnFailureListener{

                                    exception ->
                                Log.e("Exception: ", "Failed to add comment ${exception.localizedMessage}")

                            }




                    }})
            /*currentUserId?.let { it1 ->
                ref.child(it1).get().addOnCompleteListener{ task ->

                    if(task.isSuccessful){

                        val snapshot = task.result
                        val Username = snapshot?.child("username")?.getValue(Comment::class.java)


                        val UserName = Username?.username.toString()
                        binding.textView2.setText(UserName)


                    }
                }
            }*/







        }

        val commentRV: RecyclerView = binding.rvComment

        commentList = ArrayList()
        mAdapter = commentAdapter(this, commentList)

        //mAdapter.notifyDataSetChanged()
        commentRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        commentRV.setHasFixedSize(true)
        commentRV.invalidate()


        val db = FirebaseFirestore.getInstance()




        db.collection("Event").document(eventName).collection("comment").get()


            .addOnSuccessListener { documents ->
                for (document in documents) {

                    commentList.add(document.toObject(Comment::class.java));

                    // mAdapter!!.notifyDataSetChanged()

                }

                commentRV.adapter = mAdapter
                // mAdapter!!.notifyDataSetChanged()
            }


    }


}