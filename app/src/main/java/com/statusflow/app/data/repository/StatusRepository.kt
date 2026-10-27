package com.statusflow.app.data.repository

import com.statusflow.app.data.local.ScheduledItem
import com.statusflow.app.data.local.ScheduledItemDao
import kotlinx.coroutines.flow.Flow

class StatusRepository(private val dao: ScheduledItemDao) {
    val allItems: Flow<List<ScheduledItem>> = dao.getAllItems()

    suspend fun insert(item: ScheduledItem): Long {
        return dao.insertItem(item)
    }

    suspend fun update(item: ScheduledItem) {
        dao.updateItem(item)
    }

    suspend fun delete(item: ScheduledItem) {
        dao.deleteItem(item)
    }
}
