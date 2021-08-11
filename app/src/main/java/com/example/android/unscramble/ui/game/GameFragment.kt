/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.example.android.unscramble.ui.game

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */
class GameFragment : Fragment() {

    //this is property delegation
    private val viewModel: GameViewModel by viewModels()

    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = GameFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
        // Update the UI for the initial value for currentScrambled word

        //observer fro currentScrambledWord
        viewModel.currentScrambledWord.observe(viewLifecycleOwner,{ newWord->
            binding.textViewUnscrambledWord.text = newWord
        })

        //observer for score
        viewModel.score.observe(viewLifecycleOwner,{ newScore->
            binding.score.text = getString(R.string.score,newScore)
        })
        //observer for currentWordCount
        viewModel.currentWordcount.observe(viewLifecycleOwner,{ newCount->
            binding.wordCount.text = getString(R.string.word_count,newCount,MAX_NO_OF_WORDS)
        })

    }

     //Checks the user's word, and updates the score accordingly.
     //Displays the next scrambled word.
      private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()
        if(viewModel.isUserWordCorrect(playerWord)){
            if (!viewModel.nextWord()) {
                showAlertDialog()
            }
        }
        else{
            setErrorTextField(true)
        }
    }

     //Skips the current word without changing the score.
    //Increases the word count.
    private fun onSkipWord() {
        if(viewModel.nextWord()){
            setErrorTextField(false)
        }else{
            showAlertDialog()
        }
    }

     //restart the game.
     private fun restartGame() {
        setErrorTextField(false)
        viewModel.reInitialize()
        setErrorTextField(false)
    }

    //Exits the game.
    private fun exitGame() {
        activity?.finish()
    }

    //Sets and resets the text field error status.
     private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }



    //function to create dialog
     private fun showAlertDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.congratulations)
            .setMessage(getString(R.string.you_scored,viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _,_->
                exitGame()
            }
            .setPositiveButton(R.string.play_again){ _,_->
                restartGame()
            }
            .show()
    }

}
