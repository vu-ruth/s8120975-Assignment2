package com.s8120975.assignment2.repo

import com.s8120975.assignment2.model.Catalogue

/**
 * Gateway contracts. Note: sign-in, catalogue, keypass.
 * SignInViewModel depends on AuthGateway. CatalogueViewModel depends on CatalogueGateway.
 * The real classes are RemoteAuth and RemoteCatalogue. Koin binds the interfaces in StudioModule.
 */
interface AuthGateway {
    suspend fun signIn(username: String, password: String): String
}

interface CatalogueGateway {
    suspend fun load(keypass: String): Catalogue
}

class StudioException(message: String) : Exception(message)
