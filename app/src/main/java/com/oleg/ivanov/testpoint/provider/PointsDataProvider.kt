package com.oleg.ivanov.testpoint.provider

import com.oleg.ivanov.testpoint.repository.model.PointModel

/**
 * Для переброски данных
 */
interface PointsDataProvider {
    fun sendData(pointModel: List<PointModel>?)
    fun getData(): List<PointModel>?
}