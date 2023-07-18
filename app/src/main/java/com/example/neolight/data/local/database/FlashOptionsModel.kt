package com.example.neolight.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "flash_database")
data class FlashOption(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "delay") val delay: Long,
)

data class Name(
    val name: String
)

@Dao
interface FlashOptionDao {
    @Query("SELECT * FROM flash_database")
    fun getAll(): LiveData<List<FlashOption>>

    @Query("SELECT * FROM flash_database WHERE uid = :uid")
    fun findByUid(uid: Int): LiveData<FlashOption>

    @Query("SELECT * FROM flash_database WHERE name = :name")
    suspend fun findByName(name: String?): FlashOption

    @Insert
    suspend fun insert(vararg options: FlashOption)

    @Delete(entity = FlashOption::class)
    suspend fun delete(vararg names: Name)
}
