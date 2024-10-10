package com.example.investigacion

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val urlBase = "https://670701c1a0e04071d228e52c.mockapi.io/"
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecursoAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        fetchAndDisplayRecursos()
    }

    private fun setupUI() {
        recyclerView = findViewById(R.id.rv_recursos)
        progressBar = findViewById(R.id.progress_bar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecursoAdapter(emptyList(), ::onEditRecurso, ::onDeleteRecurso)
        recyclerView.adapter = adapter

        val refreshButton: Button = findViewById(R.id.btn_refresh)
        refreshButton.setOnClickListener {
            fetchAndDisplayRecursos()
        }

        val addButton: Button = findViewById(R.id.btn_add_recurso)
        addButton.setOnClickListener {
            showCreateRecursoDialog()
        }
    }

    private fun fetchAndDisplayRecursos() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RecursoApiService::class.java)

        lifecycleScope.launch {
            try {
                val recursos = service.getRecursos()
                adapter.updateRecursos(recursos)
                recyclerView.visibility = View.VISIBLE
                if (recursos.isEmpty()) {
                    Toast.makeText(this@MainActivity, "No se encontraron recursos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                val message = when (e) {
                    is IOException -> "Error de red: verifica tu conexión a internet"
                    is HttpException -> "Error del servidor: ${e.code()}"
                    else -> "Error: ${e.message}"
                }
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showCreateRecursoDialog() {
        val dialog = CreateRecursoDialog(this) { nuevoRecurso ->
            crearNuevoRecurso(nuevoRecurso)
        }
        dialog.show()
    }

    private fun onEditRecurso(recurso: RecursoModel) {
        val dialog = CreateRecursoDialog(this, recurso) { updatedRecurso ->
            actualizarRecurso(updatedRecurso)
        }
        dialog.show()
    }

    private fun onDeleteRecurso(recurso: RecursoModel) {
        eliminarRecurso(recurso)
    }

    private fun crearNuevoRecurso(nuevoRecurso: RecursoModel) {
        val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RecursoApiService::class.java)

        lifecycleScope.launch {
            try {
                val recursoCreado = service.createRecurso(nuevoRecurso)
                Toast.makeText(this@MainActivity, "Recurso creado", Toast.LENGTH_SHORT).show()
                fetchAndDisplayRecursos() // Actualiza la lista de recursos
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun actualizarRecurso(recurso: RecursoModel) {
        val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RecursoApiService::class.java)

        lifecycleScope.launch {
            try {
                service.updateRecurso(recurso.id, recurso)
                Toast.makeText(this@MainActivity, "Recurso actualizado", Toast.LENGTH_SHORT).show()
                fetchAndDisplayRecursos() // Actualiza la lista de recursos
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun eliminarRecurso(recurso: RecursoModel) {
        val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RecursoApiService::class.java)

        lifecycleScope.launch {
            try {
                service.deleteRecurso(recurso.id)
                Toast.makeText(this@MainActivity, "Recurso eliminado", Toast.LENGTH_SHORT).show()
                fetchAndDisplayRecursos() // Actualiza la lista de recursos
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
