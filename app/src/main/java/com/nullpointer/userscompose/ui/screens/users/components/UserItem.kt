package com.nullpointer.userscompose.ui.screens.users.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.nullpointer.userscompose.R
import com.nullpointer.userscompose.models.User

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserItem(
    user: User,
    isSelectedEnable: Boolean,
    modifier: Modifier = Modifier,
    changeSelectState: (User) -> Unit,
    actionClickSimple: (User) -> Unit,
) {

    val background by remember(user.isSelect) {
        derivedStateOf {
            if (user.isSelect) Color.Cyan.copy(alpha = 0.4f) else Color.Unspecified
        }
    }

    Card(
        modifier =modifier.padding(2.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .combinedClickable(
                    onClick = {
                        if (isSelectedEnable) changeSelectState(user) else actionClickSimple(user)
                    },
                    onLongClick = { if (!isSelectedEnable) changeSelectState(user) },
                )
                .drawBehind { drawRect(background) }
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageUser(
                urlImg = user.imgUser,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = user.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
private fun ImageUser(
    urlImg: String,
    modifier: Modifier = Modifier,
) {

    AsyncImage(
        model = ImageRequest
            .Builder(LocalContext.current)
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_broken_image)
            .transformations(CircleCropTransformation())
            .data(urlImg)
            .crossfade(true)
            .build(),
        contentDescription = stringResource(R.string.description_img_user),
        modifier = modifier

    )

}