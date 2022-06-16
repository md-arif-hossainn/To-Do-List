package com.example.arif.todoapp

import CustomAlertDialog
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arif.todoapp.adapters.TodoAdapter
import com.example.arif.todoapp.databinding.FragmentTodoListBinding
import com.example.arif.todoapp.entities.TodoModel
import com.example.arif.todoapp.prefdata.LoginPreference
import com.example.arif.todoapp.viewmodels.TodoViewModel
import kotlinx.coroutines.launch
import show_all
import show_completed
import show_incompleted
import todo_delete
import todo_edit
import todo_edit_completed

class TodoListFragment : Fragment() {
    private lateinit var binding: FragmentTodoListBinding
    private lateinit var preference: LoginPreference
    private var userId = 0L
    private var menuItemOption = show_all
    private var priorityOption = PriorityOptions.all
    private lateinit var adapter: TodoAdapter
    private val todoViewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.todo_list_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val completedItem: MenuItem = menu.findItem(R.id.item_completed)
        val incompletedItem: MenuItem = menu.findItem(R.id.item_incompleted)
        val allItem: MenuItem = menu.findItem(R.id.item_all)

        if (menuItemOption == show_all) {
            allItem.isVisible = false
            completedItem.isVisible = true
            incompletedItem.isVisible = true
        } else if (menuItemOption == show_completed) {
            allItem.isVisible = true
            completedItem.isVisible = false
            incompletedItem.isVisible = true
        } else if (menuItemOption == show_incompleted) {
            allItem.isVisible = true
            completedItem.isVisible = true
            incompletedItem.isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_logout -> {
                lifecycle.coroutineScope.launch {
                    preference.setLoginStatus(status = false, 0L, requireContext())
                }
            }

            R.id.item_completed -> {
                menuItemOption = show_completed
                lifecycle.coroutineScope.launch {
                    todoViewModel
                        .getTodoByStatusUserId(userId, 1)
                        .observe(viewLifecycleOwner) {
                            adapter.submitList(it)
                        }
                }
            }

            R.id.item_incompleted -> {
                menuItemOption = show_incompleted
                lifecycle.coroutineScope.launch {
                    todoViewModel
                        .getTodoByStatusUserId(userId, 0)
                        .observe(viewLifecycleOwner) {
                            adapter.submitList(it)
                        }
                }
            }

            R.id.item_all -> {
                menuItemOption = show_all
                lifecycle.coroutineScope.launch {
                    todoViewModel
                        .getTodoByUserId(userId)
                        .observe(viewLifecycleOwner) {
                            adapter.submitList(it)
                        }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        preference = LoginPreference(requireContext())
        adapter = TodoAdapter{model, action ->
            performTodoRowAction(model, action)
        }
        binding.todoRV.layoutManager = LinearLayoutManager(requireActivity())
        binding.todoRV.adapter = adapter
        preference.isLoggedInFlow
            .asLiveData()
            .observe(viewLifecycleOwner){
            if (!it) {
                findNavController().navigate(R.id.action_todoListFragment_to_loginFragment)
            }
        }

        preference.userIdFlow.asLiveData()
            .observe(viewLifecycleOwner) {
                userId = it
                todoViewModel.getTodoByUserId(it).observe(viewLifecycleOwner) {
                    todoViewModel.todoList = it
                    if (priorityOption == PriorityOptions.all) {
                        adapter.submitList(it)
                    }else {
                        populateListAccordingToPriority()
                    }

                }
            }


        binding.newTodoFab.setOnClickListener {
            findNavController().navigate(R.id.action_todoListFragment_to_newTodoFragment)
        }

        binding.cbLayout.setOnCheckedChangeListener { radioGroup, i ->
            priorityOption = when(i) {
                R.id.lowCB -> PriorityOptions.low
                R.id.normalCB -> PriorityOptions.normal
                R.id.highCB -> PriorityOptions.high
                else -> PriorityOptions.all
            }

            if (priorityOption == PriorityOptions.all) {
                adapter.submitList(todoViewModel.todoList)
            } else {
                populateListAccordingToPriority()
            }

        }

        return binding.root
    }

    private fun populateListAccordingToPriority() {
        val tempList = mutableListOf<TodoModel>()
        todoViewModel.todoList.forEach {
            if (it.priority == priorityOption) {
                tempList.add(it)
            }
        }
        adapter.submitList(tempList)
    }

    private fun performTodoRowAction(model: TodoModel, action: String) {
        when(action) {
            todo_edit_completed -> {
                todoViewModel.updateTodo(model)
            }
            todo_delete -> {
                CustomAlertDialog(
                    icon = R.drawable.ic_baseline_delete_24,
                    title = "Delete",
                    body = "Sure to delete this item?",
                    posBtnText = "YES",
                    negBtnText = "CANCEL",
                ) {
                    todoViewModel.deleteTodo(model)
                }.show(childFragmentManager, null)
            }
            todo_edit -> {

                val bundle = bundleOf("id" to model.todoId)
                findNavController().navigate(R.id.action_todoListFragment_to_newTodoFragment, bundle)

            }
        }
    }
}