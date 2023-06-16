package id.capstone.recomenuapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.capstone.recomenuapp.R
import id.capstone.recomenuapp.databinding.ActivityRegisterBinding
import id.capstone.recomenuapp.ui.viewmodel.Factory
import id.capstone.recomenuapp.ui.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<AuthViewModel> { factory }
    private lateinit var factory: Factory
    private var likeIngredient: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = Factory.getInstance(this)
        setSpinnerAdapter()
        setupSpinnerAction()
        setupListener()
    }

    private fun setupListener() {
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnRegister.setOnClickListener {
            when {
                binding.etName.text.toString().trim().isEmpty() -> {
                    binding.etName.error = "Insert Name!"
                    binding.etName.requestFocus()
                }

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

                binding.etPhoneNumber.text.toString().trim().isEmpty() -> {
                    binding.etPhoneNumber.error = "Insert Phone Number"
                }

                likeIngredient.isNullOrEmpty() -> {
                    binding.spinnerLikeIngredient.error = "Fill your like ingredient!"
                    binding.spinnerLikeIngredient.requestFocus()
                }

                else -> {
                    binding.apply {
                        viewModel.postRegister(
                            etName.text.toString(),
                            etEmail.text.toString(),
                            etPassword.text.toString(),
                            etPhoneNumber.text.toString(),
                            likeIngredient.orEmpty(),
                            isAdmin = false
                        )
                    }
                    viewModel.responseRegister.observe(this@RegisterActivity) { response ->
                        if (response.id != null) {
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun setSpinnerAdapter() {
        binding.spinnerLikeIngredient.setAdapter(
            ArrayAdapter(
                this, R.layout.dropdown_list, resources.getStringArray(R.array.adapter_ingredient)
            )
        )
    }

    private fun setupSpinnerAction() {
        binding.apply {
            spinnerLikeIngredient.setOnItemClickListener { parent, _, position, _ ->
                likeIngredient = resources.getStringArray(R.array.adapter_ingredient)[position]
            }
        }

    }

}