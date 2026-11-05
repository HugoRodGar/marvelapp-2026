package edu.iesam.marvelapp.features.superheroes.data

import edu.iesam.marvelapp.features.superheroes.data.local.xml.SuperHeroXmlLocalDataSource
import edu.iesam.marvelapp.features.superheroes.domain.SuperHero
import edu.iesam.marvelapp.features.superheroes.domain.SuperHeroRepository

class SuperHeroDataRepository(private val xmlLocalDataSource: SuperHeroXmlLocalDataSource) : SuperHeroRepository{

    override fun getAll(): List<SuperHero> {
        return xmlLocalDataSource.findAll()
    }

}