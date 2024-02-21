package com.alperen.roombasicmedium.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val name:String,
    val surname:String,
    val age:Int)