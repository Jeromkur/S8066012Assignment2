package com.vu.s8066012assignment2.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.vu.s8066012assignment2.R
import com.vu.s8066012assignment2.data.network.Animal

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View>(R.id.detailsRoot)
        ) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Reads the selected animal passed by the Dashboard.
        val species = intent.getStringExtra(EXTRA_SPECIES)
        val scientificName = intent.getStringExtra(EXTRA_SCIENTIFIC_NAME)
        val habitat = intent.getStringExtra(EXTRA_HABITAT)
        val diet = intent.getStringExtra(EXTRA_DIET)
        val status = intent.getStringExtra(EXTRA_STATUS)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)

        if (species == null || scientificName == null ||
            habitat == null || diet == null ||
            status == null || description == null ||
            !intent.hasExtra(EXTRA_LIFESPAN)
        ) {
            Toast.makeText(
                this,
                R.string.animal_details_missing,
                Toast.LENGTH_LONG
            ).show()
            finish()
            return
        }

        val lifespan = intent.getIntExtra(EXTRA_LIFESPAN, 0)

        findViewById<TextView>(R.id.detailsSpecies).text = species
        findViewById<TextView>(R.id.detailsScientificName).text =
            scientificName

        findViewById<TextView>(R.id.detailsSummary).text = getString(
            R.string.animal_summary,
            habitat,
            diet,
            status,
            lifespan
        )

        findViewById<TextView>(R.id.detailsDescription).text = description

        // Returns to the existing Dashboard.
        findViewById<MaterialButton>(R.id.backButton).setOnClickListener {
            finish()
        }
    }

    companion object {
        private const val EXTRA_SPECIES = "animal_species"
        private const val EXTRA_SCIENTIFIC_NAME = "animal_scientific_name"
        private const val EXTRA_HABITAT = "animal_habitat"
        private const val EXTRA_DIET = "animal_diet"
        private const val EXTRA_STATUS = "animal_status"
        private const val EXTRA_LIFESPAN = "animal_lifespan"
        private const val EXTRA_DESCRIPTION = "animal_description"

        // Keeps all Details navigation keys together.
        fun createIntent(context: Context, animal: Animal): Intent {
            return Intent(context, DetailsActivity::class.java).apply {
                putExtra(EXTRA_SPECIES, animal.species)
                putExtra(EXTRA_SCIENTIFIC_NAME, animal.scientificName)
                putExtra(EXTRA_HABITAT, animal.habitat)
                putExtra(EXTRA_DIET, animal.diet)
                putExtra(EXTRA_STATUS, animal.conservationStatus)
                putExtra(EXTRA_LIFESPAN, animal.averageLifespan)
                putExtra(EXTRA_DESCRIPTION, animal.description)
            }
        }
    }
}