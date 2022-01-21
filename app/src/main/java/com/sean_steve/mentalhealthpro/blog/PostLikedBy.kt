package com.sean_steve.mentalhealthpro.blog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sean_steve.mentalhealthpro.R
import java.util.ArrayList

class PostLikedBy : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var postId: String? = null
    var list: MutableList<ModelUsers?>? = null
    internal var adapterUsers: AdapterUser? = null
    var firebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.postlikedby)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Post Liked By")
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        firebaseAuth = FirebaseAuth.getInstance()
        actionBar.setSubtitle(firebaseAuth!!.currentUser!!.email)
        recyclerView = findViewById(R.id.likerecycle)
        val intent = intent
        postId = intent.getStringExtra("pid")
        list = ArrayList()
        val reference = FirebaseDatabase.getInstance().getReference("Likes")
        reference.child(postId!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                (list as ArrayList<ModelUsers?>).clear()
                for (dataSnapshot1 in dataSnapshot.children) {
                    val hisUid = "" + dataSnapshot1.ref.key
                    getUsers(hisUid)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun getUsers(hisUid: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.orderByChild("uid").equalTo(hisUid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (ds in dataSnapshot.children) {
                        val model = ds.getValue(ModelUsers::class.java)
                        list!!.add(model)
                    }
                    adapterUsers = AdapterUser(this@PostLikedBy, list)
                    recyclerView!!.adapter = adapterUsers
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}