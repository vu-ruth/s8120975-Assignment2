package com.s8120975.assignment2.screens.catalogue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.s8120975.assignment2.R
import com.s8120975.assignment2.databinding.FragmentCatalogueBinding
import com.s8120975.assignment2.model.Technique
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Catalogue screen. This is the dashboard. Note: catalogue layout, dashboard layout, RecyclerView, click.
 * Layout file: res/layout/fragment_catalogue.xml.
 * The RecyclerView id is technique_list. The adapter is TechniqueAdapter.
 * A row click calls open(), which navigates to the detail screen and passes every field, including description.
 */
class CatalogueFragment : Fragment() {
    private var binding: FragmentCatalogueBinding? = null
    private val model: CatalogueViewModel by viewModel()
    private val adapter = TechniqueAdapter(::open)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = FragmentCatalogueBinding.inflate(inflater, container, false)
        binding = view
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val page = binding ?: return
        page.techniqueList.layoutManager = LinearLayoutManager(requireContext())
        page.techniqueList.adapter = adapter
        page.retryButton.setOnClickListener { model.refresh() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.state.collect { render(it) }
            }
        }
    }

    /**
     * Function that updates the catalogue layout.
     * The count uses entityTotal from the API, not the number of rows currently on screen.
     * A problem shows the error group and the retry button. Retry calls CatalogueViewModel.refresh.
     */
    private fun render(state: CatalogueState) {
        val page = binding ?: return
        adapter.replace(state.techniques)
        page.countLabel.text = countLabel(state.entityTotal)
        page.busyIndicator.isVisible = state.busy
        page.problemGroup.isVisible = state.problem != null
        page.problemText.text = state.problem
        page.emptyLabel.isVisible = !state.busy && state.problem == null && state.techniques.isEmpty()
        page.techniqueList.isVisible = state.techniques.isNotEmpty()
    }

    /**
     * Function that opens the detail screen.
     * Passes technique, equipment, subject, pioneeringPhotographer, yearIntroduced and description.
     * The destination is detailFragment in nav_graph.xml.
     */
    private fun open(row: Technique) {
        findNavController().navigate(
            R.id.detailFragment,
            bundleOf(
                "technique" to row.technique,
                "equipment" to row.equipment,
                "subject" to row.subject,
                "pioneeringPhotographer" to row.pioneeringPhotographer,
                "yearIntroduced" to row.yearIntroduced,
                "description" to row.description,
            ),
        )
    }

    override fun onDestroyView() {
        binding?.techniqueList?.adapter = null
        binding = null
        super.onDestroyView()
    }

    companion object {
        fun countLabel(total: Int): String = when (total) {
            1 -> "1 technique"
            else -> "$total techniques"
        }
    }
}
