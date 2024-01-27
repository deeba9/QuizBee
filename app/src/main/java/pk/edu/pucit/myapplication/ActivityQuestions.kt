package pk.edu.pucit.myapplication

import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import pk.edu.pucit.myapplication.R
import pk.edu.pucit.myapplication.ResultActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import pk.edu.pucit.myapplication.databinding.ActivityQuestionBinding

class ActivityQuestions : AppCompatActivity() {
    // Sample questions and answers
    private var currentQuestionIndex = 0
    private var score = 0
    private val questions = arrayOf(
        "Complete the analogy: Hand is to Glove as Foot is to _______.",
        "What does the acronym SQL stand for?",
        "What is the purpose of the \"git clone\" command in Git?",
        "Among the given options, which search algorithm requires less memory?",
        "Divide 30 by half and add ten.",
        "What will be the value of the following Python expression? 4 + 3 % 5",
        "Which planet is known as the Red Planet?",
        "In what year did the Titanic sink?",
        "What is the largest mammal in the world?",
        "Which database type is known for its flexibility and scalability?"
    )

    private val correctAnswers = arrayOf("Shoe", "Structured Query Language",
        "Copy a repository into a new directory", "Depth First Search", "70", "7",    "Mars",
        "1912",
        "Blue Whale","NoSQL")
    private lateinit var binding: ActivityQuestionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = intent.getStringExtra("user")
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val questionTextView: TextView = binding.tvque
        val radioGroup: RadioGroup = binding.answersgrp
        val nextButton: Button = binding.button3
        val quitButton: Button = binding.buttonquit

        // Set the initial question
        setQuestion(currentQuestionIndex)

        // Set click listeners
        nextButton.setOnClickListener {
            checkAnswer(currentQuestionIndex)
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                setQuestion(currentQuestionIndex)
            } else {
                if (user != null) {
                    showFinalScore(user)
                }
            }
        }
        quitButton.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("score", score)
            intent.putExtra("user", user)
            startActivity(intent)
            finish() // Close the activity
        }
    }

        private fun setQuestion(index: Int) {
            val questionTextView: TextView = binding.tvque
            val radioGroup: RadioGroup = binding.answersgrp

            // Set the question text
            questionTextView.text = questions[index]

            // Clear radio group selection
            radioGroup.clearCheck()

            // Set answer options
            for (i in 0 until radioGroup.childCount) {
                val radioButton = radioGroup.getChildAt(i) as RadioButton
                radioButton.text = getAnswerOption(index, i)
            }
        }

        private fun checkAnswer(index: Int) {
            val radioGroup: RadioGroup = binding.answersgrp
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId

            if (selectedRadioButtonId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                val selectedAnswer = selectedRadioButton.text.toString()

                if (selectedAnswer == correctAnswers[index]) {
                    // Correct answer
                    score++
                }
            }
        }


    private fun showFinalScore(user: String) {
        // Assuming there is a ResultActivity to navigate to
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("user", user)
        intent.putExtra("score", score)
        startActivity(intent)
        finish() // Close the current activity
    }

    private fun getAnswerOption(questionIndex: Int, optionIndex: Int): String {
        val optionsArray = getOptionsForQuestion(questionIndex)
        return if (optionIndex < optionsArray.size) {
            optionsArray[optionIndex]
        } else {
            ""
        }
    }

    private fun getOptionsForQuestion(questionIndex: Int): Array<String> {

        return when (questionIndex) {
            0 -> arrayOf("Sock", "Shoe", "Boot", "Slipper")
            1 -> arrayOf("Structured Query Language", "Simple Question Language", "Server Quality Language", "Scripted Query Language")
            2 -> arrayOf("Create a new branch","Merge branches","Copy a repository into a new directory",
                "Delete a repository")
            3 -> arrayOf("Optimal Search", "Depth First Search", "Breadth-First Search", "Linear Search")
            4 -> arrayOf("40.5", "70", "50", "I know this is trick question!")
            5 -> arrayOf("7","2","4","1")
            6 -> arrayOf("Earth", "Mars", "Venus", "Jupiter")
            7 -> arrayOf("1905", "1912", "1920", "1931")
            8 ->arrayOf("Elephant", "Giraffe", "Blue Whale", "Hippopotamus")
            9 -> arrayOf("RDBMS", "NoSQL", "SQL", "OODB")

            else -> emptyArray()
        }
    }

}