package com.example.android.unscramble.ui.game
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private var _score = 0
    private var _count = 0
    private var _currentScrambledWord = "test"
    //backing these property
    public val count : Int get() = _count
    public val score : Int get() = _score
    public val currentScrambledWord : String get() = _currentScrambledWord

    //creating variable for the game

    //we are making it so we can avoid repetition
    private val wordList : MutableList<String> = mutableListOf()
    private lateinit var currentWord : String

    private fun getNextWord(){
        //the random function gets you the random list element
        currentWord = allWordsList.random()
        //converting into char array and shuffling the array to generate the scrambled word
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        //keep on shuffling until you get the correct word
        while(tempWord.toString().equals(currentWord,false)){
            tempWord.shuffle()
        }
        if(wordList.contains(currentWord)){
            getNextWord()
        }
        else{
           wordList.add(currentWord)
          _currentScrambledWord = tempWord.toString()
            //it is the count of the word
          _count++
        }
    }
}