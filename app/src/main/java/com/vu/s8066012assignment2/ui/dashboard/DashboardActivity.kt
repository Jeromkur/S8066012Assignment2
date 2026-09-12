package com.vu.s8066012assignment2.ui.dashboard

import com.vu.s8066012assignment2.ui.details.DetailsActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.vu.s8066012assignment2.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View>(R.id.dashboardRoot)
        ) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val keypass = intent.getStringExtra(EXTRA_KEYPASS).orEmpty()

        val recyclerView =
            findViewById<RecyclerView>(R.id.animalsRecyclerView)
        val progress =
            findViewById<ProgressBar>(R.id.dashboardProgress)
        val message =
            findViewById<TextView>(R.id.dashboardMessage)
        val count =
            findViewById<TextView>(R.id.animalCount)
        val retryButton =
            findViewById<MaterialButton>(R.id.retryButton)

        val animalAdapter = AnimalAdapter { animal ->
            startActivity(DetailsActivity.createIntent(this, animal))
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = animalAdapter

        retryButton.setOnClickListener {
            viewModel.loadAnimals(keypass, forceRefresh = true)
        }

        // Updates the screen when loading, data or errors change.
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    progress.isVisible = state is DashboardUiState.Loading
                    retryButton.isVisible = state is DashboardUiState.Error
                    count.isVisible = state is DashboardUiState.Success
                    recyclerView.isVisible = state is DashboardUiState.Success
                    message.isVisible = false

                    when (state) {
                        is DashboardUiState.Success -> {
                            animalAdapter.submitList(state.animals)
                            count.text = getString(
                                R.string.animal_count,
                                state.total
                            )

                            if (state.animals.isEmpty()) {
                                message.setText(R.string.no_animals)
                                message.isVisible = true
                            }
                        }

                        is DashboardUiState.Error -> {
                            message.text = state.message
                            message.isVisible = true
                        }

                        else -> Unit
                    }
                }
            }
        }

        viewModel.loadAnimals(keypass)
    }

    companion object {
        const val EXTRA_KEYPASS = "keypass"
    }
}