package com.escom.mediafinder.data.repository

import com.escom.mediafinder.data.local.*
import com.escom.mediafinder.data.model.Show
import com.escom.mediafinder.data.remote.TVMazeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaFinderRepository @Inject constructor(
    private val api: TVMazeApi,
    private val userDao: UserDao,
    private val favoriteDao: FavoriteDao,
    private val searchHistoryDao: SearchHistoryDao,
    private val showCacheDao: ShowCacheDao
) {
    // ============ USER OPERATIONS ============
    suspend fun registerUser(username: String, password: String, isAdmin: Boolean = false): Result<UserEntity> {
        return try {
            val userId = userDao.insertUser(UserEntity(username = username, password = password, isAdmin = isAdmin))
            val user = userDao.getUserById(userId.toInt())
            Result.success(user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(username: String, password: String): Result<UserEntity> {
        return try {
            val user = userDao.login(username, password)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Usuario o contraseña incorrectos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllUsers(): List<UserEntity> = userDao.getAllUsers()

    // ============ SHOW OPERATIONS ============
    suspend fun searchShows(query: String, userId: Int): Result<List<Show>> {
        return try {
            // Guardar búsqueda en historial
            searchHistoryDao.insertSearch(SearchHistoryEntity(userId = userId, query = query))

            // Intentar búsqueda remota
            val response = api.searchShows(query)
            val shows = response.map { it.show }

            // Cachear resultados
            val cacheEntities = shows.map { it.toShowCacheEntity() }
            showCacheDao.insertShows(cacheEntities)

            Result.success(shows)
        } catch (e: Exception) {
            // Si falla, buscar en cache local
            val cachedShows = showCacheDao.searchShowsLocally(query)
            if (cachedShows.isNotEmpty()) {
                Result.success(cachedShows.map { it.toShow() })
            } else {
                Result.failure(e)
            }
        }
    }

    // ============ FAVORITES OPERATIONS ============
    suspend fun addToFavorites(userId: Int, show: Show) {
        val favorite = FavoriteEntity(
            userId = userId,
            showId = show.id,
            showName = show.name,
            imageUrl = show.image?.medium,
            genres = show.genres?.joinToString(","),
            rating = show.rating?.average
        )
        favoriteDao.insertFavorite(favorite)
    }

    suspend fun removeFromFavorites(userId: Int, showId: Int) {
        favoriteDao.deleteFavorite(userId, showId)
    }

    fun getFavoritesByUser(userId: Int): Flow<List<FavoriteEntity>> {
        return favoriteDao.getFavoritesByUser(userId)
    }

    fun getAllFavorites(): Flow<List<FavoriteEntity>> {
        return favoriteDao.getAllFavorites()
    }

    suspend fun isFavorite(userId: Int, showId: Int): Boolean {
        return favoriteDao.isFavorite(userId, showId)
    }

    // ============ SEARCH HISTORY ============
    fun getSearchHistory(userId: Int): Flow<List<SearchHistoryEntity>> {
        return searchHistoryDao.getSearchHistoryByUser(userId)
    }

    fun getAllSearchHistory(): Flow<List<SearchHistoryEntity>> {
        return searchHistoryDao.getAllSearchHistory()
    }

    suspend fun clearHistory(userId: Int) {
        searchHistoryDao.clearUserHistory(userId)
    }

    // ============ RECOMMENDATIONS ============
    suspend fun getRecommendations(userId: Int): Result<List<Show>> {
        return try {
            // Obtener géneros favoritos del usuario
            val favoriteGenres = favoriteDao.getUserFavoriteGenres(userId)
            val genresList = favoriteGenres.flatMap { it.split(",") }
                .map { it.trim() }
                .groupBy { it }
                .mapValues { it.value.size }
                .toList()
                .sortedByDescending { it.second }
                .take(3)
                .map { it.first }

            if (genresList.isEmpty()) {
                // Si no hay favoritos, mostrar shows populares
                val shows = api.getAllShows(0).take(10)
                return Result.success(shows)
            }

            // Buscar shows similares por género
            val recommendations = mutableListOf<Show>()
            for (genre in genresList) {
                val shows = showCacheDao.getShowsByGenre(genre)
                recommendations.addAll(shows.map { it.toShow() })
            }

            // Si el cache está vacío, hacer búsqueda por cada género
            if (recommendations.isEmpty()) {
                for (genre in genresList.take(1)) {
                    val response = api.searchShows(genre)
                    recommendations.addAll(response.map { it.show })
                }
            }

            Result.success(recommendations.distinctBy { it.id }.take(10))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ============ HELPER FUNCTIONS ============
    private fun Show.toShowCacheEntity() = ShowCacheEntity(
        showId = id,
        name = name,
        language = language,
        genres = genres?.joinToString(","),
        status = status,
        premiered = premiered,
        rating = rating?.average,
        imageUrl = image?.medium,
        summary = summary
    )

    private fun ShowCacheEntity.toShow() = Show(
        id = showId,
        name = name,
        language = language,
        genres = genres?.split(","),
        status = status,
        premiered = premiered,
        rating = rating?.let { com.escom.mediafinder.data.model.Rating(it) },
        image = imageUrl?.let { com.escom.mediafinder.data.model.ImageUrl(it, it) },
        summary = summary
    )
}