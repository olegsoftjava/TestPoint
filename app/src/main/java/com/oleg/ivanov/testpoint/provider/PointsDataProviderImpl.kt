package com.oleg.ivanov.testpoint.provider

import com.oleg.ivanov.testpoint.repository.model.PointModel
import javax.inject.Inject

class PointsDataProviderImpl @Inject constructor() : PointsDataProvider {
    private var pointModel: List<PointModel>? = null

    override fun sendData(pointModel: List<PointModel>?) {
        this.pointModel = pointModel
    }

    override fun getData(): List<PointModel>? = pointModel
}