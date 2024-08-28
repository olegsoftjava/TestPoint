package com.oleg.ivanov.testpoint.presentation.points_screen.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oleg.ivanov.testpoint.MyApplication.Companion.dispatcherIO
import com.oleg.ivanov.testpoint.repository.PointsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PointsViewModelImpl @Inject constructor(
    private val pointsManager: PointsManager,
) : ViewModel(), PointsViewModel {

    private val _viewState = MutableSharedFlow<PointsViewState>()
    override val viewState: Flow<PointsViewState>
        get() = _viewState

    override fun getPoints(count: Int) {
        viewModelScope.launch {
            withContext(dispatcherIO) {
                pointsManager.getPoints(count).let {
                    if (it.errorModel == null) {
                        _viewState.emit(PointsViewState.PointData(it.pointModel))
                    } else {
                        _viewState.emit(PointsViewState.PointError(it.errorModel))
                    }
                }
            }
        }
    }

}