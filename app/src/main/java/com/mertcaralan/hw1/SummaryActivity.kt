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
        val username = intent.getStringExtra("username") ?: "Guest" // Primitive data
        val snackData = intent.getParcelableExtra<SnackData>("snack_data") // Parcelable object

        // Verileri göster
        snackData?.let {
            binding.tvEmoji.text = it.emoji
            binding.tvRecommendation.text = it.recommendation
            binding.tvDetails.text = """
                User: $username
                Mood: ${it.mood}
                Time: ${it.timeOfDay}
                Hunger Level: ${it.hungerLevel}%
            """.trimIndent()
        }

        // Confirm butonu - AlertDialog
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