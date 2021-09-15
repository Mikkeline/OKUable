package com.madassignment.okuable.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.madassignment.okuable.R
import com.madassignment.okuable.data.Comment
import com.madassignment.okuable.fragment.EventFragment_Organiser.Companion.appContext

class commentAdapter (
    val context: Context,
    private var comments: List<Comment>
    ) :
    RecyclerView.Adapter<commentAdapter.ViewHolder>() {

        private var firestore: FirebaseFirestore? = null
        private var reference: DatabaseReference? = null
        private var currentUserId: String? = null


        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

            var profilePic: ImageView = itemView.findViewById(R.id.profile_pic)
            var userName: TextView = itemView.findViewById(R.id.username_tv)
            var date: TextView = itemView.findViewById(R.id.date_tv)
            var comment: TextView = itemView.findViewById(R.id.comment_tv)



        }

        override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comment_item_row, parent,false)

            //firestore = FirebaseFirestore.getInstance()
            //user = FirebaseAuth.getInstance().currentUser
            //reference = FirebaseDatabase.getInstance().getReference("Users")
            // currentUserId = "5bJBTRI8QHM0YH0bwVdumSqsR5H3"//carereceiver
            //currentUserId = "KqwNtEpvjgdO8lpLfBXEcu8QdAI2" //caregiver

            val viewHolder = ViewHolder(itemView)

            //mAdapter?.notifyDataSetChanged()
            return viewHolder
        }




        override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

            val currentPosition = comments[i]
            val context = appContext
            viewHolder.userName.text = currentPosition.username
            viewHolder.date.text = currentPosition.timeStamp?.toString()
            viewHolder.comment.text = currentPosition.comment



            Glide.with(context)
                .load(currentPosition.dlUrl)
                .into(viewHolder.profilePic)


        }


        override fun getItemCount(): Int {
            return comments.size
        }




    }


