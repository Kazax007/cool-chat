package ru.kazax.chat.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.kazax.chat.MainActivity
import ru.kazax.chat.R
import ru.kazax.chat.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        val username = binding.editTextLogin
        val email = binding.editTextEmail
        val password = binding.editTextPassword
        val passwordRetype = binding.editTextPasswordAgain
        val loading = binding.progressBar
        val signupBtn = binding.signupBtn

        loginViewModel.loginFormState.observe(this@SignupActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            signupBtn.isEnabled = loginState.isDataValid

            if (loginState.emailError != null) {
                email.error = getString(loginState.emailError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@SignupActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
        })

        email.afterTextChanged {
            loginViewModel.signUpDataChanged(
                email.text.toString(),
                password.text.toString(),
                passwordRetype.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.signUpDataChanged(
                    email.text.toString(),
                    password.text.toString(),
                    passwordRetype.text.toString()
                )
            }
        }

        passwordRetype.apply {
            afterTextChanged {
                loginViewModel.signUpDataChanged(
                    email.text.toString(),
                    password.text.toString(),
                    passwordRetype.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.signup(
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }
        }

        signupBtn.setOnClickListener {
            loading.visibility = View.VISIBLE
            loginViewModel.signup(
                email.text.toString(),
                password.text.toString()
            )
        }

    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}