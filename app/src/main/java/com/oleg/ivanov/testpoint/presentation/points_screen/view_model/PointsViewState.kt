package com.oleg.ivanov.testpoint.presentation.points_screen.view_model

import com.oleg.ivanov.testpoint.repository.model.ErrorModel
import com.oleg.ivanov.testpoint.repository.model.PointModel

sealed class PointsViewState {
    internal data class PointData(val pointModel: List<PointModel>?) : PointsViewState()
    internal data class PointError(val errorModel: ErrorModel) : PointsViewState()
}