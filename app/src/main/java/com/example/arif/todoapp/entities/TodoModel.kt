package com.example.arif.todoapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_todo")
data class TodoModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "todo_id")
    var todoId: Long = 0,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    val name: String,
    val priority: String,
    var date: Long = System.currentTimeMillis(),
    var time: Long,
    var day: Int,
    var month: Int,
    var year: Int,
    var hour: Int,
    var minute: Int,
    var completed: Boolean = false
)
