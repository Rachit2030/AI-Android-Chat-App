package com.example.chatapplication.view


import androidx.compose.foundation.Image
import com.example.chatapplication.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapplication.ui.theme.ChatApplicationTheme
import com.example.chatapplication.viewmodel.ChatViewModel

@Composable
fun ChatApp() {
    val viewModel: ChatViewModel = viewModel()
    val temp by viewModel.CurrentResponseImageState.collectAsState()

    var isShowingHistory by remember {
        mutableStateOf(false)
    }

    var statusTitle by remember {
        mutableStateOf("CHAT APP")
    }



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                statusTitle = statusTitle,
                isShowingHistory,
                onClick = { isShowingHistory = isShowingHistory.not() }
            )
        }

    ) {

        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()


        ) {
            if (isShowingHistory) {
                statusTitle = "HISTORY"
                HistoryTab(viewModel)
            } else {
                statusTitle = "CHAT APP"
                ChatHomeScreen(viewModel, temp, viewModel.imageUIState)
            }
        }

    }
}

@Composable
fun TopAppBar(
    statusTitle: String,
    isShowingHistory: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .background(color = MaterialTheme.colors.secondaryVariant)
            .fillMaxWidth()
    ) {
        if (isShowingHistory) {
            IconButton(onClick = onClick) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_back),
                    contentDescription = "history",
                    modifier = Modifier
//                .fillMaxWidth()
                        .padding(8.dp),
                    alignment = Alignment.CenterEnd

                )
            }
        }



        Text(
            text = statusTitle,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colors.onPrimary

        )
        if (!isShowingHistory) {

            IconButton(onClick = onClick) {
                Image(
                    painter = painterResource(id = R.drawable.history_vector),
                    contentDescription = "history",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    alignment = Alignment.CenterEnd

                )
            }
        }
    }

}


@Preview
@Composable
fun DefaultAppPreview() {
    ChatApplicationTheme {
        ChatApp()
    }
}