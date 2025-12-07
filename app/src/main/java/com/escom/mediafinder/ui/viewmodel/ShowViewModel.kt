package com.escom.mediafinder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escom.mediafinder.data.local.FavoriteEntity
import com.escom.mediafinder.data.local.SearchHistoryEntity
import com.escom.mediafinder.data.model.Show
import com.escom.mediafinder.data.repository.MediaFinderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowViewModel @Inject constructor(
    private val repository: MediaFinderRepository
) : ViewModel() {

    private val _shows = MutableStateFlow<List<Show>>(emptyList())
    val shows: StateFlow<List<Show>> = _shows

    private val _recommendations = MutableStateFlow<List<Show>>(emptyList())
    val recommendations: StateFlow<List<Show>> = _recommendations

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _favorites = MutableStateFlow<List<FavoriteEntity>>(emptyList())
    val favorites: StateFlow<List<FavoriteEntity>> = _favorites

    private val _searchHistory = MutableStateFlow<List<SearchHistoryEntity>>(emptyList())
    val searchHistory: StateFlow<List<SearchHistoryEntity>> = _searchHistory

    fun searchShows(query: String, userId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val result = repository.searchShows(query, userId)
            _loading.value = false

            if (result.isSuccess) {
                _shows.value = result.getOrNull() ?: emptyList()
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Error en la búsqueda"
            }
        }
    }

    fun loadFavorites(userId: Int) {
        viewModelScope.launch {
            repository.getFavoritesByUser(userId).collect {
                _favorites.value = it
            }
        }
    }

    fun loadSearchHistory(userId: Int) {
        viewModelScope.launch {
            repository.getSearchHistory(userId).collect {
                _searchHistory.value = it
            }
        }
    }

    fun toggleFavorite(userId: Int, show: Show, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                repository.removeFromFavorites(userId, show.id)
            } else {
                repository.addToFavorites(userId, show)
            }
        }
    }

    suspend fun checkIsFavorite(userId: Int, showId: Int): Boolean {
        return repository.isFavorite(userId, showId)
    }

    fun loadRecommendations(userId: Int) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getRecommendations(userId)
            _loading.value = false

            if (result.isSuccess) {
                _recommendations.value = result.getOrNull() ?: emptyList()
            }
        }
    }

    fun clearHistory(userId: Int) {
        viewModelScope.launch {
            repository.clearHistory(userId)
        }
    }

    fun clearError() {
        _error.value = null
    }
}