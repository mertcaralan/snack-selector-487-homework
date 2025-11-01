package com.mertcaralan.hw1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mertcaralan.hw1.databinding.ActivitySummaryBinding

class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent'ten veri al
        val snackData = intent.getParcelableExtra<SnackData>("snack_data")

        // Verileri göster
        snackData?.let {
            binding.tvCategory.text = "Category: ${it.category}"
            binding.tvIntensity.text = "Intensity: ${it.intensity}"
            binding.ivSummary.setImageResource(android.R.drawable.btn_star_big_on)
        }

        // Confirm butonu - AlertDialog göster
        binding.btnConfirm.setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.summary_title))
            .setMessage(getString(R.string.alert_message))
            .setPositiveButton(getString(R.string.btn_confirm)) { dialog, _ ->
                // Geri veri döndür (Activity Result)
                val resultIntent = Intent()
                resultIntent.putExtra("result_message", getString(R.string.result_saved))
                setResult(RESULT_OK, resultIntent)
                finish()
            }
            .setNegativeButton(getString(R.string.btn_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}