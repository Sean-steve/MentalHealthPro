package com.sean_steve.mentalhealthpro

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.sean_steve.mentalhealthpro.blog.ModelPost
import com.sean_steve.mentalhealthpro.blog.PostDetails
import com.sean_steve.mentalhealthpro.blog.PostLikedBy
import java.lang.Exception
import java.util.*

class AdapterPost(var context: Context, var modelPosts: MutableList<ModelPost?>?) :
    RecyclerView.Adapter<AdapterPost.MyHolder>() {
    var myuid: String
    private val liekeref: DatabaseReference
    private val postref: DatabaseReference
    var mprocesslike = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.raw_post, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, @SuppressLint("RecyclerView") position: Int) {
        val uid = modelPosts?.get(position)?.uid
        val nameh = modelPosts?.get(position)?.uname
        val titlee = modelPosts?.get(position)?.title
        val descri = modelPosts?.get(position)?.description
        val ptime = modelPosts?.get(position)?.ptime
        val dp = modelPosts?.get(position)?.udp
        val plike = modelPosts?.get(position)?.plike
        val image = modelPosts?.get(position)?.uimage
        val email = modelPosts?.get(position)?.uemail
        val comm = modelPosts?.get(position)?.pcomments
        val pid = modelPosts?.get(position)?.ptime
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = ptime?.toLong()!!
        val timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString()
        holder.name.text = nameh
        holder.title.text = titlee
        holder.description.text = descri
        holder.time.text = timedate
        holder.like.text = "$plike Likes"
        holder.comments.text = "$comm Comments"
        setLikes(holder, ptime)
        try {
            Glide.with(context).load(dp).into(holder.picture)
        } catch (e: Exception) {
        }
        holder.image.visibility = View.VISIBLE
        try {
            Glide.with(context)?.load(image).into(holder.image)
        } catch (e: Exception) {
        }
        holder.like.setOnClickListener {
            val intent = Intent(holder.itemView.context, PostLikedBy::class.java)
            intent.putExtra("pid", pid)
            holder.itemView.context.startActivity(intent)
        }
        holder.likebtn.setOnClickListener {
            val plike = modelPosts?.get(position)?.plike?.toInt()
            mprocesslike = true
            val postid = modelPosts?.get(position)?.ptime
            liekeref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (mprocesslike) {
                        mprocesslike = if (postid?.let { it1 -> dataSnapshot.child(it1).hasChild(myuid) } == true) {
                            postref.child(postid).child("plike").setValue("" + (plike?.minus(1)))
                            liekeref.child(postid).child(myuid).removeValue()
                            false
                        } else {
                            postid?.let { it1 -> postref.child(it1).child("plike").setValue("" + (plike?.plus(
                                1))) }
                            postid?.let { it1 -> liekeref.child(it1).child(myuid).setValue("Liked") }
                            false
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
        holder.more.setOnClickListener { showMoreOptions(holder.more, uid!!, myuid, ptime, image!!) }
        holder.comment.setOnClickListener {
            val intent = Intent(context, PostDetails::class.java)
            intent.putExtra("pid", ptime)
            context.startActivity(intent)
        }
    }

    private fun showMoreOptions(
        more: ImageButton,
        uid: String,
        myuid: String,
        pid: String,
        image: String
    ) {
        val popupMenu = PopupMenu(context, more, Gravity.END)
        if (uid == myuid) {
            popupMenu.menu.add(Menu.NONE, 0, 0, "DELETE")
        }
        popupMenu.setOnMenuItemClickListener { item ->
            if (item.itemId == 0) {
                deltewithImage(pid, image)
            }
            false
        }
        popupMenu.show()
    }

    private fun deltewithImage(pid: String, image: String) {
        val pd = ProgressDialog(context)
        pd.setMessage("Deleting")
        val picref = FirebaseStorage.getInstance().getReferenceFromUrl(image)
        picref.delete().addOnSuccessListener {
            val query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("ptime")
                .equalTo(pid)
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (dataSnapshot1 in dataSnapshot.children) {
                        dataSnapshot1.ref.removeValue()
                    }
                    pd.dismiss()
                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }.addOnFailureListener { }
    }

    private fun setLikes(holder: MyHolder, pid: String) {
        liekeref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(pid).hasChild(myuid)) {
                    holder.likebtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_launcher_foreground,
                        0,
                        0,
                        0)
                    holder.likebtn.text = "Liked"
                } else {
                    holder.likebtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_launcher_foreground,
                        0,
                        0,
                        0)
                    holder.likebtn.text = "Like"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun getItemCount(): Int {
        return modelPosts?.size!!
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var picture: ImageView
        var image: ImageView
        var name: TextView
        var time: TextView
        var title: TextView
        var description: TextView
        var like: TextView
        var comments: TextView
        var more: ImageButton
        var likebtn: Button
        var comment: Button
        var profile: LinearLayout

        init {
            picture = itemView.findViewById(R.id.picturetv)
            image = itemView.findViewById(R.id.pimagetv)
            name = itemView.findViewById(R.id.unametv)
            time = itemView.findViewById(R.id.utimetv)
            more = itemView.findViewById(R.id.morebtn)
            title = itemView.findViewById(R.id.ptitletv)
            description = itemView.findViewById(R.id.descript)
            like = itemView.findViewById(R.id.plikeb)
            comments = itemView.findViewById(R.id.pcommentco)
            likebtn = itemView.findViewById(R.id.like)
            comment = itemView.findViewById(R.id.comment)
            profile = itemView.findViewById(R.id.profilelayout)
        }
    }

    init {
        myuid = FirebaseAuth.getInstance().currentUser!!.uid
        liekeref = FirebaseDatabase.getInstance().reference.child("Likes")
        postref = FirebaseDatabase.getInstance().reference.child("Posts")
    }
}