package mobappdev.example.nback_cimpl.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.GameApplication
import mobappdev.example.nback_cimpl.NBackHelper
import mobappdev.example.nback_cimpl.data.UserPreferencesRepository

/**
 * This is the GameViewModel.
 *
 * It is good practice to first make an interface, which acts as the blueprint
 * for your implementation. With this interface we can create fake versions
 * of the viewmodel, which we can use to test other parts of our app that depend on the VM.
 *
 * Our viewmodel itself has functions to start a game, to specify a gametype,
 * and to check if we are having a match
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */


interface GameViewModel {
    val gameState: StateFlow<GameState>
    val score: StateFlow<Int>
    val highscore: StateFlow<Int>
    val nBack: Int

    fun setGameType(gameType: GameType)
    fun startGame()

    fun checkMatch()
    fun saveHighScore(score: Int)
    fun endGame()

}

class GameVM(
    private val userPreferencesRepository: UserPreferencesRepository,
   // private val navigateToHomeScreen: () -> Unit //todo: correct?
): GameViewModel, ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    override val gameState: StateFlow<GameState>
        get() = _gameState.asStateFlow()

    private val _score = MutableStateFlow(0)
    override val score: StateFlow<Int>
        get() = _score

    private val _highscore = MutableStateFlow(0)
    override val highscore: StateFlow<Int>
        get() = _highscore

    // nBack is currently hardcoded
    override val nBack: Int = 2

    private var job: Job? = null  // coroutine job for the game event
    private val eventInterval: Long = 2000L  // 2000 ms (2s)

    private val nBackHelper = NBackHelper()  // Helper that generate the event array
    private var events = emptyArray<Int>()  // Array with all events

    override fun setGameType(gameType: GameType) {
        // update the gametype in the gamestate
        _gameState.value = _gameState.value.copy(gameType = gameType)
    }
    override fun saveHighScore(score: Int) {
        viewModelScope.launch {
            saveHighScore(userPreferencesRepository, score)
        }
    }


    override fun startGame() {
        //todo: set isGameRunning to true?
        _gameState.value.isGameRunning = true
        println("Game started")
        Log.d("GameVM", "Game started")
        job?.cancel()  // Cancel any existing game loop

        // Get the events from our C-model (returns IntArray, so we need to convert to Array<Int>)
        events = nBackHelper.generateNBackString(10, 9, 30, nBack).toList().toTypedArray()  // Todo Higher Grade: currently the size etc. are hardcoded, make these based on user input
        Log.d("GameVM", "The following sequence was generated: ${events.contentToString()}")

        job = viewModelScope.launch {
            when (gameState.value.gameType) {
                GameType.AUDIO -> runAudioGame()
                GameType.AUDIOVISUAL -> runAudioVisualGame()
                GameType.VISUAL -> runVisualGame(events)
            }
            // Todo: update the highscore(why here and not in endgame?)

        }
    }




    override fun checkMatch() {
        _gameState.value = _gameState.value.copy(isButtonEnabled = false)
Log.d("GameVM", "Checking match" + "Current Event Value: " + _gameState.value.eventValue + "NBack Value: " + _gameState.value.NBackValue)
val currentValue = _gameState.value.eventValue

        Log.d("GameScreen", "Current Value: $currentValue, NBack Value: ${gameState.value.NBackValue}")
        val nBackValue = _gameState.value.NBackValue
        if (currentValue == nBackValue) {
            _score.value += 1
        }
    }



    override fun endGame() {
        _gameState.value.isGameRunning = false
        job?.cancel()
        _highscore.value = _score.value.coerceAtLeast(_highscore.value)
        saveHighScore(_highscore.value)
        _score.value = 0
//navigateToHomeScreen()
    }

    private fun runAudioGame() {
        // Todo: Make work for Basic grade
    }

    private suspend fun runVisualGame(events: Array<Int>){
        // Todo: Check if this needs some more game logic or checks
        for ((index, value) in events.withIndex()) {
            val nBackValue = if (index >= nBack) events[index - nBack] else -1
            _gameState.value = _gameState.value.copy(eventValue = value, NBackValue = nBackValue)
         //  checkMatch()
            _gameState.value = _gameState.value.copy(isButtonEnabled = true)
            delay(eventInterval)
        }

        endGame()
    }

    private fun runAudioVisualGame(){
        // Todo: Make work for Higher grade
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GameApplication)
                GameVM(application.userPreferencesRespository)
            }
        }
    }

    init {
        // Code that runs during creation of the vm
        viewModelScope.launch {
            userPreferencesRepository.highscore.collect {
                _highscore.value = it
            }
        }
    }
}

suspend fun saveHighScore(userPreferencesRepository: UserPreferencesRepository, score: Int) {

          userPreferencesRepository.saveHighScore(score)
  }


// Class with the different game types
enum class GameType{
    AUDIO,
    VISUAL,
    AUDIOVISUAL
}

data class GameState(
    // You can use this state to push values from the VM to your UI.
    val gameType: GameType = GameType.VISUAL,  // Type of the game
    val eventValue: Int = -1,  // The value of the array string
    val NBackValue: Int = -1,  // The value of the previous event
    val isButtonEnabled: Boolean = true,  // Enable the button
    var isGameRunning: Boolean = false  // Check if the game ended

)

class FakeVM: GameViewModel{
    override val gameState: StateFlow<GameState>
        get() = MutableStateFlow(GameState()).asStateFlow()
    override val score: StateFlow<Int>
        get() = MutableStateFlow(2).asStateFlow()
    override val highscore: StateFlow<Int>
        get() = MutableStateFlow(42).asStateFlow()
    override val nBack: Int
        get() = 2

    override fun setGameType(gameType: GameType) {
    }

    override fun startGame() {
    }

    override fun checkMatch() {
    }
    override fun saveHighScore(score: Int) {
    }
    override fun endGame() {

    }

}