package br.edu.infnet.livrosbacklog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.infnet.livrosbacklog.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Rubricas de API")
        val results = intent.getStringExtra("json_results")

        binding.jsonResultsTextview.text = results

    }
}