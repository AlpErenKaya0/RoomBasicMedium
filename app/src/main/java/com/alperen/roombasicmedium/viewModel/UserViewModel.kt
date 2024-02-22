package com.alperen.roombasicmedium.viewModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.alperen.roombasicmedium.data.User
import com.alperen.roombasicmedium.data.UserDatabase
import com.alperen.roombasicmedium.data.UserRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application):AndroidViewModel(application) {
    val readAllData: LiveData<List<User>>
    private val repository: UserRepository

    init {
        val userDao = UserDatabase.invoke(application).userDao()
        repository = UserRepository(userDao)
        readAllData = userDao.readAllData()
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            //yapılacak işlemlerin fonksiyonlarını kullandık
            repository.addUser(user)
        }
    }

    fun deleteAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllUsers()
        }
    }
        fun deleteUser(user: User) {
            viewModelScope.launch(Dispatchers.IO) {
                //yapılacak işlemlerin fonksiyonlarını kullandık
                repository.deleteUser(user)
            }
        }

        fun getUsersBetween30And40(): LiveData<List<User>> {
            return repository.getUsersBetween30And40()
        }

        fun getUsersByName(name: String): LiveData<List<User>> {
            return repository.getUsersByName(name)
        }

}