package com.example.data

import com.example.model.ClipCategory
import com.example.model.ClipEntity
import kotlinx.coroutines.flow.Flow

class ClipRepository(private val clipDao: ClipDao) {

    val allClips: Flow<List<ClipEntity>> = clipDao.getAllClips()
    val pinnedClips: Flow<List<ClipEntity>> = clipDao.getPinnedClips()

    fun getClipsByCategory(category: ClipCategory): Flow<List<ClipEntity>> {
        return clipDao.getClipsByCategory(category.name)
    }

    fun searchClips(query: String): Flow<List<ClipEntity>> {
        return clipDao.searchClips(query)
    }

    suspend fun getClipById(id: Long): ClipEntity? = clipDao.getClipById(id)

    suspend fun insert(clip: ClipEntity): Long = clipDao.insert(clip)

    suspend fun update(clip: ClipEntity) = clipDao.update(clip)

    suspend fun delete(clip: ClipEntity) = clipDao.delete(clip)

    suspend fun deleteById(id: Long) = clipDao.deleteById(id)

    suspend fun togglePin(id: Long, isPinned: Boolean) = clipDao.togglePin(id, isPinned)

    suspend fun incrementCopyCount(id: Long) = clipDao.incrementCopyCount(id)

    suspend fun clearAll() = clipDao.clearAll()

    suspend fun seedInitialDataIfEmpty() {
        if (clipDao.getCount() == 0) {
            val now = System.currentTimeMillis()
            val twoDaysAgo = now - 2 * 24 * 60 * 60 * 1000L
            val sampleClips = listOf(
                ClipEntity(
                    title = "Ideas Note",
                    content = "The best ideas are often the ones you almost forgot to write down.",
                    category = ClipCategory.NOTE.name,
                    tags = "Personal",
                    source = "Saved just now",
                    isPinned = false,
                    createdAt = twoDaysAgo,
                    updatedAt = twoDaysAgo,
                    copyCount = 1
                ),
                ClipEntity(
                    title = "Linear App Link",
                    content = "https://linear.app — a calmer way to manage projects",
                    category = ClipCategory.LINK.name,
                    tags = "Links",
                    source = "From Safari",
                    isPinned = false,
                    createdAt = twoDaysAgo,
                    updatedAt = twoDaysAgo,
                    copyCount = 2
                ),
                ClipEntity(
                    title = "Launch Checklist",
                    content = "Launch checklist: confirm owner, write the one-pager, review test coverage",
                    category = ClipCategory.NOTE.name,
                    tags = "Work",
                    source = "From Notes",
                    isPinned = false,
                    createdAt = twoDaysAgo,
                    updatedAt = twoDaysAgo,
                    copyCount = 0
                )
            )
            clipDao.insertAll(sampleClips)
        }
    }
}
