package com.escom.mediafinder.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun login(username: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserEntity?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>
}

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE userId = :userId AND showId = :showId")
    suspend fun deleteFavorite(userId: Int, showId: Int)

    @Query("SELECT * FROM favorites WHERE userId = :userId ORDER BY addedAt DESC")
    fun getFavoritesByUser(userId: Int): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND showId = :showId)")
    suspend fun isFavorite(userId: Int, showId: Int): Boolean

    @Query("SELECT genres FROM favorites WHERE userId = :userId AND genres IS NOT NULL")
    suspend fun getUserFavoriteGenres(userId: Int): List<String>
}

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: SearchHistoryEntity)

    @Query("SELECT * FROM search_history WHERE userId = :userId ORDER BY timestamp DESC LIMIT 20")
    fun getSearchHistoryByUser(userId: Int): Flow<List<SearchHistoryEntity>>

    @Query("SELECT * FROM search_history ORDER BY timestamp DESC")
    fun getAllSearchHistory(): Flow<List<SearchHistoryEntity>>

    @Query("DELETE FROM search_history WHERE userId = :userId")
    suspend fun clearUserHistory(userId: Int)
}

@Dao
interface ShowCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShow(show: ShowCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShows(shows: List<ShowCacheEntity>)

    @Query("SELECT * FROM show_cache WHERE showId = :showId")
    suspend fun getShowById(showId: Int): ShowCacheEntity?

    @Query("SELECT * FROM show_cache WHERE name LIKE '%' || :query || '%' ORDER BY rating DESC")
    suspend fun searchShowsLocally(query: String): List<ShowCacheEntity>

    @Query("SELECT * FROM show_cache WHERE genres LIKE '%' || :genre || '%' ORDER BY rating DESC LIMIT 10")
    suspend fun getShowsByGenre(genre: String): List<ShowCacheEntity>

    @Query("DELETE FROM show_cache WHERE cachedAt < :timestamp")
    suspend fun deleteOldCache(timestamp: Long)
}