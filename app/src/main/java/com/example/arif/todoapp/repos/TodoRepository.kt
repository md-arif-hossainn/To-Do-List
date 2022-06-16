package com.example.arif.todoapp.repos

import androidx.lifecycle.LiveData
import com.example.arif.todoapp.daos.TodoDao
import com.example.arif.todoapp.entities.TodoModel

class TodoRepository(val todoDao: TodoDao) {

    suspend fun insertTodo(todoModel: TodoModel) : Long{
        return todoDao.insertTodo(todoModel)
    }

    fun getTodoByUserId(userId: Long) = todoDao.getTodosByUserId(userId)

    fun getTodoByStatusUserId(userId: Long, status: Int) =
        todoDao.getTodosByStatusAndUserId(userId, status)
    fun getTODoById(id: Long): LiveData<TodoModel> = todoDao.getTodosById(id)

    fun getTodosByPriorityAndUserId(userId: Long, priority: String) =
        todoDao.getTodosByPriorityAndUserId(userId, priority)

    suspend fun updateTodo(todoModel: TodoModel) = todoDao.updateTodo(todoModel)

    suspend fun deleteTodo(todoModel: TodoModel) = todoDao.deleteTodo(todoModel)
}