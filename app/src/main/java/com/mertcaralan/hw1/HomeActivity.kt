package com.mertcaralan.hw1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mertcaralan.hw1.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)))
    }

    private val editorLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val message = result.data?.getStringExtra("result_message")
                ?: getString(R.string.result_saved)
            Snackbar.make(binding.mainLayout, message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val blinkAnim = AnimationUtils.loadAnimation(this, R.anim.blink)
        binding.tvTitle.startAnimation(blinkAnim)

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, EditorActivity::class.java)
            editorLauncher.launch(intent)
        }

        binding.btnLanguage.setOnClickListener {
            changeLanguage()
        }
    }

    private fun changeLanguage() {
        val currentLang = LocaleHelper.getLanguage(this)
        val newLang = if (currentLang == "tr") "en" else "tr"
        LocaleHelper.setLocale(this, newLang)
        Toast.makeText(this, getString(R.string.toast_language_changed), Toast.LENGTH_SHORT).show()
        recreate()
    }
}