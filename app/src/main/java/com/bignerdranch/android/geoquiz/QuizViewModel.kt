package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
const val CURRENT_COUNT_KEY = "CURRENT_COUNT_KEY"
const val CORRECT_ANSWER_KEY = "CORRECT_ANSWER_KEY"

//saving the data here
class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel()
{
    //list of Question objects
    private val questionBank = listOf(
        Question(R.string.question_spain, true),
        Question(R.string.question_egypt, false),
        Question(R.string.question_canada, false),
        Question(R.string.question_ocean, true),
        Question(R.string.question_river, true)
    )
    private var isCheater: Boolean
        get() = savedStateHandle?.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    //get the current index from SavedStateHandler
    private var currentIndex: Int
            get() = savedStateHandle?.get(CURRENT_INDEX_KEY) ?: 0
            set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    //answer to the current question
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    //the current question
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    //count the number of times hit the true or false buttons
    private var count: Int
        get() =savedStateHandle?.get(CURRENT_COUNT_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_COUNT_KEY, value)
    private var correctAnswer: Double
        get() = savedStateHandle?.get(CORRECT_ANSWER_KEY) ?: 0.0
        set(value) = savedStateHandle.set(CORRECT_ANSWER_KEY, value)
    //move to the next question
    //as currentIndex is updated, the currentIndex in this class is updated and saved when configuration is changed
    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }
 //******************************************************//
 //         THIS FUNCTIONS ADDED TO HELP CHALLENGE SECTIONS
 //*****************************************************//
    fun getCorrAnswer():Double{
        return correctAnswer
    }
    fun setCorrAnswer(correctAnswer: Double){
        this.correctAnswer = correctAnswer
    }
    fun returnCount():Int{
        return count
    }
    fun setCountNum(count: Int){
        this.count = count
    }
    fun returnCurrentIndex(): Int
    {
        return currentIndex
    }
    fun questionBankSize():Int{
        return questionBank.size
    }
    fun setIndex(index: Int){
        currentIndex = index
    }
    fun setIsCheater(check: Boolean){
        isCheater = check
    }
    fun getIsCheater():Boolean{
        return isCheater
    }
}