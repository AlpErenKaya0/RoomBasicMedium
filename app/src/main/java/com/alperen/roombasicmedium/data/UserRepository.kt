package com.alperen.roombasicmedium.data
import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {
    val readAllData : LiveData<List<User>> = userDao.readAllData()
    suspend fun addUser(user:User)
    {
        userDao.addUser(user)
    }
    suspend fun deleteUser(user:User){
        userDao.deleteUser(user)
    }
     fun getUsersBetween30And40(): LiveData<List<User>> {
        return userDao.getUsersBetween30And40()
    }
    fun getUsersByName(name: String): LiveData<List<User>> {
        return userDao.getUsersByName(name)
    }
    suspend fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }
}