package com.sean_steve.mentalhealthpro.blog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.sean_steve.mentalhealthpro.R
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

internal class AdapterUser(var context: Context, var list: MutableList<ModelUsers?>?) :
    RecyclerView.Adapter<AdapterUser.MyHolder>() {
    var firebaseAuth: FirebaseAuth
    var uid: String?
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.raw_users, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val hisuid = list?.get(position)?.uid
        val userImage = list?.get(position)?.image
        val username = list?.get(position)?.name
        val usermail = list?.get(position)?.email
        holder.name.text = username
        holder.email.text = usermail
        try {
            Glide.with(context).load(userImage).into(holder.profiletv)
        } catch (e: Exception) {
        }
    }

    override fun getItemCount(): Int {
        return list?.size!!
    }

    internal inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profiletv: CircleImageView = itemView.findViewById(R.id.imagep)
        var name: TextView = itemView.findViewById(R.id.namep)
        var email: TextView = itemView.findViewById(R.id.emailp)

    }

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        uid = firebaseAuth.uid
    }
}