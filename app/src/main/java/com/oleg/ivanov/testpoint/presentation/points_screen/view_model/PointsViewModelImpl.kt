package com.oleg.ivanov.testpoint.presentation.points_screen.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oleg.ivanov.testpoint.repository.PointsManager
import com.oleg.ivanov.testpoint.repository.model.ErrorModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PointsViewModelImpl @Inject constructor(
    private val pointsManager: PointsManager,
) : ViewModel(), PointsViewModel {

    private val _viewState = MutableSharedFlow<PointsViewState>()
    override val viewState: Flow<PointsViewState>
        get() = _viewState

    override fun getPoints(count: Int) {
        viewModelScope.launch {
            val result = runCatching {
                pointsManager.getPoints(count)
            }

            val pointsViewState = result.fold(
                onSuccess = { responsePointModel ->
                    if (responsePointModel.errorModel == null) {
                        PointsViewState.PointData(responsePointModel.pointModel)
                    } else {
                        PointsViewState.PointError(responsePointModel.errorModel)
                    }
                },
                onFailure = {
                    PointsViewState.PointError(ErrorModel("Unexpected error occurred", -1))
                }
            )

            _viewState.emit(pointsViewState)
        }
    }

}