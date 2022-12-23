package br.edu.infnet.livrosbacklog.application

import android.app.Application
import br.edu.infnet.livrosbacklog.repositorios.LivrosRepository

class LivrosApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        LivrosRepository.initialize()

    }
}