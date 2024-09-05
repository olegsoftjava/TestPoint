package com.oleg.ivanov.testpoint.presentation.table_screen.view_model

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface TableViewModel {
    val viewState: Flow<TableViewState>
    fun getListData()
    fun getBitmapData()
    fun saveToFile(context: Context, bitmap: Bitmap)
}