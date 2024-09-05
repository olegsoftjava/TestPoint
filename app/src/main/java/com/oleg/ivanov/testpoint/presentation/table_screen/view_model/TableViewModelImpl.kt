package com.oleg.ivanov.testpoint.presentation.table_screen.view_model

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oleg.ivanov.testpoint.presentation.visualisation_component.GraphPoint
import com.oleg.ivanov.testpoint.provider.PointsDataProvider
import com.oleg.ivanov.testpoint.util.FileSaver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TableViewModelImpl @Inject constructor(
    private val pointsDataProvider: PointsDataProvider,
) : ViewModel(), TableViewModel {

    private val _viewState = MutableSharedFlow<TableViewState>()
    override val viewState: Flow<TableViewState>
        get() = _viewState

    override fun getListData() {
        viewModelScope.launch {
            _viewState.emit(TableViewState.PointData(pointsDataProvider.getData()))
        }
    }

    override fun saveToFile(context: Context, bitmap: Bitmap) {
        viewModelScope.launch {
            val fileSaver = FileSaver()
            val saveSuccess = fileSaver.saveBitmapToPictures(context, bitmap)
            _viewState.emit(TableViewState.FileSave(saveSuccess))
        }
    }

    override fun getBitmapData() {
        viewModelScope.launch {
            val graphPoint = GraphPoint()
            pointsDataProvider.getData()?.let {
                graphPoint.createBitmap(it)?.let { bitmap ->
                    _viewState.emit(TableViewState.BitmapData(bitmap))
                }
            }
        }
    }
}