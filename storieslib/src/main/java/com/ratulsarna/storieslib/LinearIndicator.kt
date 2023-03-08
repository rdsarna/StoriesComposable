package com.ratulsarna.storieslib

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
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

@Composable
fun LinearIndicator(
    modifier: Modifier,
    inProgress: Boolean = false,
    setProgress: Float = 0f,
    indicatorBackgroundColor: Color,
    indicatorProgressColor: Color,
    slideDurationInSeconds: Long,
    onPauseTimer: Boolean = false,
    hideIndicators: Boolean = false,
    onAnimationEnd: () -> Unit,
) {
    val delayInMillis = rememberSaveable { (slideDurationInSeconds * 1000) / 100 }
    val progress = remember { Animatable(0f) }

    if (inProgress) {
        LaunchedEffect(key1 = onPauseTimer) {
            if (progress.value == 1f) {
                progress.snapTo(0f)
            }
            while (progress.value < 1f && isActive && onPauseTimer.not()) {
                progress.animateTo(progress.value + 0.01f)
                delay(delayInMillis)
            }

            //When the timer is not paused and animation completes then move to next page.
            if (onPauseTimer.not()) {
                delay(200)
                onAnimationEnd()
            }
        }
    } else if (progress.value != setProgress) {
        LaunchedEffect(key1 = setProgress) {
            progress.animateTo(setProgress)
        }
    }

    if (hideIndicators.not()) {
        LinearProgressIndicator(
            backgroundColor = indicatorBackgroundColor,
            color = indicatorProgressColor,
            modifier = modifier
                .padding(top = 12.dp, bottom = 12.dp)
                .clip(RoundedCornerShape(12.dp)),
            progress = if (inProgress) progress.value else setProgress,
        )
    }
}