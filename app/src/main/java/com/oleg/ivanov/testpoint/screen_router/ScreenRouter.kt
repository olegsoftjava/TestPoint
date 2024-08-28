package com.oleg.ivanov.testpoint.screen_router

import android.app.Activity
import com.oleg.ivanov.testpoint.repository.model.PointModel

/**
 * Для переключения экранов
 */
interface ScreenRouter {
    /**
     * Открыть экран таблицей
     */
    fun openTableScreen(activity: Activity, data: List<PointModel>?)
}