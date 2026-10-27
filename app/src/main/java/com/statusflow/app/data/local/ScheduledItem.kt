package com.statusflow.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scheduled_items")
data class ScheduledItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mediaUri: String,
    val scheduleTime: Long,
    val isRecurring: Boolean,
    val status: String // DRAFT, SCHEDULED, ATTEMPTING, PUBLISHED, FAILED
)
