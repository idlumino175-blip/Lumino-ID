package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.ClipEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClipDao {
    @Query("SELECT * FROM clips ORDER BY isPinned DESC, createdAt DESC")
    fun getAllClips(): Flow<List<ClipEntity>>

    @Query("SELECT * FROM clips WHERE isPinned = 1 ORDER BY createdAt DESC")
    fun getPinnedClips(): Flow<List<ClipEntity>>

    @Query("SELECT * FROM clips WHERE category = :category ORDER BY isPinned DESC, createdAt DESC")
    fun getClipsByCategory(category: String): Flow<List<ClipEntity>>

    @Query("""
        SELECT * FROM clips 
        WHERE title LIKE '%' || :query || '%' 
           OR content LIKE '%' || :query || '%' 
           OR tags LIKE '%' || :query || '%' 
        ORDER BY isPinned DESC, createdAt DESC
    """)
    fun searchClips(query: String): Flow<List<ClipEntity>>

    @Query("SELECT * FROM clips WHERE id = :id LIMIT 1")
    suspend fun getClipById(id: Long): ClipEntity?

    @Query("SELECT COUNT(*) FROM clips")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(clip: ClipEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clips: List<ClipEntity>)

    @Update
    suspend fun update(clip: ClipEntity)

    @Delete
    suspend fun delete(clip: ClipEntity)

    @Query("DELETE FROM clips WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE clips SET isPinned = :isPinned WHERE id = :id")
    suspend fun togglePin(id: Long, isPinned: Boolean)

    @Query("UPDATE clips SET copyCount = copyCount + 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun incrementCopyCount(id: Long, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM clips")
    suspend fun clearAll()
}
