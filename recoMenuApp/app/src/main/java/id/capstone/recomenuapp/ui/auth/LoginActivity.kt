package id.capstone.recomenuapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.capstone.recomenuapp.databinding.ActivityLoginBinding
import id.capstone.recomenuapp.model.ResponseSession
import id.capstone.recomenuapp.ui.HomeActivity
import id.capstone.recomenuapp.ui.viewmodel.AuthViewModel
import id.capstone.recomenuapp.ui.viewmodel.Factory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<AuthViewModel> { factory }
    private lateinit var factory: Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = Factory.getInstance(this)
        setupListener()
    }

    private fun setupListener() {
        binding.btnLogin.setOnClickListener {
            when {

                binding.etEmail.text.toString().trim().isEmpty() -> {
                    binding.etEmail.error = "Insert Email!"
                    binding.etEmail.requestFocus()
                }

                !Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString().trim())
                    .matches() -> {
                    binding.etEmail.error = "Email is not valid!"
                    binding.etEmail.requestFocus()
                }

                binding.etPassword.text.toString().trim().isEmpty() -> {
                    binding.etPassword.error = "Insert Password"
                    binding.etPassword.requestFocus()
                }

                else -> {
                    binding.apply {
                        viewModel.postLogin(
                            etEmail.text.toString(),
                            etPassword.text.toString(),
                        )
                    }
                    viewModel.responseLogin.observe(this@LoginActivity) { response ->
                        if (response.token != null && response.email != null) {
                            viewModel.getAllUsers(response.token)
                            viewModel.responseGetAllUsers.observe(this@LoginActivity) {
                                val session = it.find { registerModel ->
                                    registerModel.email == response.email
                                }
                                if (session != null) {
                                    viewModel.saveSession(
                                        ResponseSession(
                                            response.email,
                                            response.token,
                                            true,
                                            session.id.orEmpty(),
                                            session.name.orEmpty(),
                                            session.phone.orEmpty(),
                                            session.likeIngredient.orEmpty()
                                        )
                                    )
                                }
                                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            Toast.makeText(this, "Error login", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}