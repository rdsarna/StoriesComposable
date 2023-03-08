package com.ratulsarna.storieslib

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

/*
 * Copyright 2023 Ratul Sarna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@OptIn(ExperimentalComposeUiApi::class, ExperimentalPagerApi::class)
@Composable
fun Stories(
    numberOfPages: Int,
    modifier: Modifier = Modifier,
    indicatorModifier: Modifier = Modifier
        .padding(top = 8.dp, bottom = 8.dp)
        .clip(RoundedCornerShape(12.dp)),
    spaceBetweenIndicator: Dp = 4.dp,
    indicatorBackgroundColor: Color = Color.LightGray,
    indicatorProgressColor: Color = Color.White,
    indicatorBackgroundGradientColors: List<Color> = listOf(Color.Black, Color.Transparent),
    slideDurationInSeconds: Long = 5,
    touchToPause: Boolean = true,
    hideIndicators: Boolean = false,
    swipeAnimationOnPageTransition: Boolean = false,
    tapForNext: Boolean = true,
    tapForPrevious: Boolean = true,
    onTapForNext: (() -> Unit)? = null,
    onTapForPrevious: (() -> Unit)? = null,
    onEveryStoryChange: ((Int) -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
    content: @Composable (Int) -> Unit,
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    var pauseTimer by remember {
        mutableStateOf(false)
    }

    var currentPage by remember {
        mutableStateOf(0)
    }

    Box(
        modifier = modifier,
    ) {
        //Full screen content behind the indicator
        StoryImage(
            numberOfPages = numberOfPages,
            pagerState = pagerState,
            onPressOrRelease = { isPressed ->
                if (touchToPause) {
                    pauseTimer = isPressed
                }
            },
            onTap = {
                when {
                    it == HorizontalHalf.RIGHT && currentPage < numberOfPages && tapForNext -> {
                        coroutineScope.launch {
                            currentPage++
                            onTapForNext?.invoke()
                            onEveryStoryChange?.invoke(currentPage)
                            if (currentPage == numberOfPages) onComplete?.invoke()
                            if (swipeAnimationOnPageTransition) {
                                pagerState.animateScrollToPage(currentPage)
                            } else {
                                pagerState.scrollToPage(currentPage)
                            }
                        }
                    }
                    it == HorizontalHalf.LEFT && currentPage > 0 && tapForPrevious -> {
                        coroutineScope.launch {
                            // if we're on the last page, we need to go back 2 pages so that
                            // we do not repeat the last page
                            if (currentPage == numberOfPages) currentPage -= 2 else currentPage--
                            onTapForPrevious?.invoke()
                            onEveryStoryChange?.invoke(currentPage)
                            if (swipeAnimationOnPageTransition) {
                                pagerState.animateScrollToPage(currentPage)
                            } else {
                                pagerState.scrollToPage(currentPage)
                            }
                        }
                    }
                }
            },
            content = content,
        )

        //Indicator based on the number of items
        val indicatorsModifier =
            if (hideIndicators) {
                Modifier.fillMaxWidth()
            } else {
                Modifier
                    .fillMaxWidth()
                    .apply {
                        if (indicatorBackgroundGradientColors.isNotEmpty()) {
                            background(
                                brush = Brush.verticalGradient(indicatorBackgroundGradientColors)
                            )
                        }
                    }
            }

        Row(
            modifier = indicatorsModifier,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.padding(spaceBetweenIndicator))

            ListOfIndicators(
                numberOfPages,
                indicatorModifier,
                indicatorBackgroundColor,
                indicatorProgressColor,
                slideDurationInSeconds,
                pauseTimer,
                hideIndicators,
                spaceBetweenIndicator,
                currentPage,
            ) {
                coroutineScope.launch {
                    currentPage++
                    if (currentPage < numberOfPages) {
                        onEveryStoryChange?.invoke(currentPage)
                        if (swipeAnimationOnPageTransition) {
                            pagerState.animateScrollToPage(currentPage)
                        } else {
                            pagerState.scrollToPage(currentPage)
                        }
                    }
                    if (currentPage == numberOfPages) {
                        onComplete?.invoke()
                    }
                }
            }
        }
    }

}

@Composable
private fun RowScope.ListOfIndicators(
    numberOfPages: Int,
    @SuppressLint("ModifierParameter") indicatorModifier: Modifier,
    indicatorBackgroundColor: Color,
    indicatorProgressColor: Color,
    slideDurationInSeconds: Long,
    pauseTimer: Boolean,
    hideIndicators: Boolean,
    spaceBetweenIndicator: Dp,
    currentPage: Int,
    onAnimationEnd: () -> Unit,
) {
    for (index in 0 until numberOfPages) {
        LinearIndicator(
            modifier = indicatorModifier.weight(1f),
            inProgress = index == currentPage,
            setProgress = if (index < currentPage) 1f else 0f,
            indicatorBackgroundColor = indicatorBackgroundColor,
            indicatorProgressColor = indicatorProgressColor,
            slideDurationInSeconds = slideDurationInSeconds,
            onPauseTimer = pauseTimer,
            hideIndicators = hideIndicators,
            onAnimationEnd = onAnimationEnd,
        )

        Spacer(modifier = Modifier.padding(spaceBetweenIndicator))
    }
}