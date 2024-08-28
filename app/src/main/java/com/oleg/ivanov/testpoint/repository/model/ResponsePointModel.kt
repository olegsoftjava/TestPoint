package com.oleg.ivanov.testpoint.repository.model

/**
 * Ответ сервера с точками и ошибками сети
 */
data class ResponsePointModel(
    /**
     * Список точек
     */
    val pointModel: List<PointModel>?,
    /**
     * Описание сетевой ошибки, null - ошибок нет
     */
    val errorModel: ErrorModel?,
)
