package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding

//CHALLENGES:
//1. prev button
//2. non-repeated answers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var  gradePercentage: Double = 0.0
    private val quizViewModel: QuizViewModel by viewModels()
    private var cheated: Boolean = false

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
        ) { result ->
        // Handle the result
            if(result.resultCode == Activity.RESULT_OK)
            {
                //return true if cheated and false if not cheated
                quizViewModel.setIsCheater(result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //create a binding class for activity_main.xml
        binding = ActivityMainBinding.inflate(layoutInflater)
        //pass the root view to setContentView (the top Linearlayout)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        //informs when the button has been pressed
        //binding.trueButton is a type of button
        //TRUE button
        binding.trueButton.setOnClickListener{ view: View ->
            if(quizViewModel.returnCount() ==0) {
                checkAnswer(true)
                quizViewModel.setCountNum(quizViewModel.returnCount()+1)
            }
        }
        //FALSE button
        binding.falseButton.setOnClickListener{ view: View ->
            if(quizViewModel.returnCount() ==0)
            {
                checkAnswer(false)
                quizViewModel.setCountNum(quizViewModel.returnCount()+1)
            }
        }
        //(CHALLENGE) CLICK ON TextView TO GO TO THE NEXT QUESTION
        binding.questionTextView.setOnClickListener {
            quizViewModel.setIsCheater(false)
            cheated = false
            if(quizViewModel.returnCurrentIndex() == quizViewModel.questionBankSize()-1)
            {
                binding.questionTextView.setText("Your quiz grade is: "
                        + calculateGrade()+ "%")
                quizViewModel.setIndex(-1)
                quizViewModel.setCountNum(0)
                quizViewModel.setCorrAnswer(0.0)
            }
            else
            {
                //move to the next question
                quizViewModel.moveToNext()
                quizViewModel.setCountNum(0)
                updateQuestion()
            }
        }
        //NEXT button
        binding.nextButton.setOnClickListener {
            quizViewModel.setIsCheater(false)
            cheated = false
            if(quizViewModel.returnCurrentIndex() == quizViewModel.questionBankSize()-1)
            {
                binding.questionTextView.setText("Your quiz grade is: "
                        + calculateGrade()+ "%")
                quizViewModel.setIndex(-1)
                quizViewModel.setCountNum(0)
                quizViewModel.setCorrAnswer(0.0)
            }
            else
            {
                //move to the next question
                quizViewModel.moveToNext()
                quizViewModel.setCountNum(0)
                updateQuestion()
            }
        }
        updateQuestion()
        //CHEAT button
        binding.cheatButton.setOnClickListener {
            cheated = true
            //saving the current question's answer
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            //sending MainActivity as the Context and answerIsTrue as a Boolean value
            val intent = CheatActivity.newIntent(this@MainActivity,answerIsTrue )
            //GETTING DATA BACK TO MainActivity
            cheatLauncher.launch(intent)
        }
        updateQuestion()

        // (CHALLENGE) PREVIOUS BUTTON
        binding.prevButton.setOnClickListener {
            quizViewModel.setIndex(quizViewModel.returnCurrentIndex()-1)
            prevQuestion()
        }
    }
    private fun calculateGrade(): Double
    {
        gradePercentage = (quizViewModel.getCorrAnswer() / quizViewModel.questionBankSize()) * 100.0
        return gradePercentage
    }
    private fun updateQuestion()
    {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
    }
    private fun prevQuestion()
    {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
    }
    private fun checkAnswer(userAnswer: Boolean) {
        //the correct answer of the question
        val correctAnswer = quizViewModel.currentQuestionAnswer
        //check if userAnswer matches the correct answer
        val messageResId =
            if(quizViewModel.getIsCheater()){
                R.string.judgement_toast
            }else if(userAnswer == correctAnswer){
                R.string.correct_toast
            } else {
                R.string.incorrect_toast
            }
        if(userAnswer == correctAnswer && !cheated)
        {
            quizViewModel.setCorrAnswer(quizViewModel.getCorrAnswer()+1)
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}