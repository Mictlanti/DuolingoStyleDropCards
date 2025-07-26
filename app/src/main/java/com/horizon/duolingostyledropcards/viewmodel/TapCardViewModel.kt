package com.horizon.duolingostyledropcards.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TapCardViewModel : ViewModel() {

    private val _selectedInOrder = mutableStateListOf<Int>()
    val selectedInOrder: SnapshotStateList<Int> = _selectedInOrder

    private val _listHideElements = mutableStateMapOf<Int, Boolean>()
    val listHideElements : SnapshotStateMap<Int, Boolean> = _listHideElements

    private var check = 0

    private val _btnCheck = MutableStateFlow(check)
    val btnCheck : StateFlow<Int> = _btnCheck.asStateFlow()

    fun initCards(listCards: List<String>) {

        _selectedInOrder.clear()
        _listHideElements.clear()

        repeat(listCards.size) {
            _listHideElements[it] = false
        }

    }

    fun onTopCardClicked(i: Int) {
        _listHideElements[i] = false
        _selectedInOrder.remove(i)
    }

    fun onBottomCardClicked(i: Int) {
        _listHideElements[i] = true
        if (!_selectedInOrder.contains(i)) {
            _selectedInOrder.add(i)
        }
    }

    fun onCheckBtnClicked(listCorrect: List<Int>) {
        if (_selectedInOrder.toList() == listCorrect) {
            _btnCheck.value = 1
        } else {
            _btnCheck.value = 2
        }

        resetValueCheck()
    }

    private fun resetValueCheck() {
        viewModelScope.launch {
            delay(1000)
            if(_btnCheck.value != 0) {
                _btnCheck.value = 0
            }
        }
    }

}