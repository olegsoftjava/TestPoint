package com.oleg.ivanov.testpoint.screen_router

import android.app.Activity
import android.content.Intent
import com.oleg.ivanov.testpoint.presentation.table_screen.TableActivity
import com.oleg.ivanov.testpoint.provider.PointsDataProvider
import com.oleg.ivanov.testpoint.repository.model.PointModel
import javax.inject.Inject

class ScreenRouterImpl @Inject constructor(
    private val pointsDataProvider: PointsDataProvider,
) : ScreenRouter {

    override fun openTableScreen(activity: Activity, data: List<PointModel>?) {
        pointsDataProvider.sendData(data) // Данных много, по этому надо како-то провайдер бандл не прокатит
        val intentTableActivity =
            Intent(activity, TableActivity::class.java)
        activity.startActivity(intentTableActivity)
    }
}