package com.kaliostech.iquiz

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kaliostech.iquiz.databinding.ActivityPlayBinding
import java.util.*
import java.util.concurrent.TimeUnit

class PlayActivity : AppCompatActivity() {

    private lateinit var activityPlayBinding: ActivityPlayBinding

    //    timer
    private var countDownTimer: CountDownTimer? = null
    private val countDownInMilliSecond: Long = 30000
    private val countDownInterval: Long = 1000
    private var timeLeftMilliSeconds: Long = 0
    private var defaultColor: ColorStateList? = null

    private var score = 0
    private var correct = 0
    private var wrong = 0
    private var skip = 0
    private var qIndex = 0
    private var updateQueNo = 1

    private var questions = arrayOf(
        "Q.1. What is the capital city of Australia?",
        "Q.2. Who wrote the play 'Romeo and Juliet'?",
        "Q.3. What is the chemical symbol for gold?",
        "Q.4. Which planet is known as the Red Planet?",
        "Q.5. What is the hardest natural substance on Earth?",
        "Q.6. Who painted the Mona Lisa?",
        "Q.7. Which element has the atomic number 1?",
        "Q.8. What is the largest mammal in the world?",
        "Q.9. Who developed the theory of relativity?",
        "Q.10. What is the smallest country in the world by land area?")

    private var answer = arrayOf(
        "Canberra",
        "William Shakespeare",
        "Au",
        "Mars",
        "Diamond",
        "Leonardo da Vinci",
        "Hydrogen",
        "Blue Whale",
        "Albert Einstein",
        "Vatican City")

    private var options = arrayOf(
        "Sydney",
        "Canberra",
        "Melbourne",
        "Brisbane",
        "William Wordsworth",
        "William Shakespeare",
        "Charles Dickens",
        "George Bernard Shaw",
        "Ag",
        "Au",
        "Al",
        "Ga",
        "Earth",
        "Mars",
        "Venus",
        "Jupiter",
        "Graphite",
        "Diamond",
        "Gold",
        "Silver",
        "Leonardo da Vinci",
        "Vincent van Gogh",
        "Pablo Picasso",
        "Claude Monet",
        "Hydrogen",
        "Oxygen",
        "Helium",
        "Nitrogen",
        "Elephant",
        "Blue Whale",
        "Giraffe",
        "Great White Shark",
        "Isaac Newton",
        "Albert Einstein",
        "Nikola Tesla",
        "Galileo Galilei",
        "Monaco",
        "Vatican City",
        "San Marino",
        "Liechtenstein")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityPlayBinding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(activityPlayBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(activityPlayBinding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun showNextQuestion() {

        checkAnswer()

        activityPlayBinding.apply {

            if (updateQueNo < 10) {
                tvNoOfQues.text = "${updateQueNo + 1}/10"
                updateQueNo++
            }
            if (qIndex <= questions.size - 1) {
                tvQuestion.text = questions[qIndex]
                radioButton1.text = options[qIndex * 4] // 2*4=8
                radioButton2.text = options[qIndex * 4 + 1] //  2*4+1=9
                radioButton3.text = options[qIndex * 4 + 2] //  2*4+2=10
                radioButton4.text = options[qIndex * 4 + 3] //  2*4+3=11
            } else {
                score = correct
                val intent = Intent(this@PlayActivity, ResultActivity::class.java)
                intent.putExtra("correct", correct)
                intent.putExtra("wrong", wrong)
                intent.putExtra("skip", skip)
                startActivity(intent)
                finish()
            }
            radiogrp.clearCheck()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkAnswer() {

        activityPlayBinding.apply {

            if (radiogrp.checkedRadioButtonId == -1) {
                skip++
                timeOverAlertDialog()

            } else {
                val checkRadioButton =
                    findViewById<RadioButton>(radiogrp.checkedRadioButtonId)
                val checkAnswer = checkRadioButton.text.toString()
                if (checkAnswer == answer[qIndex]) {
                    correct++
                    txtPlayScore.text = "Score : $correct"
                    correctAlertDialog()
                    countDownTimer?.cancel()
                } else {
                    wrong++
                    wrongAlertDialog()
                    countDownTimer?.cancel()
                }

            }
            qIndex++
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {

        activityPlayBinding.apply {
            tvQuestion.text = questions[qIndex]
            radioButton1.text = options[0]
            radioButton2.text = options[1]
            radioButton3.text = options[2]
            radioButton4.text = options[3]

            // check options selected or not
            // if selected then selected option correct or wrong
            nextQuestionBtn.setOnClickListener {
                if (radiogrp.checkedRadioButtonId == -1) {
                    Toast.makeText(this@PlayActivity,
                        "Please select an options",
                        Toast.LENGTH_SHORT)
                        .show()
                } else {
                    showNextQuestion()
                }
            }

            tvNoOfQues.text = "$updateQueNo/10"
            tvQuestion.text = questions[qIndex]

            defaultColor = quizTimer.textColors


            timeLeftMilliSeconds = countDownInMilliSecond

            statCountDownTimer()
        }
    }

    private fun statCountDownTimer() {
        countDownTimer = object : CountDownTimer(timeLeftMilliSeconds, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {

                activityPlayBinding.apply {

                    timeLeftMilliSeconds = millisUntilFinished
                    val second = TimeUnit.MILLISECONDS.toSeconds(timeLeftMilliSeconds).toInt()

                    // %02d format the integer with 2 digit
                    val timer = String.format(Locale.getDefault(), "Time: %02d", second)
                    quizTimer.text = timer

                    if (timeLeftMilliSeconds < 10000) {
                        quizTimer.setTextColor(Color.RED)
                    } else {
                        quizTimer.setTextColor(defaultColor)
                    }
                }
            }

            override fun onFinish() {
                showNextQuestion()

            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun correctAlertDialog() {
        val builder = AlertDialog.Builder(this@PlayActivity)
        val view = LayoutInflater.from(this@PlayActivity).inflate(R.layout.layout_correct_answers, null)
        builder.setView(view)
        val tvScore = view.findViewById<TextView>(R.id.tvDialog_score)
        val correctOkBtn = view.findViewById<Button>(R.id.correct_ok)
        tvScore.text = "Score : $correct"
        val alertDialog = builder.create()
        correctOkBtn.setOnClickListener {
            timeLeftMilliSeconds = countDownInMilliSecond
            statCountDownTimer()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun wrongAlertDialog() {
        val builder = AlertDialog.Builder(this@PlayActivity)
        val view = LayoutInflater.from(this@PlayActivity).inflate(R.layout.layout_wrong_answers, null)
        builder.setView(view)

        val tvWrongDialogCorrectAns = view.findViewById<TextView>(R.id.tv_wrongDialog_correctAns)
        val wrongOk = view.findViewById<Button>(R.id.wrong_ok)
        tvWrongDialogCorrectAns.text = "Correct Answer : " + answer[qIndex]
        val alertDialog = builder.create()

        wrongOk.setOnClickListener {
            timeLeftMilliSeconds =
                countDownInMilliSecond
            statCountDownTimer()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun timeOverAlertDialog() {
        val builder = AlertDialog.Builder(this@PlayActivity)
        val view = LayoutInflater.from(this@PlayActivity).inflate(R.layout.layout_time_over, null)
        builder.setView(view)
        val timeOverOk = view.findViewById<Button>(R.id.timeOver_ok)
        val alertDialog = builder.create()
        timeOverOk.setOnClickListener {
            timeLeftMilliSeconds = countDownInMilliSecond
            statCountDownTimer()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}