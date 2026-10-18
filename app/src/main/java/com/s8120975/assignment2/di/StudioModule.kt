package com.s8120975.assignment2.di

import com.s8120975.assignment2.net.PhotoApi
import com.s8120975.assignment2.net.buildPhotoApi
import com.s8120975.assignment2.repo.AuthGateway
import com.s8120975.assignment2.repo.CatalogueGateway
import com.s8120975.assignment2.repo.RemoteAuth
import com.s8120975.assignment2.repo.RemoteCatalogue
import com.s8120975.assignment2.screens.catalogue.CatalogueViewModel
import com.s8120975.assignment2.screens.detail.DetailViewModel
import com.s8120975.assignment2.screens.signin.SignInViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module. Note: dependency injection, Koin, Retrofit, ViewModel.
 * DarkroomApp.onCreate loads this module with startKoin.
 * single creates one PhotoApi, one AuthGateway and one CatalogueGateway for the whole app.
 * viewModel creates a fresh SignInViewModel, CatalogueViewModel or DetailViewModel per screen.
 * SavedStateHandle is supplied by Koin when a ViewModel asks for it with get().
 */
val studioModule = module {
    single<PhotoApi> { buildPhotoApi() }
    single<AuthGateway> { RemoteAuth(get()) }
    single<CatalogueGateway> { RemoteCatalogue(get()) }

    viewModel { SignInViewModel(get(), get()) }
    viewModel { CatalogueViewModel(get(), get()) }
    viewModel { DetailViewModel(get()) }
}
