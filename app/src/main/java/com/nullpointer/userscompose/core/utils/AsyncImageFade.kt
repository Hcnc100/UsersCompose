package com.nullpointer.userscompose.core.utils

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation


@Composable
fun AsyncImageFade(
    data: Any?,
    @DrawableRes
    resourceLoading: Int,
    @DrawableRes
    resourceFailed: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {

    val painterImg = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .crossfade(true)
            .data(data)
            .transformations(CircleCropTransformation())
            .build(),
        placeholder = painterResource(id = resourceLoading),
        error = painterResource(id = resourceFailed),
    )
    Image(
        painter = painterImg,
        contentDescription = contentDescription,
        colorFilter = if (painterImg.isSuccess) null else ColorFilter.tint(getGrayColor()),
        contentScale = if (painterImg.isSuccess) ContentScale.Crop else ContentScale.Fit,
        modifier = modifier
    )
}