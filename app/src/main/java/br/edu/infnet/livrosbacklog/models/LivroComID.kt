package br.edu.infnet.livrosbacklog.models

data class LivroComID(    val tituloLivro: String = "",
                          val autorLivro: String = "",
                          val categoriaLivro: String = "",
                          val leituraLivro: Boolean = false,
                          val classificacaoLivro: Int = 0,
                          var id: String = "")
