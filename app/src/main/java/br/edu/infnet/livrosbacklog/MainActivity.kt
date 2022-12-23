package br.edu.infnet.livrosbacklog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.infnet.livrosbacklog.databinding.ActivityLoginBinding
import br.edu.infnet.livrosbacklog.databinding.ActivityMainBinding
import br.edu.infnet.livrosbacklog.repositorios.LivrosRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: LivrosRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setup()
    }

    private fun setup() {
        setTitle("Principal")
        repository = LivrosRepository.get()
        setupViews()
        SetupOnClickListeners()
    }

    private fun SetupOnClickListeners(){
        binding.btnLogout.setOnClickListener{
            repository.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setupViews() {
        binding.tvBemVindo.text = "Seja bem vindo, ${repository.getCurrentUser()?.email}"
    }
}