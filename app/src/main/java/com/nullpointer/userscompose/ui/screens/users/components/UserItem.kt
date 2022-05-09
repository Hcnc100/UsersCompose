package com.nullpointer.userscompose.ui.screens.users.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.nullpointer.userscompose.R
import com.nullpointer.userscompose.models.User

@OptIn(ExperimentalCoilApi::class, ExperimentalFoundationApi::class)
@Composable
fun UserItem(
    user: User,
    isSelectedEnable: Boolean,
    changeSelectState: (User) -> Unit,
    actionClickSimple: (User) -> Unit,
) {

    Card(Modifier
        .padding(2.dp)
        .combinedClickable(
            onClick = { if (isSelectedEnable) changeSelectState(user) else actionClickSimple(user) },
            onLongClick = { if (!isSelectedEnable) changeSelectState(user) },
        ),
        backgroundColor = if (user.isSelect) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        shape = RoundedCornerShape(10.dp)) {
        Column(modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            ImageUser(
                urlImg = user.imgUser,
                contextDescription = stringResource(R.string.description_img_user),
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = user.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.body1)
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ImageUser(
    urlImg: String,
    contextDescription: String,
    modifier: Modifier = Modifier,
) {
    val painter = rememberImagePainter(urlImg) {
        size(OriginalSize)
        transformations(CircleCropTransformation())
        crossfade(true)
        placeholder(R.drawable.ic_person)
        error(R.drawable.ic_broken_image)
    }
    Box(contentAlignment = Alignment.Center, modifier = modifier.clip(CircleShape)) {
        Image(painter = painter,
            contentDescription = contextDescription,
            modifier = Modifier
                .fillMaxSize()
                .background(if (isSystemInDarkTheme()) Color.LightGray else Color.Gray),
            contentScale = ContentScale.Crop)
        if (painter.state is ImagePainter.State.Loading) {
            CircularProgressIndicator()
        }
    }
}