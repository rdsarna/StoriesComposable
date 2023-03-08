package com.ratulsarna.storieslib

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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

/**
 * @param numberOfPages Number of pages in the story
 * @param modifier Modifier for the whole story
 * @param indicatorModifier Modifier for the indicator
 * @param spaceBetweenIndicator Space between the indicators
 * @param indicatorBackgroundColor Background color of the indicator
 * @param indicatorProgressColor Progress color of the indicator
 * @param indicatorBackgroundGradientColors Background gradient colors of the indicator
 * @param slideDurationInSeconds Duration of each slide in seconds
 * @param touchToPause Pause the story when user touches the screen
 * @param hideIndicators Hide the indicators
 * @param repeatStories Loop or loop back the stories after the last one or the first one
 * @param swipeAnimationOnPageTransition Enable or disable the swipe animation on page transition
 * @param tapForNext Enable or disable the tap for next
 * @param tapForPrevious Enable or disable the tap for previous
 * @param onTapForNext Callback for tap for next
 * @param onTapForPrevious Callback for tap for previous
 * @param onEveryStoryChange Callback for every story change
 * @param onComplete Callback for when all the stories are completed
 * @param content Content of the story at the given index
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
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
    repeatStories: Boolean = true,
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
                    it == HorizontalHalf.RIGHT && tapForNext -> {
                        coroutineScope.launch {
                            if (repeatStories.not() && currentPage == numberOfPages) {
                                return@launch
                            }
                            currentPage = if (repeatStories) {
                                (currentPage + 1) % numberOfPages
                            } else {
                                (currentPage + 1)
                            }
                            if (currentPage < numberOfPages) {
                                onTapForNext?.invoke()
                                onEveryStoryChange?.invoke(currentPage)
                                if (swipeAnimationOnPageTransition) {
                                    pagerState.animateScrollToPage(currentPage)
                                } else {
                                    pagerState.scrollToPage(currentPage)
                                }
                            }
                            if (currentPage == numberOfPages) onComplete?.invoke()
                        }
                    }
                    it == HorizontalHalf.LEFT && tapForPrevious -> {
                        coroutineScope.launch {
                            if (repeatStories.not() && currentPage == 0) {
                                return@launch
                            }
                            currentPage = if (repeatStories) {
                                if (currentPage == 0) numberOfPages - 1 else currentPage - 1
                            } else {
                                if (currentPage == numberOfPages) currentPage - 2 else currentPage - 1
                            }
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