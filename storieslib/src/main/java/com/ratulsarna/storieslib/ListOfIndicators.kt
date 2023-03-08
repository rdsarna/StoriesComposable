package com.ratulsarna.storieslib

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

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

@Composable
fun RowScope.ListOfIndicators(
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