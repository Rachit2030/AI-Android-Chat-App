package com.example.chatapplication.view


import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatapplication.R
import com.example.chatapplication.model.DBData
import com.example.chatapplication.ui.theme.Shapes
import com.example.chatapplication.viewmodel.ChatViewModel


@Composable
fun HistoryTab(viewModel: ChatViewModel) {
    val item = viewModel.getFromDB()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(item) { pic ->
            HistoryItem(data = pic)
        }
    }
}

@Composable
fun HistoryItem(data: DBData) {
    Card(elevation = 8.dp) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = data.req.uppercase(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(4.dp),
                textAlign = TextAlign.Center
            )
            AsyncImage(
                model = data.stored_url,
                contentDescription = "data.stored_url",
                modifier = Modifier
                    .clip(shape = Shapes.large)
                    .fillMaxSize(),
                alignment = Alignment.Center,
                placeholder = painterResource(id = R.drawable.baseline_downloading)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


