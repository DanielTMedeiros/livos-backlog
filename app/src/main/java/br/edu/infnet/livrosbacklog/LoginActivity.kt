package br.edu.infnet.livrosbacklog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.edu.infnet.livrosbacklog.databinding.ActivityLoginBinding
import br.edu.infnet.livrosbacklog.repositorios.LivrosRepository
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var repository: LivrosRepository
    lateinit var mAdView : AdView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        setup()
    }

    override fun onStart() {
        super.onStart()
        if (repository.isLoggedIn()) {
            startMainActivity()
        }


    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setup() {
        setTitle("Login")
        setupOnClickListeners()
        repository = LivrosRepository.get()
    }

    private fun setupOnClickListeners() {
        binding.apply {
            btnLogin.setOnClickListener {
                OnLoginClick()
            }
            btnCadastrar.setOnClickListener {
                if(binding.inputLogin.text.toString() != "" && binding.inputSenha.text.toString() !=""){
                    onCadastrarClick()
                }
            }
        }
    }

    private fun onCadastrarClick() {

        repository.cadastrarUsuarioComSenha(
            binding.inputLogin.text.toString(),
            binding.inputSenha.text.toString(),
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(LoginActivity.TAG, "createUserWithEmail:success")
                //val user = auth.currentUser
                startMainActivity()
            } else {
                // If sign in fails, display a message to the user.
                Log.w(LoginActivity.TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    this, "Cadastro Falhou.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }



    }

    private fun OnLoginClick() {
        repository.login(
            binding.inputLogin.text.toString(),
            binding.inputSenha.text.toString()
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(Companion.TAG, "signInWithEmail:success")
                //val user = auth.currentUser
                startMainActivity()
            } else {
                // If sign in fails, display a message to the user.
                Log.w(Companion.TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(
                    this, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

    companion object {
        const val TAG = "Livros Backlog"
    }
}