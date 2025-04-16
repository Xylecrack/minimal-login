package com.example.loginapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapp.data.DatabaseHelper
import com.example.loginapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            if (validateInputs()) {
                loginUser()
            }
        }

        binding.registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.githubIcon.setOnClickListener {
            val githubUrl = "https://github.com/Xylecrack"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl))
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        val user = databaseHelper.checkUser(email, password)

        if (user != null) {
            Toast.makeText(this, "Welcome back, ${user.name}!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("USERNAME", user.name)
            }
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Email validation
        if (binding.emailEditText.text.isNullOrBlank()) {
            binding.emailTextInputLayout.error = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString()).matches()) {
            binding.emailTextInputLayout.error = "Invalid email format"
            isValid = false
        } else {
            binding.emailTextInputLayout.error = null
        }

        // Password validation
        if (binding.passwordEditText.text.isNullOrBlank()) {
            binding.passwordTextInputLayout.error = "Password is required"
            isValid = false
        } else {
            binding.passwordTextInputLayout.error = null
        }

        return isValid
    }
}