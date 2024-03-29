package com.nullpointer.userscompose.ui.screens.users.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nullpointer.userscompose.R
import com.nullpointer.userscompose.core.utils.AsyncImageFade
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

    val surfaceColor by animateColorAsState(
        if (user.isSelect) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
    )

    Surface(
        elevation = 5.dp,
        color = surfaceColor,
        shape = MaterialTheme.shapes.small,
        modifier = modifier.padding(2.dp),
    ) {
        Column(
            modifier = Modifier
                .combinedClickable(
                    onClick = {
                        if (isSelectedEnable) changeSelectState(user) else actionClickSimple(user)
                    },
                    onLongClick = { if (!isSelectedEnable) changeSelectState(user) },
                )
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImageFade(
                data = user.imgUser,
                resourceLoading = R.drawable.ic_person,
                resourceFailed = R.drawable.ic_broken_image,
                contentDescription = stringResource(id = R.string.description_img_user, user.name),
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

