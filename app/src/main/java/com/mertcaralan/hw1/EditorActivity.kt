package com.mertcaralan.hw1

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mertcaralan.hw1.databinding.ActivityEditorBinding

class EditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditorBinding
    private var selectedCategory: String = ""
    private var intensity: Int = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()
        setupSeekBar()
        setupButtons()
    }

    private fun setupSpinner() {
        val categories = resources.getStringArray(R.array.categories)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        // Spinner event handler
        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = categories[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupSeekBar() {
        // SeekBar event handler - ImageView'i kontrol eder
        binding.seekBarIntensity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                intensity = progress
                binding.tvIntensityValue.text = intensity.toString()

                // SeekBar ile ImageView'in alpha ve scale'ini kontrol et
                val alpha = 0.3f + (progress / 100f) * 0.7f
                val scale = 0.6f + (progress / 100f) * 0.4f

                binding.ivPreview.alpha = alpha
                binding.ivPreview.scaleX = scale
                binding.ivPreview.scaleY = scale
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupButtons() {
        // Toast butonu
        binding.btnToast.setOnClickListener {
            Toast.makeText(this, getString(R.string.toast_message), Toast.LENGTH_SHORT).show()
        }

        // Preview butonu - Custom Dialog göster
        binding.btnPreview.setOnClickListener {
            if (selectedCategory.isEmpty()) {
                Snackbar.make(binding.rootLayout, getString(R.string.snackbar_warning),
                    Snackbar.LENGTH_SHORT).show()
            } else {
                showCustomDialog()
            }
        }

        // Continue butonu - SummaryActivity'ye geç
        binding.btnContinue.setOnClickListener {
            if (selectedCategory.isEmpty()) {
                Snackbar.make(binding.rootLayout, getString(R.string.snackbar_warning),
                    Snackbar.LENGTH_LONG).show()
            } else {
                goToSummary()
            }
        }
    }

    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_preview)

        val ivPreview = dialog.findViewById<ImageView>(R.id.ivDialogPreview)
        val tvCategory = dialog.findViewById<TextView>(R.id.tvDialogCategory)
        val tvIntensity = dialog.findViewById<TextView>(R.id.tvDialogIntensity)
        val btnClose = dialog.findViewById<Button>(R.id.btnDialogClose)

        ivPreview.setImageResource(android.R.drawable.btn_star_big_on)
        tvCategory.text = "Category: $selectedCategory"
        tvIntensity.text = "Intensity: $intensity"

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun goToSummary() {
        val snackData = SnackData(
            category = selectedCategory,
            intensity = intensity
        )

        val intent = Intent(this, SummaryActivity::class.java)
        intent.putExtra("snack_data", snackData) // Parcelable object
        startActivity(intent)
    }
}