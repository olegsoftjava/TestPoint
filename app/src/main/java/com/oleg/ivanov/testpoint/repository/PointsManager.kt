package com.oleg.ivanov.testpoint.repository

import com.oleg.ivanov.testpoint.repository.model.ResponsePointModel

/**
 * Менеджер points
 */
interface PointsManager {
    /**
     * Получить точки
     * @param count - кол-во запрашиваемых точек
     */
    suspend fun getPoints(count: Int): ResponsePointModel
}