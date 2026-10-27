package com.statusflow.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.statusflow.app.data.local.AppDatabase
import com.statusflow.app.data.local.ScheduledItem
import com.statusflow.app.data.repository.StatusRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StatusViewModel(application: Application) : AndroidViewModel(application) {
    private val database = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "statusflow.db"
    ).build()

    private val repository = StatusRepository(database.scheduledItemDao())

    val scheduledItems: StateFlow<List<ScheduledItem>> = repository.allItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addItem(mediaUri: String, scheduleTime: Long, isRecurring: Boolean) {
        viewModelScope.launch {
            repository.insert(
                ScheduledItem(
                    mediaUri = mediaUri,
                    scheduleTime = scheduleTime,
                    isRecurring = isRecurring,
                    status = "SCHEDULED"
                )
            )
        }
    }

    fun deleteItem(item: ScheduledItem) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }
}
