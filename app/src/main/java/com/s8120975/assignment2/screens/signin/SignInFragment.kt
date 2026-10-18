package com.s8120975.assignment2.screens.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.s8120975.assignment2.R
import com.s8120975.assignment2.databinding.FragmentSignInBinding
import com.s8120975.assignment2.screens.catalogue.CatalogueViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Sign-in screen. Note: sign-in layout, login layout, sign-in fragment.
 * Layout file: res/layout/fragment_sign_in.xml (FragmentSignInBinding).
 * The sign-in endpoint is not called here. The Sign in button calls SignInViewModel.submit.
 * by viewModel() asks Koin for the SignInViewModel.
 */
class SignInFragment : Fragment() {
    private var binding: FragmentSignInBinding? = null
    private val model: SignInViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = FragmentSignInBinding.inflate(inflater, container, false)
        binding = view
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val form = binding ?: return
        val current = model.state.value
        if (form.studentIdInput.text.isNullOrEmpty() && current.studentId.isNotEmpty()) {
            form.studentIdInput.setText(current.studentId)
        }
        if (form.firstNameInput.text.isNullOrEmpty() && current.firstName.isNotEmpty()) {
            form.firstNameInput.setText(current.firstName)
        }

        form.studentIdInput.doAfterTextChanged { model.noteStudentId(it?.toString().orEmpty()) }
        form.firstNameInput.doAfterTextChanged { model.noteFirstName(it?.toString().orEmpty()) }
        form.signInButton.setOnClickListener {
            hideKeyboard()
            model.submit()
        }
        form.firstNameInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                model.submit()
                true
            } else {
                false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.state.collect { render(it) }
            }
        }
    }

    /**
     * Function that updates the sign-in layout.
     * Shows the error sentence, the progress spinner, and disables the button while busy.
     * When keypass is set, navigates to catalogueFragment in nav_graph.xml and passes the keypass.
     * That navigation pops this screen, so Back from the catalogue leaves the app.
     */
    private fun render(state: SignInState) {
        val form = binding ?: return
        form.problemText.isVisible = state.problem != null
        form.problemText.text = state.problem
        form.busyIndicator.isVisible = state.busy
        form.signInButton.isEnabled = !state.busy
        val keypass = state.keypass ?: return
        model.keypassConsumed()
        findNavController().navigate(
            R.id.catalogueFragment,
            bundleOf(CatalogueViewModel.KEY_KEYPASS to keypass),
            NavOptions.Builder()
                .setPopUpTo(R.id.signInFragment, true)
                .build(),
        )
    }

    private fun hideKeyboard() {
        val focused = binding?.root?.findFocus() ?: return
        val imm = context?.getSystemService<InputMethodManager>() ?: return
        imm.hideSoftInputFromWindow(focused.windowToken, 0)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
