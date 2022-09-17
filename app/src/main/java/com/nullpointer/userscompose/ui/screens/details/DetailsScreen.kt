package com.nullpointer.userscompose.ui.screens.details

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nullpointer.userscompose.R
import com.nullpointer.userscompose.core.utils.AsyncImageFade
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
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)) {
                    HeaderUserPhoto(
                        imgUser = user.imgUser,
                        modifier = Modifier.weight(.3f),
                        nameUser = user.name,
                    )
                    InfoUser(
                        user = user,
                        Modifier
                            .weight(.7f)
                            .fillMaxHeight()
                    )
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    HeaderUserPhoto(imgUser = user.imgUser, nameUser = user.name)
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
    val textDateSave = remember { user.timestamp.toFormat(context) }
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
    nameUser: String,
    modifier: Modifier = Modifier,
    colorHeader: Color = MaterialTheme.colors.primary
) {

    Box(
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth()
            .drawBehind {
                drawRect(
                    color = colorHeader,
                    size = size.copy(height = size.height / 2)
                )
            }
            .padding(15.dp)
    ) {

        AsyncImageFade(
            data = imgUser,
            resourceLoading = R.drawable.ic_person,
            resourceFailed = R.drawable.ic_broken_image,
            contentDescription = stringResource(id = R.string.description_img_user, nameUser),
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
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