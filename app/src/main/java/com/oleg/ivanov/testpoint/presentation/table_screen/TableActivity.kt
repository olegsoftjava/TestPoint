package com.oleg.ivanov.testpoint.presentation.table_screen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.oleg.ivanov.testpoint.MyApplication
import com.oleg.ivanov.testpoint.MyApplication.Companion.dispatcherMain
import com.oleg.ivanov.testpoint.R
import com.oleg.ivanov.testpoint.databinding.ActivityTableBinding
import com.oleg.ivanov.testpoint.presentation.BaseActivity
import com.oleg.ivanov.testpoint.presentation.ext_ui.animateAlphaDown
import com.oleg.ivanov.testpoint.presentation.ext_ui.animateSpeedWayFromDownToUp
import com.oleg.ivanov.testpoint.presentation.ext_ui.animateSpeedWayFromRightToLeft
import com.oleg.ivanov.testpoint.presentation.ext_ui.animateUpDown
import com.oleg.ivanov.testpoint.presentation.table_screen.adapter.PointAdapter
import com.oleg.ivanov.testpoint.presentation.table_screen.view_model.TableViewModelImpl
import com.oleg.ivanov.testpoint.presentation.table_screen.view_model.TableViewState
import com.oleg.ivanov.testpoint.repository.model.PointModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class TableActivity : BaseActivity<ActivityTableBinding>(ActivityTableBinding::inflate) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var tableViewModel: TableViewModelImpl

    private var jobList: MutableList<Job?> = mutableListOf()

    private var recyclerViewState: Parcelable? = null
    private var currentScale: Float = 1f

    private var isColdStart = true

    init {
        MyApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        tableViewModel =
            ViewModelProvider(this, viewModelFactory)[TableViewModelImpl::class.java]

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_table)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.imageViewBack.setOnClickListener { finish() }

        if (savedInstanceState != null) {
            isColdStart = false
            recyclerViewState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                savedInstanceState.getParcelable("recycler_state", Parcelable::class.java)
            } else {
                @Suppress("DEPRECATION")
                savedInstanceState.getParcelable("recycler_state")
            }
            currentScale = savedInstanceState.getFloat("image_scale", 1.0f)
        }

        render()

        binding.imageView.setOnMatrixChangeListener { rect ->
            val matrixValues = FloatArray(9)
            binding.imageView.imageMatrix.getValues(matrixValues)

            currentScale = matrixValues[Matrix.MSCALE_X]
        }

        jobList.add(CoroutineScope(dispatcherMain).launch {
            tableViewModel.getListData()
            tableViewModel.getBitmapData()
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(
            "recycler_state",
            binding.recyclerViewPoint.layoutManager?.onSaveInstanceState()
        )
        outState.putFloat("image_scale", currentScale)
    }

    override fun onDestroy() {
        super.onDestroy()
        jobList.forEach {
            it?.cancel()
        }
    }

    private fun render() {
        jobList.add(CoroutineScope(dispatcherMain).launch {
            tableViewModel.viewState.collect { data ->
                when (data) {
                    is TableViewState.PointData -> {

                        tryShowOops((data.pointModel?.size ?: 0) > 50)

                        setupPointList(data.pointModel)

                    }

                    is TableViewState.BitmapData -> {

                        setupGraphView(bitmap = data.bitmap)

                        setupButtonSave(
                            bitmap = data.bitmap,
                            needAnimate = isColdStart
                        )

                    }

                    is TableViewState.FileSave -> {
                        showFileResult(data.saveSuccess)
                    }
                }
            }
        })
    }

    @UiThread
    private fun showFileResult(saveSuccess: Boolean) {
        if (saveSuccess) {
            Toast.makeText(
                this@TableActivity,
                "Файл записан, смотрите папку фото",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this@TableActivity,
                "Ошибка записи файла",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun tryShowOops(show: Boolean) {
        if (show && isColdStart) {
            CoroutineScope(dispatcherMain).launch {
                binding.imageOops.isVisible = true
                binding.imageOops.animateSpeedWayFromDownToUp()
                delay(3000)
                binding.imageOops.animateAlphaDown()
                delay(550)
                binding.imageOops.isVisible = false
            }
        }
    }

    private fun setupGraphView(bitmap: Bitmap) {
        Glide.with(this@TableActivity)
            .load(bitmap)
            .into(binding.imageView as ImageView)
        binding.imageView.animateSpeedWayFromDownToUp(0f)
        try {
            binding.imageView.setScale(currentScale, true)
        } catch (_: Exception) {
        }
    }

    private fun setupButtonSave(bitmap: Bitmap, needAnimate: Boolean) {
        binding.imageViewSave.isVisible = true

        if (needAnimate) {
            CoroutineScope(dispatcherMain).launch {
                delay(500)
                binding.imageViewSave.animateUpDown()
            }
        }

        binding.imageViewSave.setOnClickListener {
            it.animateUpDown()
            tableViewModel.saveFile(applicationContext, bitmap)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupPointList(pointModel: List<PointModel>?) {
        binding.recyclerViewPoint.layoutManager =
            LinearLayoutManager(this@TableActivity)
        binding.recyclerViewPoint.isVisible = false
        pointModel?.let {
            binding.recyclerViewPoint.adapter = PointAdapter(dataSource = it)
            binding.recyclerViewPoint.adapter?.notifyDataSetChanged()
            binding.recyclerViewPoint.isVisible = true
            if (isColdStart) {
                binding.recyclerViewPoint.animateSpeedWayFromRightToLeft()
            }

            recyclerViewState?.let { state ->
                binding.recyclerViewPoint.layoutManager?.onRestoreInstanceState(state)
            }
        }
    }
}

