package com.mertcaralan.hw1

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mertcaralan.hw1.databinding.ActivityEditorBinding

class EditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditorBinding
    private var selectedMood: String = ""
    private var selectedTime: String = ""
    private var hungerLevel: Int = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinners()
        setupSeekBar()
        setupButtons()
    }

    private fun setupSpinners() {
        // Mood Spinner
        val moods = resources.getStringArray(R.array.moods)
        val moodAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, moods)
        moodAdapter.setDropDownViewResource(R.layout.item_spinner_custom)
        binding.spinnerMood.adapter = moodAdapter

        binding.spinnerMood.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedMood = moods[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Time Spinner
        val times = resources.getStringArray(R.array.times)
        val timeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, times)
        timeAdapter.setDropDownViewResource(R.layout.item_spinner_custom)
        binding.spinnerTime.adapter = timeAdapter

        binding.spinnerTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedTime = times[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupSeekBar() {
        binding.seekBarHunger.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                hungerLevel = progress
                binding.tvHungerValue.text = hungerLevel.toString()

                // SeekBar ile ImageView kontrolü
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

        // Preview butonu - Custom Dialog
        binding.btnPreview.setOnClickListener {
            if (selectedMood.isEmpty() || selectedTime.isEmpty()) {
                Snackbar.make(binding.rootLayout, getString(R.string.snackbar_warning),
                    Snackbar.LENGTH_SHORT).show()
            } else {
                val (recommendation, emoji) = generateRecommendation()
                showCustomDialog(recommendation, emoji)
            }
        }

        // Continue butonu
        binding.btnContinue.setOnClickListener {
            if (selectedMood.isEmpty() || selectedTime.isEmpty()) {
                Snackbar.make(binding.rootLayout, getString(R.string.snackbar_warning),
                    Snackbar.LENGTH_LONG).show()
            } else {
                goToSummary()
            }
        }
    }

    private fun generateRecommendation(): Pair<String, String> {
        // ÖNERİ MANTĞI: Mood + Time + Hunger Level
        return when {
            // TIRED (Yorgun)
            (selectedMood.contains("Tired") || selectedMood.contains("Yorgun")) &&
                    (selectedTime.contains("Morning") || selectedTime.contains("Sabah")) -> {
                if (hungerLevel > 60) "Strong Coffee ☕ & Croissant 🥐" to "☕"
                else "Green Tea 🍵 & Toast 🍞" to "🍵"
            }
            (selectedMood.contains("Tired") || selectedMood.contains("Yorgun")) &&
                    (selectedTime.contains("Afternoon") || selectedTime.contains("Öğleden")) -> {
                "Energy Bar 🍫 & Orange Juice 🍊" to "🍫"
            }
            (selectedMood.contains("Tired") || selectedMood.contains("Yorgun")) -> {
                "Herbal Tea 🫖 & Honey 🍯" to "🫖"
            }

            // HAPPY (Mutlu)
            (selectedMood.contains("Happy") || selectedMood.contains("Mutlu")) &&
                    (selectedTime.contains("Morning") || selectedTime.contains("Sabah")) -> {
                "Pancakes 🥞 & Fresh Juice 🧃" to "🥞"
            }
            (selectedMood.contains("Happy") || selectedMood.contains("Mutlu")) -> {
                if (hungerLevel > 70) "Ice Cream 🍦 & Waffle 🧇" to "🍦"
                else "Cookies 🍪 & Milk 🥛" to "🍪"
            }

            // STRESSED (Stresli)
            (selectedMood.contains("Stressed") || selectedMood.contains("Stresli")) -> {
                if (hungerLevel > 60) "Dark Chocolate 🍫 & Almonds 🌰" to "🍫"
                else "Chamomile Tea 🫖 & Crackers 🍘" to "🫖"
            }

            // ENERGETIC (Enerjik)
            (selectedMood.contains("Energetic") || selectedMood.contains("Enerjik")) && hungerLevel > 70 -> {
                "Protein Shake 🥤 & Banana 🍌" to "🥤"
            }
            (selectedMood.contains("Energetic") || selectedMood.contains("Enerjik")) -> {
                "Smoothie 🍹 & Granola Bar 🍫" to "🍹"
            }

            // SAD (Üzgün)
            (selectedMood.contains("Sad") || selectedMood.contains("Üzgün")) -> {
                if (hungerLevel > 60) "Pizza 🍕 & Soda 🥤" to "🍕"
                else "Hot Chocolate ☕ & Marshmallow 🍡" to "☕"
            }

            // HUNGRY Level Based (Genel açlık seviyesi)
            hungerLevel > 80 -> "Burger 🍔 & Fries 🍟" to "🍔"
            hungerLevel < 30 -> "Water 💧 & Apple 🍎" to "🍎"

            // DEFAULT
            else -> "Sandwich 🥪 & Tea 🍵" to "🥪"
        }
    }

    private fun showCustomDialog(recommendation: String, emoji: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_preview)

        val tvEmoji = dialog.findViewById<TextView>(R.id.tvDialogEmoji)
        val tvRecommendation = dialog.findViewById<TextView>(R.id.tvDialogRecommendation)
        val tvDetails = dialog.findViewById<TextView>(R.id.tvDialogDetails)
        val btnClose = dialog.findViewById<Button>(R.id.btnDialogClose)

        tvEmoji.text = emoji
        tvRecommendation.text = recommendation
        tvDetails.text = "Mood: $selectedMood\nTime: $selectedTime\nHunger: $hungerLevel%"

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun goToSummary() {
        val (recommendation, emoji) = generateRecommendation()

        val snackData = SnackData(
            mood = selectedMood,
            timeOfDay = selectedTime,
            hungerLevel = hungerLevel,
            recommendation = recommendation,
            emoji = emoji
        )

        val intent = Intent(this, SummaryActivity::class.java)
        intent.putExtra("username", "User#${System.currentTimeMillis() % 1000}") // Primitive data
        intent.putExtra("snack_data", snackData) // Parcelable object
        startActivity(intent)
    }
}