package com.ratulsarna.storieslib

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.toSize
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState

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

@OptIn(ExperimentalPagerApi::class)
@ExperimentalComposeUiApi
@Composable
fun StoryImage(
    pagerState: PagerState,
    numberOfPages: Int,
    onPressOrRelease: (Boolean) -> Unit,
    onTap: (HorizontalHalf) -> Unit,
    content: @Composable (Int) -> Unit,
) {
    var size by remember {
        mutableStateOf(value = Size.Zero)
    }
    HorizontalPager(
        count = numberOfPages,
        state = pagerState,
        modifier = Modifier
            .onSizeChanged {
                size = it.toSize()
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        val currTime = System.currentTimeMillis()
                        onPressOrRelease(true)
                        tryAwaitRelease()
                        onPressOrRelease(false)
                        if (System.currentTimeMillis() - currTime < 200) {
                            onTap(if (it.x < size.width / 2) HorizontalHalf.LEFT else HorizontalHalf.RIGHT)
                        }
                    },
                )
            },
        userScrollEnabled = false,
    ) {
        content(it)
    }
}

enum class HorizontalHalf {
    LEFT,
    RIGHT
}