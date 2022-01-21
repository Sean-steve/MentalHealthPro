package com.sean_steve.mentalhealthpro

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ProfileSettingsFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileSettingsFragment()
    }

    private lateinit var viewModel: ProfileSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.profile_settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileSettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}