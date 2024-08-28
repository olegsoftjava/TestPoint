package com.oleg.ivanov.testpoint.presentation.table_screen.view_model

import android.graphics.Bitmap
import com.oleg.ivanov.testpoint.repository.model.PointModel

sealed class TableViewState {
    internal data class PointData(val pointModel: List<PointModel>?) : TableViewState()
    internal data class BitmapData(val bitmap: Bitmap) : TableViewState()
    internal data class FileSave(val saveSuccess: Boolean) : TableViewState()
}