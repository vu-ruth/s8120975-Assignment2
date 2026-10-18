package com.s8120975.assignment2.repo

import com.s8120975.assignment2.model.Catalogue
import com.s8120975.assignment2.model.Technique
import com.s8120975.assignment2.net.PhotoApi
import com.s8120975.assignment2.net.SignInBody
import com.s8120975.assignment2.net.TechniqueBody

/**
 * RemoteAuth calls the sign-in endpoint. Note: function that signs in, login endpoint, keypass.
 * SignInViewModel.submit calls signIn. This class calls PhotoApi.signIn.
 * The password is not trimmed, because the first name is case sensitive.
 */
class RemoteAuth(
    private val api: PhotoApi,
) : AuthGateway {
    /**
     * Function that sends the student ID and first name to the Footscray sign-in endpoint.
     * Returns the keypass. For this student that keypass is photography.
     * A blank keypass, a 400 response, or no network becomes a StudioException.
     */
    override suspend fun signIn(username: String, password: String): String = guarded(
        rejectedMessage = "That student ID or first name was not accepted. Check the capitals.",
    ) {
        val keypass = api.signIn(SignInBody(username = username, password = password)).keypass
        if (keypass.isBlank()) {
            throw StudioException("Sign-in did not return a key.")
        }
        keypass
    }
}

/**
 * RemoteCatalogue calls the catalogue endpoint. Note: dashboard endpoint, entityTotal, entities, description.
 * CatalogueViewModel.refresh calls load. This class calls PhotoApi.catalogue.
 * toTechnique copies description so the detail screen can show it. The row layout does not.
 */
class RemoteCatalogue(
    private val api: PhotoApi,
) : CatalogueGateway {
    /** Function that loads the catalogue. GET dashboard/{keypass}. Keeps entityTotal from the JSON. */
    override suspend fun load(keypass: String): Catalogue = guarded(
        rejectedMessage = "That catalogue key was not accepted. Sign in again.",
    ) {
        val body = api.catalogue(keypass)
        Catalogue(
            techniques = body.entities.map { it.toTechnique() },
            entityTotal = body.entityTotal,
        )
    }
}

private fun TechniqueBody.toTechnique() = Technique(
    technique = technique,
    equipment = equipment,
    subject = subject,
    pioneeringPhotographer = pioneeringPhotographer,
    yearIntroduced = yearIntroduced,
    description = description,
)
