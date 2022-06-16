package com.example.arif.todoapp.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.arif.todoapp.entities.TodoModel

@Dao
interface TodoDao {
    @Insert
    suspend fun insertTodo(todoModel: TodoModel) : Long

    @Update
    suspend fun updateTodo(todoModel: TodoModel)

    @Delete
    suspend fun deleteTodo(todoModel: TodoModel)

    @Query("select * from tbl_todo where user_id = :userId")
    fun getTodosByUserId(userId: Long) : LiveData<List<TodoModel>>
    @Query("select * from tbl_todo where user_id = :userId")
    fun getTodosById(userId: Long) : LiveData<TodoModel>

    @Query("select * from tbl_todo where completed = :status and user_id = :userId")
    fun getTodosByStatusAndUserId(userId: Long, status: Int) : LiveData<List<TodoModel>>

    @Query("select * from tbl_todo where priority = :priority and user_id = :userId")
    fun getTodosByPriorityAndUserId(userId: Long, priority: String) : LiveData<List<TodoModel>>


}