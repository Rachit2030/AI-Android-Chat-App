package com.example.chatapplication.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatapplication.R
import com.example.chatapplication.model.CurrentImageModel
import com.example.chatapplication.ui.theme.Shapes
import com.example.chatapplication.viewmodel.ChatViewModel
import com.example.chatapplication.viewmodel.ImageUIState


@Composable
fun ChatHomeScreen(
    viewModel: ChatViewModel,
    temp: CurrentImageModel,
    imageUIState: ImageUIState

) {
    val focusManager = LocalFocusManager.current

    Box {

        Box(modifier = Modifier.align(Alignment.Center)) {
            when (imageUIState) {
                is ImageUIState.Idle -> {}
                is ImageUIState.Loading -> LoadingScreen()
                is ImageUIState.Success -> ImagePromptView(data = temp)
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomStart)) {
            ChatTextField(
                value = viewModel.getCurrentPrompt(),
                onValueChange = { viewModel.updateCurrentPrompt(it) },
                onKeyboardDone = {
                    if (viewModel.getCurrentPrompt() == "") {
                        focusManager.clearFocus()
                    } else {
                        viewModel.searchCurrentPrompt()
                        focusManager.clearFocus()
                        viewModel.clearCurrentPrompt()
                    }
                },
            )
        }
    }

}



@Composable
fun ImagePromptView(data: CurrentImageModel) {

    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = data.req.uppercase(),
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            AsyncImage(
                model = data.stored_url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.95f)
                    .align(Alignment.CenterHorizontally)
                    .clip(shape = Shapes.large),
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun ChatTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onKeyboardDone: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            modifier = modifier
                .clip(Shapes.medium)
                .padding(4.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
                .fillMaxWidth(.76f),
            label = { Text(text = stringResource(id = R.string.enter_text)) },
            keyboardActions = KeyboardActions(onSearch = { onKeyboardDone() })
        )
        Button(
            onClick = { onKeyboardDone() },
            shape = Shapes.medium,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryVariant),
        ) {
            Text(
                text = stringResource(id = R.string.search),
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )

        }
    }
}
