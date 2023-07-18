package com.example.neolight.ui.flashoption

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.neolight.NeoLightApplication
import com.example.neolight.data.FlashOptionsRepository
import com.example.neolight.data.local.database.FlashOption
import kotlinx.coroutines.launch

class FlashOptionViewModel(
    private val flashOptionsRepository: FlashOptionsRepository
) : ViewModel() {

    val allFlashOptions : LiveData<List<FlashOption>> = flashOptionsRepository.allOptions
    var selected: FlashOption = FlashOption(1, "Default", 0)

    fun insert(flashOption: FlashOption) = viewModelScope.launch {
        flashOptionsRepository.insert(flashOption)
    }

    fun delete(name: String) = viewModelScope.launch {
        flashOptionsRepository.delete(name)
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val flashOptionsRepository = (this[APPLICATION_KEY] as NeoLightApplication).repository
                FlashOptionViewModel(flashOptionsRepository = flashOptionsRepository)
            }
        }
    }
}