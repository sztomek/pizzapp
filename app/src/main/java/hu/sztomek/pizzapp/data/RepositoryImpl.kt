package hu.sztomek.pizzapp.data

import hu.sztomek.pizzapp.data.api.DatabaseApi
import hu.sztomek.pizzapp.data.api.WebApi
import hu.sztomek.pizzapp.domain.Repository

class RepositoryImpl(private val databaseApi: DatabaseApi, private val webApi: WebApi) : Repository {
}