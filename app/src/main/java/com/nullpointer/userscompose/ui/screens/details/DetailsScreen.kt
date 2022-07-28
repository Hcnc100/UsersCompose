package com.nullpointer.userscompose.ui.screens.details

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.nullpointer.userscompose.R
import com.nullpointer.userscompose.core.utils.shareViewModel
import com.nullpointer.userscompose.core.utils.toFormat
import com.nullpointer.userscompose.models.User
import com.nullpointer.userscompose.presentation.UsersViewModel
import com.nullpointer.userscompose.ui.share.ToolbarBack
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun DetailsScreen(
    user: User,
    usersViewModel: UsersViewModel = shareViewModel(),
    navigator: DestinationsNavigator,
) {
    Scaffold(
        floatingActionButton = {
            ButtonDeleterUser {
                usersViewModel.deleterUser(user)
                navigator.popBackStack()
            }
        },
        topBar = {
            ToolbarBack(
                title = stringResource(R.string.title_details_user),
                actionBack = navigator::popBackStack
            )
        }
    ) {
        when ( LocalConfiguration.current.orientation) {

            Configuration.ORIENTATION_LANDSCAPE -> {
                Row(modifier = Modifier.fillMaxWidth().padding(it)) {
                    HeaderUserPhoto(imgUser = user.imgUser, modifier = Modifier.weight(.3f))
                    InfoUser(user = user, Modifier.weight(.7f).fillMaxHeight())
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()).padding(it)
                ) {
                    HeaderUserPhoto(imgUser = user.imgUser)
                    Spacer(modifier = Modifier.height(20.dp))
                    InfoUser(user = user)
                }
            }

        }
    }
}

@Composable
private fun InfoUser(
    user: User,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current
) {
    val textDateSave by derivedStateOf { user.timestamp.toFormat(context) }
    Card(modifier = modifier.padding(5.dp)) {
        Column(modifier = Modifier.padding(10.dp)) {
            RowInfo(nameField = stringResource(R.string.name_user_text), dataField = user.name)
            RowInfo(
                nameField = stringResource(R.string.last_name_user_text),
                dataField = user.lastName
            )
            RowInfo(nameField = stringResource(R.string.city_user_text), dataField = user.city)
            RowInfo(nameField = stringResource(R.string.date_user_text), dataField = textDateSave)
        }
    }
}

@Composable
private fun HeaderUserPhoto(
    imgUser: String,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .height(200.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.5f)
                .background(MaterialTheme.colors.primary)
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imgUser)
                .placeholder(R.drawable.ic_person)
                .transformations(CircleCropTransformation())
                .error(R.drawable.ic_broken_image)
                .build(),
            contentDescription = stringResource(id = R.string.description_img_user),
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.Center)
        )
    }

}

@Composable
private fun ButtonDeleterUser(
    actionDeleter: () -> Unit
) {
    FloatingActionButton(onClick = actionDeleter) {
        Icon(
            painter = rememberVectorPainter(image = Icons.Default.Delete),
            contentDescription = stringResource(R.string.description_deleter_user_button)
        )
    }
}

@Composable
private fun RowInfo(
    nameField: String,
    dataField: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Text(
            text = nameField,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.W400
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = dataField,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.W300
        )
    }
}