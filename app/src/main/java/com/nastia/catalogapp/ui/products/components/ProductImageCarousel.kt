package com.nastia.catalogapp.ui.products.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun ProductImageCarousel(images: List<String>) {
    if (images.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { images.size })

    HorizontalPager(state = pagerState) { page ->
        AsyncImage(
            model = images[page],
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Fit
        )
    }
}