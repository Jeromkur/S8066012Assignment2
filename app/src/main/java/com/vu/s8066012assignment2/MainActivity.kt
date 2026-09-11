package com.vu.s8066012assignment2


import android.content.Intent
import com.vu.s8066012assignment2.ui.dashboard.DashboardActivity
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
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.vu.s8066012assignment2.ui.login.LoginUiState
import com.vu.s8066012assignment2.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Keeps content clear of system bars and the keyboard.
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View>(R.id.main)
        ) { view, insets ->
            val padding = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.ime()
            )
            view.setPadding(
                padding.left,
                padding.top,
                padding.right,
                padding.bottom
            )
            insets
        }

        val usernameInput =
            findViewById<TextInputEditText>(R.id.usernameInput)
        val passwordInput =
            findViewById<TextInputEditText>(R.id.passwordInput)
        val loginButton =
            findViewById<MaterialButton>(R.id.loginButton)
        val progress =
            findViewById<ProgressBar>(R.id.loginProgress)
        val message =
            findViewById<TextView>(R.id.loginError)

        loginButton.setOnClickListener {
            viewModel.login(
                username = usernameInput.text?.toString().orEmpty(),
                password = passwordInput.text?.toString().orEmpty()
            )
        }

        // Observes login state while the screen is visible.
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val loading = state is LoginUiState.Loading

                    progress.isVisible = loading
                    loginButton.isEnabled = !loading
                    usernameInput.isEnabled = !loading
                    passwordInput.isEnabled = !loading

                    message.isVisible =
                        state is LoginUiState.Error ||
                                state is LoginUiState.Success

                    message.text = when (state) {
                        is LoginUiState.Error -> state.message

                        // Opens the Dashboard after successful login.
                        is LoginUiState.Success -> {
                            if (!isFinishing) {
                                val dashboardIntent = Intent(
                                    this@MainActivity,
                                    DashboardActivity::class.java
                                )
                                dashboardIntent.putExtra(
                                    DashboardActivity.EXTRA_KEYPASS,
                                    state.keypass
                                )
                                startActivity(dashboardIntent)
                                finish()
                            }
                            ""
                        }

                        else -> ""
                    }
                }
            }
        }
    }
}