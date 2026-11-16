package edu.iesam.marvelapp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.iesam.marvelapp.core.cache.AppCacheTTL
import edu.iesam.marvelapp.core.serialization.GsonJsonSerializer
import edu.iesam.marvelapp.core.serialization.KotlinJsonSerializer
import edu.iesam.marvelapp.core.time.LocalAppTime
import edu.iesam.marvelapp.features.superheroes.data.SuperHeroDataRepository
import edu.iesam.marvelapp.features.superheroes.data.local.xml.SuperHeroXmlLocalDataSource
import edu.iesam.marvelapp.features.superheroes.domain.GetSuperHeroesUseCase
import edu.iesam.marvelapp.features.superheroes.domain.SuperHero
import edu.iesam.marvelapp.features.superheroes.presentation.SuperHeroListViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: SuperHeroListViewModel by viewModels{
        SuperHeroListViewModel.factory(
            GetSuperHeroesUseCase(
                SuperHeroDataRepository(
                    SuperHeroXmlLocalDataSource(
                        this,
                        GsonJsonSerializer.create(),
                        AppCacheTTL(this, LocalAppTime())
                    )
                )
            )
        )

    }

    //Ciclo de vida: Paso 1 OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.d("MainActivity", "OnCreate")
        initObserver()
        viewModel.loadSuperHeroes()
    }

    private fun initObserver() {
        viewModel.uiState.observe(this) { uiState ->
            Log.d("MainActivity", uiState.isLoading.toString())
            bind(uiState)
        }
    }

    //Se recoge la información del viewModel
    private fun bind(uiState: SuperHeroListViewModel.UiState) {

        if (uiState.isLoading) {
            //show loading
        } else {
            //hide loading
        }

        if (uiState.hasError){
            //show error voew
        } else {
            //hide error view
        }

        uiState.superHeroes?.let { superHeroes ->
            if (superHeroes.isEmpty()) {
                //muestro pantalla de listado vacio
            } else {
                renderUi(superHeroes.first())
            }
        }

    }

    private fun renderUi(superHero: SuperHero) {

        val superHeroLayout = findViewById<ConstraintLayout>(R.id.superhero_layout)

        val labelNameSuperHero = superHeroLayout.findViewById<TextView>(R.id.superhero_name)
        labelNameSuperHero.text = superHero.name

        val labelSlugSuperHero = superHeroLayout.findViewById<TextView>(R.id.superhero_slug)
        labelSlugSuperHero.text = superHero.slug
    }

}