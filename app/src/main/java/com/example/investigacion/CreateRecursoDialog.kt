package com.example.investigacion

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class CreateRecursoDialog(context: Context, private val recurso: RecursoModel? = null, private val onSave: (RecursoModel) -> Unit) : Dialog(context) {
    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etTipo: EditText
    private lateinit var etEnlace: EditText
    private lateinit var etImagen: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button

    init {
        setContentView(R.layout.dialog_create_recurso)
        setupUI()
        recurso?.let { populateFields(it) }
    }

    private fun setupUI() {
        etTitulo = findViewById(R.id.et_titulo)
        etDescripcion = findViewById(R.id.et_descripcion)
        etTipo = findViewById(R.id.et_tipo)
        etEnlace = findViewById(R.id.et_enlace)
        etImagen = findViewById(R.id.et_imagen)
        btnGuardar = findViewById(R.id.btn_guardar)
        btnCancelar = findViewById(R.id.btn_cancelar)

        btnGuardar.setOnClickListener {
            if (validateFields()) {
                val nuevoRecurso = RecursoModel(
                    id = recurso?.id ?: 0,  // Si `recurso` no es nulo, utiliza su ID; si es nulo, usa 0
                    titulo = etTitulo.text.toString(),
                    descripcion = etDescripcion.text.toString(),
                    tipo = etTipo.text.toString(),
                    enlace = etEnlace.text.toString(),
                    imagen = etImagen.text.toString()
                )
                onSave(nuevoRecurso)
                dismiss()
            }
        }

        btnCancelar.setOnClickListener {
            dismiss()
        }
    }

    private fun populateFields(recurso: RecursoModel) {
        etTitulo.setText(recurso.titulo)
        etDescripcion.setText(recurso.descripcion)
        etTipo.setText(recurso.tipo)
        etEnlace.setText(recurso.enlace)
        etImagen.setText(recurso.imagen)
    }

    private fun validateFields(): Boolean {
        if (etTitulo.text.isEmpty()) {
            Toast.makeText(context, "Por favor ingresa un título", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
