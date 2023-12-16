package com.tegar.fitmate.ui.screens.equimentsearch

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tegar.fitmate.data.model.Exercise
import com.tegar.fitmate.data.remote.model.PredictEquimentResponse
import com.tegar.fitmate.data.util.UiState
import com.tegar.fitmate.repository.PredictEquimentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EquimentSearchViewModel  @Inject constructor(private val repository : PredictEquimentRepository) : ViewModel() {
    private val _predictResult: MutableStateFlow<UiState<PredictEquimentResponse>> =
        MutableStateFlow(UiState.Loading)
    val predict: StateFlow<UiState<PredictEquimentResponse>>
        get() = _predictResult

    fun predictImage(file : File) {
        viewModelScope.launch {
            repository.predict(file).catch {exception ->
                _predictResult.value = UiState.Error(exception.toString())

            }.collect{ data ->
                _predictResult.value = data


            }
        }
    }
    fun onTakePhoto(uri : Uri) {
        Log.d("Bitmap", uri.toString())
    }
}