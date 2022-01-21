package com.sean_steve.mentalhealthpro.blog

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sean_steve.mentalhealthpro.R
import java.lang.Exception
import java.util.*

internal class AdapterComments(
    var context: Context,
    var list: MutableList<ModelComments?>?,
    var myuid: String,
    var postid: String
) : RecyclerView.Adapter<AdapterComments.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.raw_comments, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val uid = list?.get(position)?.uid
        val name = list?.get(position)?.uname
        val email = list?.get(position)?.uemail
        val image = list?.get(position)?.udp
        val cid = list?.get(position)?.getcId()
        val comment = list?.get(position)?.comment
        val timestamp = list?.get(position)?.ptime
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp?.toLong()!!
        val timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString()
        holder.name.text = name
        holder.time.text = timedate
        holder.comment.text = comment
        try {
            Glide.with(context).load(image).into(holder.imagea)
        } catch (e: Exception) {
        }
    }

    override fun getItemCount(): Int {
        return list?.size!!
    }

    internal inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagea: ImageView
        var name: TextView
        var comment: TextView
        var time: TextView

        init {
            imagea = itemView.findViewById(R.id.loadcomment)
            name = itemView.findViewById(R.id.commentname)
            comment = itemView.findViewById(R.id.commenttext)
            time = itemView.findViewById(R.id.commenttime)
        }
    }
}