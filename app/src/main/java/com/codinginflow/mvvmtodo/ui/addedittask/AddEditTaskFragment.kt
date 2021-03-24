package com.codinginflow.mvvmtodo.ui.addedittask

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.databinding.FragmentNewTaskBinding
import com.codinginflow.mvvmtodo.utils.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_new_task) {
    private val viewModel: AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentNewTaskBinding.bind(view)

        binding.apply {
            etTaskName.setText(viewModel.taskName)
            cbImportant.isChecked = viewModel.taskImportant
            cbImportant.jumpDrawablesToCurrentState()
            tvDateCreated.isVisible = viewModel.task != null
            tvDateCreated.text = "Created: ${viewModel.task?.getTimeFormatted}"

            etTaskName.addTextChangedListener {
                viewModel.onTextChanged(etTaskName.text.toString())
            }

            cbImportant.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onCheckBoxChanged(isChecked)
            }

            fabSaveTask.setOnClickListener {
                viewModel.onSaveTaskClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when (event) {
                    is AddEditTaskEvent.NavBack -> {
                        binding.etTaskName.clearFocus()
                        setFragmentResult(
                            "add_edit_result",
                            bundleOf("add_edit_result" to event.res)
                        )
                        findNavController().popBackStack()
                    }
                    is AddEditTaskEvent.TaskNameEmpty -> {
                        Snackbar.make(requireView(),event.msg,Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }
    }

}