package com.madassignment.okuable.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.madassignment.okuable.R
import com.madassignment.okuable.adapter.commentAdapter
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


        val commentRV: RecyclerView = binding.rvComment

        commentList = ArrayList()
        mAdapter = commentAdapter(this, commentList)

        //mAdapter.notifyDataSetChanged()
        commentRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        commentRV.setHasFixedSize(true)
        commentRV.invalidate()


        val db = FirebaseFirestore.getInstance()

        val intent = intent
        val eventName: String = intent?.getStringExtra("EventName").toString()




        db.collection("Event").document(eventName).collection("comment").get()


          .addOnSuccessListener { documents ->
                  for (document in documents) {

                      commentList.add(document.toObject(Comment::class.java));

                     // mAdapter!!.notifyDataSetChanged()

                  }

                  commentRV.adapter = mAdapter
                 // mAdapter!!.notifyDataSetChanged()
              }



        var comment = binding.commentEt
        var btnBack  = binding.cgBtnBack


        btnBack.setOnClickListener{
           finish()
        }

        var btnAdd = binding.addBtn
        btnAdd.setOnClickListener{


            val commentTxt = comment.text.toString()
            val eventRef = FirebaseFirestore.getInstance().collection("Event").document(eventName)

            FirebaseFirestore.getInstance().runTransaction{

                transaction ->
              val event = transaction.get(eventRef)
              val NUM_COMMENTS = 0
              val  numComments = event.getLong(NUM_COMMENTS.toString())?.plus(1)

                val newCommentRef = FirebaseFirestore.getInstance().collection("Event").document(eventName)
                    .collection("comment").document()

                val data = HashMap<String, Any>()
                data.put("comment", commentTxt)
                data.put("timeStamp", FieldValue.serverTimestamp())


                ////Get USERNAME from realtime database
                data.put("username", "Jason")//FirebaseAuth.getInstance().currentUser?.displayName.toString())

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



        }



    }


}