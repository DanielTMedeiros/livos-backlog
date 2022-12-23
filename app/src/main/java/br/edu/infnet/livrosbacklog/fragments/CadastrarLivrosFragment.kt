package br.edu.infnet.livrosbacklog.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.infnet.livrosbacklog.R
import br.edu.infnet.livrosbacklog.databinding.FragmentCadastrarLivrosBinding
import br.edu.infnet.livrosbacklog.models.Livro
import br.edu.infnet.livrosbacklog.repositorios.LivrosRepository


class CadastrarLivrosFragment : Fragment() {
    private lateinit var repository: LivrosRepository
    private var _binding: FragmentCadastrarLivrosBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCadastrarLivrosBinding.inflate(inflater, container, false)
        val view = binding.root
        setup()
        return view
    }

    private fun setup() {
        repository = LivrosRepository.get()
        setupViews()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnCadastrar.setOnClickListener{
            onCadastrarClick()

        }
        binding.btnVoltar.setOnClickListener {
            nav(R.id. action_cadastrarLivrosFragment_to_livrosFragment )
        }

    }

    private fun onCadastrarClick() {

        val livro = getLivroFromInputs()

        repository.cadastrarLivro(livro)
            .addOnSuccessListener { documentReference ->
                toast("Cadastrado com Sucesso com o id: ${documentReference.id}")
            }.addOnFailureListener {e ->
                toast("Falha ao cadastrar")
            }
    }

    private fun getLivroFromInputs(): Livro {
        binding.apply{
            return Livro(
                tituloLivro = inputTitulo.text.toString(),
                autorLivro = inputAutor.text.toString(),
                categoriaLivro = inputCategoria.text.toString() ,
                leituraLivro = cbLeitura.isChecked,
                classificacaoLivro = rbClassificacao.rating.toInt()

            )
        }

    }


    private fun setupViews() {
        activity?.setTitle("Atualizar Livros")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}