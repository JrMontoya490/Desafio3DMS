package com.example.investigacion

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecursoAdapter(
    private var recursos: List<RecursoModel>,
    private val onEditarClick: (RecursoModel) -> Unit,
    private val onEliminarClick: (RecursoModel) -> Unit
) : RecyclerView.Adapter<RecursoAdapter.RecursoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecursoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recurso, parent, false)
        return RecursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecursoViewHolder, position: Int) {
        val recurso = recursos[position]
        holder.bind(recurso)
    }

    override fun getItemCount(): Int = recursos.size

    fun updateRecursos(nuevosRecursos: List<RecursoModel>) {
        recursos = nuevosRecursos
        notifyDataSetChanged()
    }

    inner class RecursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tituloTextView: TextView = itemView.findViewById(R.id.tv_titulo)
        private val descripcionTextView: TextView = itemView.findViewById(R.id.tv_descripcion)
        private val imagenImageView: ImageView = itemView.findViewById(R.id.iv_imagen)
        private val editarButton: Button = itemView.findViewById(R.id.btn_editar)
        private val eliminarButton: Button = itemView.findViewById(R.id.btn_eliminar)
        private val verMasButton: Button = itemView.findViewById(R.id.btn_ver_mas)

        fun bind(recurso: RecursoModel) {
            tituloTextView.text = recurso.titulo
            descripcionTextView.text = recurso.descripcion

            // Cargar la imagen usando Glide
            Glide.with(itemView.context)
                .load(recurso.imagen)
                .into(imagenImageView)

            editarButton.setOnClickListener {
                onEditarClick(recurso)
            }

            eliminarButton.setOnClickListener {
                onEliminarClick(recurso)
            }

            verMasButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recurso.enlace))
                itemView.context.startActivity(intent)
            }
        }
    }
}
