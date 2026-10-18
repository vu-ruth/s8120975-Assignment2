package com.s8120975.assignment2

/**
 * Open this file, then press Ctrl+F. That shortcut only looks in the file you have open.
 * Ctrl+Shift+F looks through the whole project. The same phrases are repeated beside the
 * real code, so a project search lands on the class that does the work.
 *
 * Question: where is the sign-in endpoint? Also called the login endpoint.
 * Answer: PhotoApi.signIn. It is a POST to footscray/auth.
 * Full sign-in URL: https://nit3213apinew.onrender.com/footscray/auth
 *
 * Question: where is the catalogue endpoint? Also called the dashboard endpoint.
 * Answer: PhotoApi.catalogue. It is a GET to dashboard/{keypass}.
 * Full catalogue URL example: https://nit3213apinew.onrender.com/dashboard/photography
 * Base URL: the HOST constant inside PhotoApi.kt, used by buildPhotoApi.
 * The keypass for this student is photography. The sign-in endpoint returns it.
 *
 * Question: where is the sign-in layout? res/layout/fragment_sign_in.xml. SignInFragment shows it.
 * Question: where is the catalogue layout? res/layout/fragment_catalogue.xml. CatalogueFragment shows it.
 * Question: where is the RecyclerView item layout? res/layout/item_technique.xml. TechniqueAdapter uses it.
 * Question: where is the detail layout? res/layout/fragment_detail.xml. DetailFragment shows it.
 * Question: where is the activity layout? res/layout/activity_host.xml. It only holds the NavHost.
 * Question: where is the navigation graph? res/navigation/nav_graph.xml.
 *
 * Question: where is the function that signs the user in? SignInViewModel.submit.
 * Question: where is the function that calls the sign-in endpoint? RemoteAuth.signIn.
 * Question: where is the function that loads the catalogue? CatalogueViewModel.refresh.
 * Question: where is the function that calls the catalogue endpoint? RemoteCatalogue.load.
 * Question: where is the function that creates a RecyclerView row? TechniqueAdapter.onCreateViewHolder.
 * Question: where is the function that fills a row? TechniqueAdapter.Holder.bind.
 * Question: where is the function that handles a row click? CatalogueFragment.open, passed into the adapter.
 * Question: where is the function that opens the detail screen? CatalogueFragment.open.
 * Question: where is the function that goes back from the detail screen? DetailFragment wires the back button to navigateUp.
 * Question: where is the function that shows a sign-in error? SignInFragment.render.
 * Question: where is the function that shows a catalogue error? CatalogueFragment.render.
 * Question: where is the function that turns an HTTP error into a sentence? guarded in CallGuard.kt.
 * Question: where is the function that strips a leading s from the student ID? IdText.asUsername.
 *
 * Question: where is the description hidden?
 * Answer: item_technique.xml and TechniqueAdapter.Holder.bind never set a description.
 * Question: where is the description shown?
 * Answer: fragment_detail.xml (description_text) and DetailFragment.render.
 *
 * RecyclerView: fragment_catalogue.xml, id technique_list. Adapter class: TechniqueAdapter.
 * ViewHolder class: TechniqueAdapter.Holder.
 * getItemCount: TechniqueAdapter.getItemCount. It returns rows.size. replace() refills that list.
 *
 * Dependency injection is Koin, not a generated graph. It starts in DarkroomApp.onCreate via startKoin.
 * The bindings live in StudioModule.studioModule.
 * ViewModels created by Koin: SignInViewModel, CatalogueViewModel, DetailViewModel.
 * Fragments ask for them with by viewModel().
 *
 * Sign-in ViewModel: SignInViewModel. Catalogue ViewModel: CatalogueViewModel. Detail ViewModel: DetailViewModel.
 * StateFlow for sign-in: SignInViewModel.state. StateFlow for the catalogue: CatalogueViewModel.state.
 * StateFlow for the detail screen: DetailViewModel.state.
 *
 * Navigation from sign-in to the catalogue: SignInFragment.render navigates to catalogueFragment and passes keypass.
 * Back from the catalogue leaves the app because that navigation pops the sign-in screen.
 * Navigation from the catalogue to the detail screen: CatalogueFragment.open passes technique, equipment,
 * subject, pioneeringPhotographer, yearIntroduced and description.
 *
 * Internet permission: AndroidManifest.xml, android.permission.INTERNET.
 * Launcher activity: HostActivity.
 *
 * Unit test for the sign-in function: SignInViewModelTest.
 * Unit test for the catalogue function: CatalogueViewModelTest.
 * Unit test for the detail screen: DetailViewModelTest.
 * Unit test for the sign-in endpoint: RemoteAuthTest.
 * Unit test for the catalogue endpoint: RemoteCatalogueTest.
 * Unit test for the student ID prefix: IdTextTest.
 */
@Suppress("unused")
internal object AskMe
