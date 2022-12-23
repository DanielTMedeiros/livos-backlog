package br.edu.infnet.livrosbacklog.repositorios

import br.edu.infnet.livrosbacklog.models.Livro
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LivrosRepository private constructor(){

    companion object {
        lateinit var auth: FirebaseAuth
        lateinit var db:FirebaseFirestore
        lateinit var colecaoLivros: CollectionReference

        private var INSTANCE: LivrosRepository? = null
        fun initialize() {
            if (INSTANCE == null) {
                INSTANCE = LivrosRepository()
            }
            auth = Firebase.auth
            db = Firebase.firestore
            colecaoLivros = db.collection("livros")
        }

        fun get(): LivrosRepository {
            return INSTANCE
                ?: throw IllegalStateException("LivrosRepository deve ser inicializado.")
        }
    }

    fun getCurrentUser() = auth.currentUser

    fun isLoggedIn(): Boolean{
        if(getCurrentUser() != null){
            return true
        }
        return false
    }

    fun cadastrarUsuarioComSenha(email: String, password: String): Task<AuthResult>{
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun login(email:String, password: String): Task<AuthResult>{
        return auth.signInWithEmailAndPassword(email, password)
    }

    fun logout(){
        auth.signOut()
    }

    fun cadastrarLivro(livro: Livro): Task<DocumentReference>{
        return colecaoLivros.add(livro)
    }

    fun getLivros(): Task<QuerySnapshot>{
        return colecaoLivros.get()
    }


    fun getLivrosColecao(): CollectionReference {
        return colecaoLivros
    }

    fun deleteLivro(id: String) {
        colecaoLivros.document(id).delete()
    }

    fun atualizaLivro(id: String?, livro: Livro) {
        if (id != null) {
            colecaoLivros.document(id).set(livro)
        }
    }


}