package com.tcs.service.service


import com.tcs.service.model.Model
import com.tcs.service.repository.Repository

import org.springframework.stereotype.Service


@Service
class Service(private val repository: Repository) {

    fun getById(id: String): Model {
        return Model(repository.findById(id.toInt()).get() )
    }


    fun get(): MutableList<Model>{

        val models = mutableListOf<Model>()
        val result = repository.findAll()
        result.forEach { entity -> models.add(Model(data = entity)) }
        return models
    }



}