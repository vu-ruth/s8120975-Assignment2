package com.s8120975.assignment2.screens.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.s8120975.assignment2.databinding.FragmentDetailBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Detail screen. Note: detail layout, description, back button.
 * Layout file: res/layout/fragment_detail.xml.
 * The back button calls navigateUp(), which returns to the catalogue.
 * render is where the description is written onto the screen.
 */
class DetailFragment : Fragment() {
    private var binding: FragmentDetailBinding? = null
    private val model: DetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = FragmentDetailBinding.inflate(inflater, container, false)
        binding = view
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.backButton?.setOnClickListener { findNavController().navigateUp() }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.state.collect { render(it) }
            }
        }
    }

    /** Function that fills the detail layout, including the description. Note: where the description is shown. */
    private fun render(state: DetailState) {
        val page = binding ?: return
        page.sheet.isVisible = !state.missing
        page.missingLabel.isVisible = state.missing
        page.techniqueName.text = state.technique
        page.yearLabel.text = state.yearLabel
        page.subjectValue.text = state.subject
        page.equipmentValue.text = state.equipment
        page.photographerValue.text = state.pioneeringPhotographer
        page.descriptionText.text = state.description
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
