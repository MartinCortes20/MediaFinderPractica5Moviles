package com.escom.mediafinder.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val password: String,
    val isAdmin: Boolean = false
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val showId: Int,
    val showName: String,
    val imageUrl: String?,
    val genres: String?,
    val rating: Double?,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val query: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "show_cache")
data class ShowCacheEntity(
    @PrimaryKey
    val showId: Int,
    val name: String,
    val language: String?,
    val genres: String?,
    val status: String?,
    val premiered: String?,
    val rating: Double?,
    val imageUrl: String?,
    val summary: String?,
    val cachedAt: Long = System.currentTimeMillis()
)