package com.nullpointer.userscompose.ui.screens.users.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer


@Composable
fun LoadingUsers(
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        modifier = modifier
    ) {

        items(10, key = {it}) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .size(80.dp)
                        .shimmer(),
                    shape = CircleShape
                ) {}
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier
                        .height(20.dp)
                        .width(50.dp)
                        .shimmer(),
                    shape = RoundedCornerShape(4.dp)
                ) {}
            }
        }
    }

}

