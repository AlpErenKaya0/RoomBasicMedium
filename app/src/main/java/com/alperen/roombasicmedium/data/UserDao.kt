package com.alperen.roombasicmedium.data
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user:User)
    //oluşturduğumuz entity tipinde bir livedata olması lazım
    //tüm veri çekiliyor, ilgili veri değil
    @Query(value="SELECT * FROM users ORDER BY id ASC ")
    fun readAllData():LiveData<List<User>>
    //Conflict strategy'si önemli! şu anlık ignore

    //delete update gibi işlemlerin yapılabilmesi için primary key lazım
    @Delete
    suspend fun deleteUser(user: User)
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
    @Query("SELECT * FROM users WHERE age BETWEEN 30 AND 40 ORDER BY id ASC")
     fun getUsersBetween30And40(): LiveData<List<User>>
    @Query("SELECT * FROM users WHERE name = :name")
    //thread bloklamamak için livedata içinde dönecek
    fun getUsersByName(name: String): LiveData<List<User>>

}