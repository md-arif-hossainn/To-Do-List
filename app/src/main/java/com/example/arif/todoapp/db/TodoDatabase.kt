package com.example.arif.todoapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.arif.todoapp.daos.TodoDao
import com.example.arif.todoapp.daos.UserDao
import com.example.arif.todoapp.entities.TodoModel
import com.example.arif.todoapp.entities.UserModel

@Database(entities = [TodoModel::class, UserModel::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao() : TodoDao
    abstract fun userDao() : UserDao

    companion object {
        private var db: TodoDatabase? = null
        fun getDB(context: Context) : TodoDatabase {
            if (db == null) {
                db = Room.databaseBuilder(
                    context,
                    TodoDatabase::class.java,
                    "todo_db"
                ).build()
                return db!!
            }
            return db!!
        }
    }
}