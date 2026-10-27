package com.statusflow.app.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduledItemDao {
    @Query("SELECT * FROM scheduled_items ORDER BY scheduleTime ASC")
    fun getAllItems(): Flow<List<ScheduledItem>>

    @Insert
    suspend fun insertItem(item: ScheduledItem): Long

    @Update
    suspend fun updateItem(item: ScheduledItem)

    @Delete
    suspend fun deleteItem(item: ScheduledItem)
}

@Database(entities = [ScheduledItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduledItemDao(): ScheduledItemDao
}
