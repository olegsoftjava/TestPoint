package com.oleg.ivanov.testpoint.repository

import android.content.Context
import com.oleg.ivanov.testpoint.R
import com.oleg.ivanov.testpoint.network.NetworkService
import com.oleg.ivanov.testpoint.repository.model.ErrorModel
import com.oleg.ivanov.testpoint.repository.model.PointModel
import com.oleg.ivanov.testpoint.repository.model.ResponsePointModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

class PointsManagerImpl @Inject constructor(
    private val networkService: NetworkService,
    private val context: Context
) : PointsManager {

    override suspend fun getPoints(count: Int): ResponsePointModel {
        if (count <= 0) {
            return ResponsePointModel(
                pointModel = null,
                errorModel = ErrorModel(
                    description = context.getString(R.string.error_quantity_must_be_greater_than_0),
                    code = -1
                )
            )
        }
        return withContext(Dispatchers.Default) {
            try {
                val response =
                    withContext(Dispatchers.IO) { networkService.getPoints(count).execute() }
                when (response.code()) {
                    200 -> {
                        val pointsJson =
                            JSONObject(response.body().toString()).getJSONArray("points")

                        val pointsArray = (0 until pointsJson.length()).map { index ->
                            pointsJson.getJSONObject(index).let {
                                PointModel(
                                    x = it.getDouble("x"),
                                    y = it.getDouble("y")
                                )
                            }
                        }.sortedBy { it.x }

                        ResponsePointModel(
                            pointModel = pointsArray,
                            errorModel = null
                        )
                    }

                    else -> ResponsePointModel(
                        pointModel = null,
                        errorModel = ErrorModel(
                            description = response.errorBody()?.byteString()?.toString()
                                ?: context.getString(R.string.unknown_error),
                            code = response.code()
                        )
                    )
                }
            } catch (e: Exception) {
                ResponsePointModel(
                    pointModel = null,
                    errorModel = ErrorModel(description = e.message.toString(), code = -1)
                )
            }
        }
    }
}