package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clips")
data class ClipEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val category: String, // String representation of ClipCategory (e.g. "LINK", "CODE", "OTP", "EMAIL", "NOTE", "TEXT")
    val tags: String = "", // Comma-separated tags e.g. "Work", "Personal", "Links"
    val source: String = "Saved just now", // Source e.g. "Saved just now", "From Safari", "From Notes"
    val isPinned: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val copyCount: Int = 0
) {
    val categoryEnum: ClipCategory
        get() = try {
            ClipCategory.valueOf(category)
        } catch (_: Exception) {
            ClipCategory.TEXT
        }

    val tagList: List<String>
        get() = tags.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

    val characterCount: Int
        get() = content.length

    val wordCount: Int
        get() = if (content.isBlank()) 0 else content.trim().split(Regex("\\s+")).size

    val lineCount: Int
        get() = if (content.isEmpty()) 0 else content.lines().size
}
