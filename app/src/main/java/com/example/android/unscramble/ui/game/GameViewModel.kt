package com.example.android.unscramble.ui.game
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {


    private var _score = 0
    private var _currentWordcount = 0
    private  val _currentScrambledWord = MutableLiveData<String>()

    //backing these property
     val currentWordcount : Int get() = _currentWordcount
     val score : Int get() = _score
     val currentScrambledWord : LiveData<String> get() = _currentScrambledWord





    //creating variable for the game
    //we are making it so we can avoid repetition
    private var wordList:MutableList<String> = mutableListOf()
    //To store the word from the list of words in the allWordList
    private lateinit var currentWord : String

    //this will provide the initial value to the current scrambled word
    init{
        Log.d("GameFragment","ViewModel Created")

          getNextWord()
    }



    private fun getNextWord(){
        //the random function gets you the random list element
        currentWord = allWordsList.random()
        //converting into char array and shuffling the array to generate the scrambled word
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        //keep on shuffling until you get the correct word
        while(String(tempWord).equals(currentWord,false)){
            tempWord.shuffle()
        }
        if(wordList.contains(currentWord)){
            getNextWord()
        }
        else{
           wordList.add(currentWord)
          _currentScrambledWord.value = String(tempWord)
            //it is the count of the word
          _currentWordcount++
        }
    }


   //function to check whether the game is ended or not
    fun nextWord():Boolean{
        if(currentWordcount< MAX_NO_OF_WORDS){
            getNextWord()
            return true
        }
        else
            return false
    }

    //increase score function
    fun increaseScore(){
        _score+= SCORE_INCREASE
    }


    //validate the word in the edit text
    fun isUserWordCorrect(word:String):Boolean{
        if(word.equals(currentWord,false)){
            increaseScore()
            return true
        }
        else{
            return false
        }
    }

    //function to re-initialize data

    fun reInitialize(){
         _score = 0
        _currentWordcount = 0
        wordList.clear()
        getNextWord()
    }

}