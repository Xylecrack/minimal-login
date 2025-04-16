package com.example.loginapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapp.data.DatabaseHelper
import com.example.loginapp.data.User
import com.example.loginapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.registerButton.setOnClickListener {
            if (validateInputs()) {
                registerUser()
            }
        }

        binding.loginTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.githubIcon.setOnClickListener {
            val githubUrl = "https://github.com/Xylecrack"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl))
            startActivity(intent)
        }
    }

    private fun registerUser() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordInput.text.toString()

        if (databaseHelper.isEmailExists(email)) {
            binding.emailTextInputLayout.error = "Email already registered"
            return
        }

        val user = User(name = name, email = email, password = password)
        val id = databaseHelper.addUser(user)

        if (id > 0) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Name validation
        if (binding.nameEditText.text.isNullOrBlank()) {
            binding.nameTextInputLayout.error = "Name is required"
            isValid = false
        } else {
            binding.nameTextInputLayout.error = null
        }

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
        if (binding.passwordInput.text.isNullOrBlank()) {
            binding.passwordLayout.error = "Password is required"
            isValid = false
        } else if (binding.passwordInput.text!!.length < 6) {
            binding.passwordLayout.error = "Password must be at least 6 characters"
            isValid = false
        } else {
            binding.passwordLayout.error = null
        }

        // Confirm password validation
        if (binding.confirmPasswordInput.text.isNullOrBlank()) {
            binding.confirmPasswordLayout.error = "Please confirm your password"
            isValid = false
        } else if (binding.passwordInput.text.toString() != binding.confirmPasswordInput.text.toString()) {
            binding.confirmPasswordLayout.error = "Passwords do not match"
            isValid = false
        } else {
            binding.confirmPasswordLayout.error = null
        }

        return isValid
    }
}