package com.example.appnasapi.bd

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class PODBDViewModel(private val podRepository: PODRepository) : ViewModel() {
    val allPods: LiveData<MutableList<POD>> = podRepository.allPods.asLiveData()

    fun insert(pod: POD) = viewModelScope.launch {
        podRepository.insert(pod)
    }

    fun delete(pod: POD) = viewModelScope.launch {
        podRepository.delete(pod)
    }

    class PODViewModelFactory(private val podRepository: PODRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PODBDViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PODBDViewModel(podRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}