package br.edu.infnet.livrosbacklog.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import br.edu.infnet.livrosbacklog.R
import br.edu.infnet.livrosbacklog.databinding.FragmentCadastrarLivrosBinding
import br.edu.infnet.livrosbacklog.databinding.FragmentEditarLivrosBinding
import br.edu.infnet.livrosbacklog.models.Livro
import br.edu.infnet.livrosbacklog.repositorios.LivrosRepository
import br.edu.infnet.livrosbacklog.viewmodel.MainViewModel


class EditarLivrosFragment : Fragment() {

    val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentEditarLivrosBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditarLivrosBinding.inflate(inflater, container, false)
        val view = binding.root
        setup()
        return view
    }

    private fun setup() {
        setupViews()
        setupObservers()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.apply{
            btnAtualizar.setOnClickListener {
                onAtualizarClick()
                navUp()
            }
            btnVoltar.setOnClickListener {
                nav(R.id.action_editarLivrosFragment_to_livrosFragment)
            }
        }
    }

    private fun onAtualizarClick() {
        val livro = getLivroFromInputs()
        viewModel.atualizaLivro(livro)

    }

    private fun getLivroFromInputs(): Livro {
        binding.apply {
            return Livro(
                tituloLivro = getTextInput(inputTitulo),
                autorLivro = getTextInput(inputAutor),
                categoriaLivro = getTextInput(inputCategoria),
                leituraLivro = cbLeitura.isChecked,
                classificacaoLivro = rbClassificacao.rating.toInt()
            )
        }
    }

    private fun setupObservers() {
        viewModel.selectedLivroComId.observe(viewLifecycleOwner){
            binding.apply {
                inputTitulo.setText(it.tituloLivro)
                inputAutor.setText(it.autorLivro)
                inputCategoria.setText(it.categoriaLivro)
                cbLeitura.isChecked()
                rbClassificacao.setRating(it.classificacaoLivro.toFloat())
            }
        }
    }

    private fun setupViews() {
        activity?.setTitle("Editar Livro")
    }
}