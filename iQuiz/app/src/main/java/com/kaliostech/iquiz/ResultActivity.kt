package com.kaliostech.iquiz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kaliostech.iquiz.databinding.ActivityResultBinding


class ResultActivity : AppCompatActivity() {

    private lateinit var activityResultBinding: ActivityResultBinding

    var totalScore = 0
    var correct = 0
    var wrong = 0
    var skip = 0
    var isKey = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityResultBinding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(activityResultBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(activityResultBinding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        totalScore = intent.extras!!.getInt("correct")
        wrong = intent.extras!!.getInt("wrong")
        skip = intent.extras!!.getInt("skip")

        initializeViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initializeViews() {

        activityResultBinding.apply {

            tvScore.text = "Score: $totalScore"
            tvright.text = "Correct: $totalScore"
            tvwrong.text = "Wrong: $wrong"
            tvSkip.text = "Skip: $skip"

            if (totalScore >= 6) {
                activityResultBinding.emojiReactionImg.setImageResource(R.drawable.high_marks)
                Toast.makeText(this@ResultActivity, "Congratulations!", Toast.LENGTH_SHORT).show()

            } else {
                emojiReactionImg.setImageResource(R.drawable.low_marks)
                Toast.makeText(this@ResultActivity, "Better Luck Next Time", Toast.LENGTH_SHORT).show()
            }

            tvPlayAgain.setOnClickListener {
                val intent = Intent(this@ResultActivity, PlayActivity::class.java)
                startActivity(intent)
            }

        }
    }
}