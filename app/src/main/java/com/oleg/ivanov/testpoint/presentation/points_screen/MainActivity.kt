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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupButtonStart()

        render()

    }

    private fun render() {
        lifecycleScope.launch {
            pointsViewModel.viewState.collect { data ->
                updateUI(isLoading = false)
                when (data) {
                    is PointsViewState.PointData -> {
                        screenRouter.openTableScreen(this@MainActivity, data.pointModel)
                    }

                    is PointsViewState.PointError -> {
                        withContext(Dispatchers.Main) {
                            showError()
                            binding.buttonStart.animateLeftRight()
                            Toast.makeText(
                                /* context = */ this@MainActivity,
                                /* text = */
                                "code:${data.errorModel.code} Error:${data.errorModel.description}",
                                /* duration = */
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupButtonStart() {
        binding.buttonStart.animateUpDown()

        binding.buttonStart.setOnClickListener {
            handleButtonStart()
        }

        binding.editTextCount.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_SEND) {
                handleButtonStart()
                true
            } else {
                false
            }
        }
    }

    private fun handleButtonStart() {
        updateUI(isLoading = true)
        try {
            pointsViewModel.getPoints(binding.editTextCount.text.toString().toInt())
        } catch (_: Exception) {
            binding.textCountInputLayout.animateLeftRight()
            showError()
            updateUI(isLoading = false)
            Toast.makeText(
                this@MainActivity,
                "Введено не верное количество.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateUI(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.buttonStart.isEnabled = !isLoading
    }

    private fun showError() {
        lifecycleScope.launch {
            binding.imageViewError.isVisible = true
            binding.imageViewError.animateSpeedWayFromDownToUp()
            delay(1000)
            binding.imageViewError.animateAlphaDown()
            delay(700)
            binding.imageViewError.isVisible = false
        }
    }

}