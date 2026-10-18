package com.s8120975.assignment2.screens.catalogue

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.s8120975.assignment2.databinding.ItemTechniqueBinding
import com.s8120975.assignment2.model.Technique

/**
 * RecyclerView adapter. Note: RecyclerView, adapter, ViewHolder, getItemCount.
 * CatalogueFragment sets this adapter on technique_list in fragment_catalogue.xml.
 * replace() is how the list is updated. getItemCount returns rows.size.
 * The row layout is item_technique.xml. bind does not show description.
 */
class TechniqueAdapter(
    private val onOpen: (Technique) -> Unit,
) : RecyclerView.Adapter<TechniqueAdapter.Holder>() {

    private val rows = mutableListOf<Technique>()

    fun replace(next: List<Technique>) {
        rows.clear()
        rows.addAll(next)
        notifyDataSetChanged()
    }

    /** Function that tells the RecyclerView how many rows exist. */
    override fun getItemCount(): Int = rows.size

    /** Function that creates one ViewHolder and inflates the item layout. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemTechniqueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, onOpen)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(rows[position])
    }

    /**
     * ViewHolder for one technique row. Note: ViewHolder.
     * bind fills the name, year, subject, photographer and equipment.
     * It does not set the description. The click calls onOpen, which is CatalogueFragment.open.
     */
    class Holder(
        private val binding: ItemTechniqueBinding,
        private val onOpen: (Technique) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(row: Technique) {
            binding.techniqueName.text = row.technique
            binding.yearLabel.text = row.yearIntroduced.toString()
            binding.subjectLine.text = row.subject
            binding.photographerName.text = row.pioneeringPhotographer
            binding.equipmentName.text = row.equipment
            binding.root.setOnClickListener { onOpen(row) }
        }
    }
}
