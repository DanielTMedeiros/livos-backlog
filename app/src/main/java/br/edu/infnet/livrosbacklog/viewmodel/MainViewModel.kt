package br.edu.infnet.livrosbacklog.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.edu.infnet.livrosbacklog.models.Livro
import br.edu.infnet.livrosbacklog.models.LivroComID
import br.edu.infnet.livrosbacklog.repositorios.LivrosRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObject

class MainViewModel: ViewModel(){

    val TAG = "ViewModel"
    val repository = LivrosRepository.get()


    fun getCurrentUserEmail(): String {
        return repository.getCurrentUser()?.email ?: "Email não encontrado"
    }

    fun logout() {
        repository.logout()
    }

    fun cadastrarLivro(livro: Livro): Task<DocumentReference> {
        return repository.cadastrarLivro(livro)
    }

    fun observeColecaoLivros() {

        repository.getLivrosColecao()
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "listen:error", e)
                    return@addSnapshotListener
                }

                val listaInput = mutableListOf<LivroComID>()

                val listaRemocao = mutableListOf<String>()

                val listaModificacao = mutableListOf<LivroComID>()

                // Ver alterações entre instantâneos
                // https://firebase.google.com/docs/firestore/query-data/listen?hl=pt&authuser=0#view_changes_between_snapshots
                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {

                        // Documento adicionado
                        DocumentChange.Type.ADDED -> {

                            val livro = dc.document.toObject<Livro>()
                            val id = dc.document.id
                            val livroComId = livroToLivroComId(livro, id)

                            Log.i(TAG, "turmaComId: ${livroComId}")
                            listaInput.add(livroComId)

                        }

                        // Documento modificado
                        DocumentChange.Type.MODIFIED -> {
                            val livro = dc.document.toObject<Livro>()
                            val id = dc.document.id
                            val livroComId = livroToLivroComId(livro, id)

                            Log.i(TAG, "Modificacao - turmaComId: ${livroComId}")
                            listaModificacao.add(livroComId)
                        }

                        // Documento removido
                        DocumentChange.Type.REMOVED -> {
                            val id = dc.document.id
                            Log.i(TAG, "id removido: ${id}")
                            listaRemocao.add(dc.document.id)

                        }
                    }
                }

                addListaToLivrosComId(listaInput)
                removeFromLivrosComId(listaRemocao)
                modifyInLivrosComId(listaModificacao)
            }
    }

    fun modifyItemInListaLivrosComId(itemModificado: LivroComID) {
        val listaAntiga = livrosComId.value
        val listaNova = mutableListOf<LivroComID>()

        listaAntiga?.forEach { itemDaLista ->
            if (itemModificado.id == itemDaLista.id) {
                listaNova.add(itemModificado)
            } else {
                listaNova.add(itemDaLista)
            }
        }
        setLivrosComId(listaNova)
    }

    private fun modifyInLivrosComId(listaModificacao: List<LivroComID>) {
        Log.i(TAG, "listaModificacao: ${listaModificacao}")
        if (listaModificacao.isNotEmpty()) {
            for (itemModificado in listaModificacao) {
                modifyItemInListaLivrosComId(itemModificado)
            }
        }
    }

    private fun removeFromLivrosComId(listaRemocao: List<String>) {

        val listaAntiga = livrosComId.value

        val listaNova = mutableListOf<LivroComID>()

        Log.i(TAG, "listaRemocao: ${listaRemocao}")

        if (listaRemocao.isNotEmpty()) {
            listaAntiga?.forEach {
                Log.i(TAG, "item da lista Antiga: ${it.id}")
                if (it.id in listaRemocao) {
                    Log.i(TAG, "item ${it.id} está dentro da listaRemocao")

                    //listaNova.add(it)
                } else {
                    Log.i(TAG, "item ${it.id} _NÃO_ está dentro da listaRemocao")

                    listaNova.add(it)
                }
            }
            setLivrosComId(listaNova)
        }


    }

    fun addListaToLivrosComId(listaInput: List<LivroComID>) {
        val listaAntiga = livrosComId.value

        val listaNova = mutableListOf<LivroComID>()

        listaAntiga?.forEach {
            listaNova.add(it)
        }

        listaInput.forEach {
            listaNova.add(it)
        }

        setLivrosComId(listaNova)


    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Livros //////////////////////////////////////////////////////////////////////////////////////
    private val _livros = MutableLiveData<List<Livro>>()
    val livros: LiveData<List<Livro>> = _livros
    fun setLivros(value: List<Livro>) {
        _livros.postValue(value)
    }

    private val _livrosComId = MutableLiveData<List<LivroComID>>()
    val livrosComId: LiveData<List<LivroComID>> = _livrosComId
    fun setLivrosComId(value: List<LivroComID>) {
        _livrosComId.postValue(value)
    }

    fun livroToLivroComId(livro: Livro, id: String): LivroComID {
        return LivroComID(
            tituloLivro = livro.tituloLivro,
            autorLivro = livro.autorLivro,
            categoriaLivro = livro.categoriaLivro,
            leituraLivro = livro.leituraLivro,
            classificacaoLivro = livro.classificacaoLivro,
            id = id
        )
    }

    fun deleteLivro(id: String) {
        repository.deleteLivro(id)
    }

    private val _selectedLivroComId = MutableLiveData<LivroComID>()
    val selectedLivroComId: LiveData<LivroComID> = _selectedLivroComId
    fun setSelectedLivroComId(value: LivroComID) {
        _selectedLivroComId.postValue(value)
    }

    fun atualizaLivro(livro: Livro) {
        repository.atualizaLivro(selectedLivroComId.value?.id, livro)
    }




    fun getLivros(): List<Livro>{

        val lista = mutableListOf<Livro>()

        repository.getLivros()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    val livro = document.toObject<Livro>()
                    lista.add(livro)
                    Log.i(TAG,"document${document}")
                    Log.i(TAG,"document${livro}")
                }
            }.addOnFailureListener {exception ->

            }
        return lista
    }

    init {
        observeColecaoLivros()
    }

}