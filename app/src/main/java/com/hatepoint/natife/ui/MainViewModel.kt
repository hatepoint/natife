package com.hatepoint.natife.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hatepoint.natife.data.GiphyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel(val repository: GiphyRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<MainState>(MainState.Loading)
    val uiState: StateFlow<MainState> = _uiState
    private val _spanCount = MutableStateFlow(2)
    val spanCount: StateFlow<Int> = _spanCount

    init {
        viewModelScope.launch {
            getGifs().collect {
                _uiState.value = it
            }
        }
    }

    suspend fun getGifs(): Flow<MainState> = flow {
        try {
            _uiState.value = MainState.Loading
            val response = repository.getTrendingGifs(limit = 25, rating = "g")
            if (response.isSuccessful) {
                val images = mutableListOf<String>()
                images.addAll(response.body()!!.data.map { it.images.fixed_height.url })
                _uiState.value = MainState.Success(images)
            } else {
                _uiState.value = MainState.Error(response.message())
            }
        } catch (e: Exception) {
            _uiState.value = MainState.Error(e.message.toString())
        }
    }

    fun setSpanCount(spans: Int) {
        _spanCount.value = spans
    }

}

sealed class MainState() {
    object Loading : MainState()
    data class Success(val data: List<String>) : MainState()
    data class Error(val error: String) : MainState()
}