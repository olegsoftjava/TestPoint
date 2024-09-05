package com.oleg.ivanov.testpoint.presentation.points_screen

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.oleg.ivanov.testpoint.MyApplication
import com.oleg.ivanov.testpoint.R
import com.oleg.ivanov.testpoint.databinding.ActivityMainBinding
import com.oleg.ivanov.testpoint.presentation.BaseActivity
import com.oleg.ivanov.testpoint.presentation.ext_ui.animateAlphaDown
import com.oleg.ivanov.testpoint.presentation.ext_ui.animateLeftRight
import com.oleg.ivanov.testpoint.presentation.ext_ui.animateSpeedWayFromDownToUp
import com.oleg.ivanov.testpoint.presentation.ext_ui.animateUpDown
import com.oleg.ivanov.testpoint.presentation.points_screen.view_model.PointsViewModelImpl
import com.oleg.ivanov.testpoint.presentation.points_screen.view_model.PointsViewState
import com.oleg.ivanov.testpoint.screen_router.ScreenRouter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val pointsViewModel: PointsViewModelImpl by lazy {
        ViewModelProvider(this, viewModelFactory)[PointsViewModelImpl::class.java]
    }

    @Inject
    lateinit var screenRouter: ScreenRouter

    init {
        MyApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setupInsets()
        setupButtonStart()
        render()
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun render() {
        lifecycleScope.launch {
            pointsViewModel.viewState.collect { data ->
                updateUI(isLoading = false)
                when (data) {
                    is PointsViewState.PointData -> screenRouter.openTableScreen(
                        this@MainActivity,
                        data.pointModel
                    )

                    is PointsViewState.PointError -> showErrorAndRetry(data)
                }
            }
        }
    }

    private fun showErrorAndRetry(data: PointsViewState.PointError) {
        showError(data.errorModel.code)
        binding.buttonStart.animateLeftRight()
        showToast("code:${data.errorModel.code} Error:${data.errorModel.description}")
    }

    private fun setupButtonStart() {
        binding.buttonStart.animateUpDown()

        binding.buttonStart.setOnClickListener {
            onStartButtonClicked()
        }

        binding.editTextCount.setOnEditorActionListener { _, actionId, _ ->
            if (actionId in listOf(
                    EditorInfo.IME_ACTION_DONE,
                    EditorInfo.IME_ACTION_GO,
                    EditorInfo.IME_ACTION_SEND
                )
            ) {
                onStartButtonClicked()
                true
            } else {
                false
            }
        }
    }

    private fun onStartButtonClicked() {
        updateUI(isLoading = true)
        try {
            val count = binding.editTextCount.text.toString().toInt()
            pointsViewModel.getPoints(count)
        } catch (e: NumberFormatException) {
            showErrorOnInput()
        }
    }

    private fun showErrorOnInput() {
        binding.textCountInputLayout.animateLeftRight()
        showError(0)
        updateUI(isLoading = false)
        showToast("Введено не верное количество.")
    }

    private fun updateUI(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.buttonStart.isEnabled = !isLoading
    }

    private fun showError(errorCode: Int) {
        lifecycleScope.launch {
            if (errorCode == 500) {
                binding.imageViewError.setImageResource(R.drawable.ic_error_500)
            } else {
                binding.imageViewError.setImageResource(R.drawable.ic_error)
            }
            binding.imageViewError.isVisible = true
            binding.imageViewError.animateSpeedWayFromDownToUp()
            delay(1000)
            binding.imageViewError.animateAlphaDown()
            delay(700)
            binding.imageViewError.isVisible = false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
