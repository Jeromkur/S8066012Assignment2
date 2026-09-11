package com.vu.s8066012assignment2.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vu.s8066012assignment2.R
import com.vu.s8066012assignment2.data.network.Animal

class AnimalAdapter(
    private val onAnimalClick: (Animal) -> Unit
) : ListAdapter<Animal, AnimalAdapter.AnimalViewHolder>(AnimalDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_animal, parent, false)

        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AnimalViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val species =
            itemView.findViewById<TextView>(R.id.animalSpecies)

        private val scientificName =
            itemView.findViewById<TextView>(R.id.animalScientificName)

        private val summary =
            itemView.findViewById<TextView>(R.id.animalSummary)

        fun bind(animal: Animal) {
            species.text = animal.species
            scientificName.text = animal.scientificName

            // Displays every summary property, excluding description.
            summary.text = itemView.context.getString(
                R.string.animal_summary,
                animal.habitat,
                animal.diet,
                animal.conservationStatus,
                animal.averageLifespan
            )

            itemView.setOnClickListener {
                onAnimalClick(animal)
            }
        }
    }

    companion object {
        // Compares items so only changed cards need updating.
        private val AnimalDiffCallback = object : DiffUtil.ItemCallback<Animal>() {
            override fun areItemsTheSame(
                oldItem: Animal,
                newItem: Animal
            ): Boolean {
                return oldItem.scientificName == newItem.scientificName
            }

            override fun areContentsTheSame(
                oldItem: Animal,
                newItem: Animal
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}