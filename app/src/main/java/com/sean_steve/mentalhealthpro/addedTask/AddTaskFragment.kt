package com.sean_steve.mentalhealthpro.addedTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.sean_steve.mentalhealthpro.ADD_EDIT_RESULT_OK
import com.sean_steve.mentalhealthpro.EventObserver
import com.sean_steve.mentalhealthpro.R
import com.sean_steve.mentalhealthpro.databinding.AddTaskFragmentBinding
import com.sean_steve.mentalhealthpro.util.setupRefreshLayout
import com.sean_steve.mentalhealthpro.util.setupSnackbar

/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
class AddTaskFragment : Fragment() {

    private lateinit var viewDataBinding: AddTaskFragmentBinding

    private val args: AddTaskFragmentArgs by navArgs()

    private val viewModel by viewModels<AddTaskViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.add_task_fragment, container, false)
        viewDataBinding = AddTaskFragmentBinding.bind(root).apply {
            this.viewmodel = viewModel
        }
        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSnackbar()
        setupNavigation()
        this.setupRefreshLayout(viewDataBinding.refreshLayout)
        viewModel.start(args.taskId)
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupNavigation() {
        viewModel.taskUpdatedEvent.observe(viewLifecycleOwner, EventObserver {
            val action = AddTaskFragmentDirections
                .actionAddTaskFragmentToTasksFragment()
            findNavController().navigate(action)
        })
    }
}
