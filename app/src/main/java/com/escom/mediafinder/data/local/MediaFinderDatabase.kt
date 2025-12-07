package com.escom.mediafinder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserEntity::class,
        FavoriteEntity::class,
        SearchHistoryEntity::class,
        ShowCacheEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MediaFinderDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun showCacheDao(): ShowCacheDao
}