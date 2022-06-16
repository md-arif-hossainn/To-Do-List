package com.example.arif.todoapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.arif.todoapp.db.TodoDatabase
import com.example.arif.todoapp.entities.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(application: Application)
    : AndroidViewModel(application) {
        private val userDao = TodoDatabase.getDB(application).userDao()
    val errMsgLD: MutableLiveData<String> = MutableLiveData()
    var userModel: UserModel? = null


    fun login(email: String, passwowrd: String, callback:(Long) -> Unit) {
        viewModelScope.launch {
            userModel = userDao.getUserByEmail(email)
            if (userModel != null) {
                if (userModel!!.password == passwowrd) {
                    callback(userModel!!.userId)
                } else {
                    errMsgLD.value = "Incorrect password"
                }
            } else {
                errMsgLD.value = "Email does not exist"
            }
        }
    }

    fun register(user: UserModel, callback:(Long) -> Unit) {
        viewModelScope.launch {
            userModel = userDao.getUserByEmail(user.email)
            if (userModel != null) {
                errMsgLD.value = "Email already exists"
            }else {
                val rowid = userDao.insertUser(user)
                userModel = user.apply {
                    userId = rowid
                }
                callback(rowid)
            }
        }
    }
}