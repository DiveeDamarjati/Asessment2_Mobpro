package org.d3if0106.asessment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if0106.asessment2.database.TinggiDao
import org.d3if0106.asessment2.model.Tinggi

class MainViewModel(dao: TinggiDao) : ViewModel() {
    val data: StateFlow<List<Tinggi>> = dao.getData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}