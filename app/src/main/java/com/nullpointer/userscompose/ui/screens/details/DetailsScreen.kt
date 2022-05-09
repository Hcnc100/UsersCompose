package com.nullpointer.userscompose.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nullpointer.userscompose.R
import com.nullpointer.userscompose.core.utils.toFormat
import com.nullpointer.userscompose.models.User
import com.nullpointer.userscompose.presentation.UsersViewModel
import com.nullpointer.userscompose.ui.screens.users.components.ImageUser
import com.nullpointer.userscompose.ui.share.ToolbarBack
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun DetailsScreen(
    user: User,
    usersViewModel: UsersViewModel,
    navigator: DestinationsNavigator,
) {
    val context = LocalContext.current
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                usersViewModel.deleterUser(user)
                navigator.popBackStack()
            }) {
                Icon(painter = rememberVectorPainter(image = Icons.Default.Delete),
                    contentDescription = "Elimina el usuario actual")
            }
        },
        topBar = {
            ToolbarBack(
                title = stringResource(R.string.title_details_user),
                actionBack = navigator::popBackStack)
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f)
                    .background(MaterialTheme.colors.primary))
                ImageUser(
                    urlImg = user.imgUser,
                    contextDescription = stringResource(id = R.string.description_img_user),
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Card(modifier = Modifier.padding(5.dp)) {
                Column(modifier = Modifier.padding(10.dp)) {
                    RowInfo(nameField = "Nombre:", dataField = user.name)
                    RowInfo(nameField = "Apellido:", dataField = user.lastName)
                    RowInfo(nameField = "City:", dataField = user.city)
                    RowInfo(nameField = "Fecha de guardado:",
                        dataField = user.timestamp.toFormat(context))
                }
            }
        }
    }
}

@Composable
fun RowInfo(
    nameField: String,
    dataField: String,
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 5.dp)) {
        Text(text = nameField,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.W400)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = dataField,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.W300)
    }
}