package com.ratulsarna.stories

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.ratulsarna.stories.ui.theme.StoriesComposeTheme
import com.ratulsarna.storieslib.Stories

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StoriesComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    Stories(
        numberOfPages = 4,
        onComplete = { Log.d("MainActivityStories", "onComplete called") },
        onEveryStoryChange = { Log.d("MainActivityStories", "onEveryStoryChange called, pageNumber=$it") },
        repeatStories = true
    ) { pageNum ->
        when (pageNum) {
            0 -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red),
                ) {
                    Text(modifier = Modifier.align(Alignment.Center), text = "Page 1")
                }
            }
            1 -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Green)

                ) {
                    Text(modifier = Modifier.align(Alignment.Center), text = "Page 2")
                }
            }
            2 -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Blue)
                ) {
                    Text(modifier = Modifier.align(Alignment.Center), text = "Page 3")
                }
            }
            3 -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Yellow)
                ) {
                    Text(modifier = Modifier.align(Alignment.Center), text = "Page 4")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StoriesComposeTheme {
        MainScreen()
    }
}