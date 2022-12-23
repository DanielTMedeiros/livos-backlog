package br.edu.infnet.livrosbacklog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.edu.infnet.livrosbacklog.databinding.LivroItemBinding
import br.edu.infnet.livrosbacklog.models.LivroComID

class LivroComIdAdapter(val listener: LivroComIdListener):
    ListAdapter<
            LivroComID,
            LivroComIdAdapter.ViewHolder
            >(LivroComIdDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent, listener)
    }

    class ViewHolder private constructor(
        val binding: LivroItemBinding,
        val listener: LivroComIdListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LivroComID, position: Int) {
            binding.apply {
                tvTituloLivro.text = item.tituloLivro
                tvAutorLivro.text = item.autorLivro
                tvCategoriaLivro.text = item.categoriaLivro
                cbLeituraLivro.isChecked = item.leituraLivro
                binding.rbClassificacaoLivro.rating = item.classificacaoLivro.toFloat()


                ivEdit.setOnClickListener {
                    listener.onEditClick(item)
                }
                ivDelete.setOnClickListener {
                    listener.onDeleteClick(item)
                }

            }
        }

        companion object {
            fun from(parent: ViewGroup, listener: LivroComIdListener): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LivroItemBinding.inflate(
                    layoutInflater, parent, false
                )
                return ViewHolder(binding, listener)
            }
        }
    }

    class LivroComIdDiffCallback : DiffUtil.ItemCallback<LivroComID>() {

        override fun areItemsTheSame(oldItem: LivroComID, newItem: LivroComID): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LivroComID, newItem: LivroComID): Boolean {
            return oldItem == newItem
        }
    }

}

interface LivroComIdListener {
    fun onEditClick(livro: LivroComID)
    fun onDeleteClick(livro: LivroComID)
}