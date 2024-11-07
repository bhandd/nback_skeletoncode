@file:Suppress("UNREACHABLE_CODE")

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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

    val gameState by vm.gameState.collectAsState()
    val number =gameState.gameType
    val score by vm.score.collectAsState()
    val isButtonEnabled by vm.gameState.collectAsState() //todo: delete this
    var isGameRunning = gameState.isGameRunning

    LaunchedEffect(isGameRunning) {
        if (!isGameRunning) {
            navigateToHomeScreen()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
        ) {

            items((1..9).toList()) { boxValue ->
                val isHighlighted = boxValue == gameState.eventValue
                val boxColor by animateColorAsState(
                    targetValue = if (isHighlighted) {
                        Color.Green // Change to green color
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    animationSpec = tween(durationMillis = 300) // Adjust the duration as needed
                )

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(boxColor)
                )

            }

        }
        Text(text="Score: $score", fontSize = 24.sp)
        //todo: delete this
//        Text(text="CurrentEventVal: "+ gameState.eventValue, fontSize = 24.sp)
//        Text(text="PreviousEventVal: "+ gameState.NBackValue, fontSize = 24.sp)
//        Spacer(modifier = Modifier.size(200.dp))

        Button(onClick = {vm.checkMatch()  },
            enabled = gameState.isButtonEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )) {
            Text(text = "Match!")
        }

        //todo: check why this doesn't work
//        Button(
//            onClick = {
//                vm.endGame()
//                navController.popBackStack()
//                //navigateToHomeScreen()
//                      },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.secondary
//            )
//        ) {
//            Text(text = "Goto Home Screen")
//        }

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