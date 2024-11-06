@file:Suppress("UNREACHABLE_CODE")

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameType
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel

//@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun GameScreen(
    vm: GameViewModel,
    navController: NavHostController,
    gameType: GameType,
    navigateToHomeScreen: () -> Unit

) {
    vm.setGameType(gameType)
    vm.startGame()
    val gameState by vm.gameState.collectAsState()
    val number =gameState.gameType
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(16.dp))

//        for (i in 0..2) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly,
//            ) {
//
//                for (j in 0..2) {
//                    val boxValue = i * 3 + j + 1
//                    val isHighlighted = remember { mutableStateOf(false) }
//
//                    LaunchedEffect(gameState) {
//                        if (boxValue == gameState.eventValue) {
//                            isHighlighted.value = true
//                            delay(1000)
//                        isHighlighted.value = false
//                        }
//                    }
//
//                    val boxColor = if (isHighlighted.value) {
//                        Color.Green // Change to green color
//                    } else {
//                        MaterialTheme.colorScheme.primary
//                    }
//
//                    Box(
//                        modifier = Modifier
//                            .size(100.dp)
//                            .background(boxColor)
//                    )
//                }
//            }
//            Spacer(modifier = Modifier.size(16.dp))
//        }


        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
        ) {

            items((1..9).toList()) { boxValue ->
                val isHighlighted = remember { mutableStateOf(false) }

                LaunchedEffect(gameState) {
                    isHighlighted.value = boxValue == gameState.eventValue
                }

                val boxColor = if (isHighlighted.value) {
                    Color.Green // Change to green color
                } else {
                    MaterialTheme.colorScheme.primary
                }

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(boxColor)
                )
            }
        }
        Button(
            onClick = navigateToHomeScreen,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(text = "Goto Screen 1")
        }
    }
    }


@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    GameScreen(
        vm = FakeVM(),
        navController = NavHostController(LocalContext.current),
        gameType = GameType.VISUAL, // Replace with an actual GameType
        navigateToHomeScreen = {}
    )
}