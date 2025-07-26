package com.horizon.duolingostyledropcards.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.horizon.duolingostyledropcards.viewmodel.TapCardViewModel
import kotlinx.coroutines.delay

@Composable
fun CardDynamicView(viewModel: TapCardViewModel) {

    val listExercise = listOf(
            "Math",
            "with",
            "is easy",
            "Mimatika"
        )
    val stateCheck by viewModel.btnCheck.collectAsState()

    Scaffold(
        bottomBar = {
            BottomAppBar(containerColor = Color.Transparent) {
                BtnCheck(stateCheck, listOf(0, 2, 1, 3), viewModel)
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) { pad ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pad)
                .padding(horizontal = 20.dp, vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Reorder the following prayer",
                fontSize = 28.sp,
                fontWeight = FontWeight.W600,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(80.dp))
            ExerciseView(
                listExercise,
                viewModel.selectedInOrder,
                viewModel.listHideElements,
                viewModel
            )
        }
    }
}

@Composable
private fun ExerciseView(
    listCards: List<String>,
    selectedInOrder: MutableList<Int>,
    listHideElements: SnapshotStateMap<Int, Boolean>,
    tapCardVM: TapCardViewModel
) {

    //Every time we load the view, we refresh the values "selectedInOrder" & "listHideElements"
    LaunchedEffect(Unit) { tapCardVM.initCards(listCards) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        //This "FlowRow" will show the cards in the order of the val selectedInOrder
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            mainAxisSpacing = 5.dp,
            crossAxisSpacing = 12.dp
        ) {
            //We show the cards as they are added in selectedInOrder
            selectedInOrder.forEach { i ->
                CardCells(
                    listCards[i],
                    visible = true,
                    paddingCard = 15.dp
                ) {
                    //On click in the card:
                    //We change the value, with respect it's index, in the "listHideElements" to false
                    //We remove the value in the selectedInOrder
                    tapCardVM.onTopCardClicked(i)
                }
            }
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            mainAxisSpacing = 12.dp,
            crossAxisSpacing = 12.dp
        ) {
            listCards.forEachIndexed { index, s ->
                //The cards in this section are will visible if listHideElements[index] == false
                CardCells(
                    s,
                    listHideElements[index] == false,
                    15.dp
                ) {
                    //On click the card:
                    //We hide the card
                    //We added the value to selectedInOrder
                    tapCardVM.onBottomCardClicked(index)
                }
            }
        }
    }
}

@Composable
fun CardCells(
    text: String,
    visible: Boolean,
    paddingCard: Dp,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        ElevatedCard(
            onClick = onClick,
            colors = CardDefaults.cardColors(containerColor = color),
            shape = MaterialTheme.shapes.large
        ) {
            Text(
                text = text,
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.padding(paddingCard)
            )
        }
    }
}

@Composable
fun BtnCheck(check: Int, listCorrect: List<Int>,viewModel: TapCardViewModel) {
    OutlinedButton(
        onClick = { viewModel.onCheckBtnClicked(listCorrect) },
        colors = ButtonDefaults.buttonColors(
            containerColor = when(check) {
                0 -> Color.Gray
                1 -> Color.Green
                else -> Color.Red
            }
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Check ur answer", fontSize = 20.sp, fontWeight = FontWeight.W600)
    }
}