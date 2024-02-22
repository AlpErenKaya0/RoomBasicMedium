package com.alperen.roombasicmedium.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    //2. bir tablo kullanılıyorsa, buraya Dao'sunun eklenmesi yeterli olacaktır
    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null
        private val lock = Any()
        operator fun invoke() = INSTANCE?: synchronized(lock) {
            INSTANCE
        }
        private fun makeDatabase (context: Context) = Room.databaseBuilder(
            context.applicationContext, UserDatabase::class.java,"user_database"
        ).build()
        }
    }
