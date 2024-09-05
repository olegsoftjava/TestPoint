package com.oleg.ivanov.testpoint.presentation.visualisation_component

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.oleg.ivanov.testpoint.repository.model.PointModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * О боже, вы реально смотрите код =)
 * Можно сделать на канвасе как здесь, можно заморочиться с recyclerView если масштаб не нужен
 * Можно векторную штуку наваять но это овер-кодинг для тестового =)))
 */

class GraphPoint {

    suspend fun createBitmap(points: List<PointModel>): Bitmap? {

        var bitmap: Bitmap? = null

        withContext(Dispatchers.IO) {
            val width = 4000
            val height = 4000

            // Определяем минимальные и максимальные значения X и Y
            val minX = points.minByOrNull { it.x }?.x?.toFloat() ?: 0f
            val maxX = points.maxByOrNull { it.x }?.x?.toFloat() ?: 0f
            val minY = points.minByOrNull { it.y }?.y?.toFloat() ?: 0f
            val maxY = points.maxByOrNull { it.y }?.y?.toFloat() ?: 0f

            // Масштабируем X и Y, чтобы заполнить ширину и высоту холста
            val scaleX = width / (maxX - minX)
            val scaleY = height / (maxY - minY)

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap!!)

            // Заливаем фон белым цветом
            canvas.drawColor(Color.WHITE)

            val paintOrdinateXY = Paint().apply {
                color = Color.BLACK
                strokeWidth = 5f
                textSize = 50f
            }

            val paintGrid = Paint().apply {
                color = Color.LTGRAY
                strokeWidth = 2f
                style = Paint.Style.STROKE
            }

            val paintPoint = Paint().apply {
                color = Color.RED
                strokeWidth = 4f
                isAntiAlias = true
            }

            val paintCurve = Paint().apply {
                color = Color.RED
                strokeWidth = 4f
                style = Paint.Style.STROKE
                isAntiAlias = true
            }

            // Рисуем координатную сетку
            val centerX = width / 2f
            val centerY = height / 2f

            // Вертикальные линии сетки
            for (i in 0 until width step 100) {
                canvas.drawLine(i.toFloat(), 0f, i.toFloat(), height.toFloat(), paintGrid)
            }

            // Горизонтальные линии сетки
            for (i in 0 until height step 100) {
                canvas.drawLine(0f, i.toFloat(), width.toFloat(), i.toFloat(), paintGrid)
            }

            // Рисуем оси X и Y
            canvas.drawLine(centerX, 0f, centerX, height.toFloat(), paintOrdinateXY) // Ось Y
            canvas.drawLine(0f, centerY, width.toFloat(), centerY, paintOrdinateXY)  // Ось X

            // Подписи осей
            canvas.drawText("X", width - 50f, centerY - 20f, paintOrdinateXY)
            canvas.drawText("Y", centerX + 20f, 50f, paintOrdinateXY)

            // Рисуем точки и линии на графике относительно осей
            val path = Path()
            for (i in points.indices) {
                val point = points[i]

                val x = centerX + (point.x.toFloat() - (minX + maxX) / 2) * scaleX / 2
                val y = centerY - (point.y.toFloat() - (minY + maxY) / 2) * scaleY / 2

                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    val prevPoint = points[i - 1]
                    val prevX = centerX + (prevPoint.x.toFloat() - (minX + maxX) / 2) * scaleX / 2
                    val prevY = centerY - (prevPoint.y.toFloat() - (minY + maxY) / 2) * scaleY / 2

                    val midX = (prevX + x) / 2
                    val midY = (prevY + y) / 2

                    path.quadTo(prevX, prevY, midX, midY)
                }

                // Рисуем точки
                canvas.drawCircle(x, y, 5f, paintPoint)
                canvas.drawText("${point.x};${point.y}", x + 10f, y - 10f, paintOrdinateXY)
            }

            // Дорисовываем последнюю точку кривой
            if (points.isNotEmpty()) {
                val lastPoint = points.last()
                val lastX = centerX + (lastPoint.x.toFloat() - (minX + maxX) / 2) * scaleX / 2
                val lastY = centerY - (lastPoint.y.toFloat() - (minY + maxY) / 2) * scaleY / 2
                path.lineTo(lastX, lastY)
            }

            // Рисуем кривую на Canvas
            canvas.drawPath(path, paintCurve)
        }

        return bitmap
    }

}