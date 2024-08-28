package com.oleg.ivanov.testpoint.presentation.points_screen.view_model

import kotlinx.coroutines.flow.Flow

/**
 * Для работы с данными PointModel
 */
interface PointsViewModel {
    val viewState: Flow<PointsViewState>

    /**
     * Получить список точек
     */
    fun getPoints(count: Int)
}