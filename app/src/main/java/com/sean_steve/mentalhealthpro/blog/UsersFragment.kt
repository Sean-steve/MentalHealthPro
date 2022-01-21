package com.sean_steve.mentalhealthpro.blog

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sean_steve.mentalhealthpro.R
import java.util.ArrayList

class UsersFragment : Fragment() {
    var recyclerView: RecyclerView = null!!
    internal var adapterUsers: AdapterUser? = null
    var usersList: MutableList<ModelUsers?>? = null
    var firebaseAuth: FirebaseAuth? = null

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment UsersFragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_users, container, false)
        //this = view.findViewById(R.id.recyclep)
        with(recyclerView) {
            recyclerView= view.findViewById(R.id.recyclep)
            setHasFixedSize(true)
            setLayoutManager(LinearLayoutManager(activity))
        }
        usersList = ArrayList()
        firebaseAuth = FirebaseAuth.getInstance()
        allUsers
        return view
    }

    private val allUsers: Unit
        private get() {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val reference = FirebaseDatabase.getInstance().getReference("Users")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    usersList!!.clear()
                    for (dataSnapshot1 in dataSnapshot.children) {
                        val modelUsers = dataSnapshot1.getValue(ModelUsers::class.java)
                        if (modelUsers!!.uid != null && modelUsers.uid != firebaseUser!!.uid) {
                            usersList!!.add(modelUsers)
                        }
                        adapterUsers = AdapterUser(activity!!, usersList)
                        recyclerView!!.adapter = adapterUsers
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    private fun searchusers(s: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usersList!!.clear()
                for (dataSnapshot1 in dataSnapshot.children) {
                    val modelUsers = dataSnapshot1.getValue(ModelUsers::class.java)
                    if (modelUsers!!.uid != null && modelUsers.uid != firebaseUser!!.uid) {
                        if (modelUsers.name!!.toLowerCase().contains(s.toLowerCase()) ||
                            modelUsers.email!!.toLowerCase().contains(s.toLowerCase())
                        ) {
                            usersList!!.add(modelUsers)
                        }
                    }
                    adapterUsers = AdapterUser(activity!!, usersList)
                    adapterUsers!!.notifyDataSetChanged()
                    recyclerView!!.adapter = adapterUsers
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        menu.findItem(R.id.logout).isVisible = false
        val item = menu.findItem(R.id.search)
        val searchView = MenuItemCompat.getActionView(item) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!TextUtils.isEmpty(query.trim { it <= ' ' })) {
                    searchusers(query)
                } else {
                    allUsers
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (!TextUtils.isEmpty(newText.trim { it <= ' ' })) {
                    searchusers(newText)
                } else {
                    allUsers
                }
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }
}