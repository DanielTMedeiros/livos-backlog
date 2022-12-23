package br.edu.infnet.livrosbacklog.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.infnet.livrosbacklog.R
import br.edu.infnet.livrosbacklog.adapter.LivroComIdAdapter
import br.edu.infnet.livrosbacklog.adapter.LivroComIdListener
import br.edu.infnet.livrosbacklog.databinding.FragmentLivrosBinding
import br.edu.infnet.livrosbacklog.models.LivroComID
import br.edu.infnet.livrosbacklog.viewmodel.MainViewModel


class LivrosFragment : Fragment() {
    val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentLivrosBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLivrosBinding.inflate(inflater, container, false)
        val view = binding.root
        setup()
        return view
    }


    val adapter = LivroComIdAdapter(
        object : LivroComIdListener {
            override fun onEditClick(livro: LivroComID) {
                viewModel.setSelectedLivroComId(livro)
                nav(R.id.action_livrosFragment_to_editarLivrosFragment)
            }

            override fun onDeleteClick(livro: LivroComID) {
                viewModel.deleteLivro(livro.id)
            }
        }
    )

    private fun setup() {
        setupViews()
        setupClickListeners()
        setupObservers()
        setupRecyclerView()
    }


    private fun setupRecyclerView() {
        // adapter precisa ser uma variável global para ser acessada por todos os métodos
        binding.rvLivros.adapter = adapter
        binding.rvLivros.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun setupObservers() {
        viewModel.livrosComId.observe(viewLifecycleOwner){
            atualizaRecyclerView(it)
        }

    }
    fun atualizaRecyclerView(lista: List<LivroComID>) {
        adapter.submitList(lista)
        binding.rvLivros.adapter = adapter
    }

    private fun setupViews() {
        activity?.setTitle("Editar Livros")
    }

    private fun setupClickListeners() {
        binding.ivBuscaraluno.setOnClickListener {
            filtrarLista(binding.inputBuscarTitulo.text.toString())
        }

        binding.btnCadastrarLivro.setOnClickListener {
            nav(R. id. action_livrosFragment_to_cadastrarLivrosFragment)
        }
    }

    private fun filtrarLista(query: String) {
        val listaAntiga = viewModel.livrosComId.value
        val listaNova = mutableListOf<LivroComID>()

        listaAntiga?.forEach {
            if (it.tituloLivro.contains(query)){
                listaNova.add(it)
            }
        }

        atualizaRecyclerView(listaNova)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

   // fun getLivros(){
   //     val lista = viewModel.getLivros()
   //     toast("tamanho da lista: ${lista.size}")
   // }
}