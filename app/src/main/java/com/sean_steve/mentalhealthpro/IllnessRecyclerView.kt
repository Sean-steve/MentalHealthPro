package com.sean_steve.mentalhealthpro

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.map

/**
 * A fragment representing a list of Items.
 */
class IllnessRecyclerView : Fragment() {
    var titlesList= mutableListOf<String>()
    var descriptionsList= mutableListOf<String>()
    var imagesList= mutableListOf<Int>()

    private var columnCount = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.illness_recycler_view_list, container, false)
postToList()
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 3 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter=RecyclerViewAdapter(titlesList, descriptionsList,
                    imagesList)
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            IllnessRecyclerView().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
    private fun addToList(title: String, description:String, image:Int){
        titlesList.add(title)
        descriptionsList.add(description)
        imagesList.add(image)
    }
    private fun postToList(){
        addToList("Anxiety","feeling of tension that can be either emotional or physical",
            R.mipmap.stress)
        addToList("Mood"," causes extreme mood swings that include emotional highs and lows",R.mipmap.bipolar)
        addToList("Depresive Disorder","difficulty sustaining attention, hyperactivity and impulsive behavior.",R.mipmap.adhd)
        addToList("Hoarding","mood disorder that causes a persistent feeling of sadness and loss of interest",R.mipmap.depression)
        addToList("phobia","A persistent fear of ghosts",R.mipmap.phasmophobia)}

}

enum class AuthenticationState {
    AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
}

val authenticationState = FirebaseUserLiveData().map { user ->
    if (user != null) {
        AuthenticationState.AUTHENTICATED
    } else {
        AuthenticationState.UNAUTHENTICATED
    }
}
