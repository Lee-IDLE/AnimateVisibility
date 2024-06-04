package com.lee_idle.animatevisibility

import androidx.compose.ui.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lee_idle.animatevisibility.ui.theme.AnimateVisibilityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimateVisibilityTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    var boxVisible by remember { mutableStateOf(true) }

    val onClick = { newState : Boolean ->
        boxVisible = newState
    }

    val state = remember { MutableTransitionState(true) }

    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Crossfade(
                targetState = boxVisible,
                animationSpec = tween(5000)
            ) {visible ->
                when(visible) {
                    true -> {
                        CustomButton(text = "Hide", targetState = false, onClick = onClick, bgColor = Color.Red)
                    }
                    false -> {
                        CustomButton(text = "Show", targetState = true, onClick = onClick, bgColor = Color.Magenta)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 애니메이션 없이
        //if(boxVisible) {

        AnimatedVisibility(
            visible = boxVisible,
            //enter = fadeIn() + expandHorizontally(), // 애니메이션 합치기 가능
            // tween 함수로 애니메이션 이징을 지정해 속도를 조정할 수 있다.
            // 미리 정의된 값을 이용할 수도 있다 예) fastOutSlowInEasing, LinearOutSlowInEasing ...
            //enter = fadeIn(animationSpec = tween(durationMillis = 5000)),
            //enter = slideInHorizontally(animationSpec = tween(durationMillis = 5000, easing = LinearOutSlowInEasing)),
            //enter = slideInHorizontally(animationSpec = tween(durationMillis = 5000, easing = CubicBezierEasing(0f, 1f, 0.5f, 1f))),
            // 반복
            enter = fadeIn(animationSpec = repeatable(10,
                animation = tween(durationMillis = 2000), repeatMode = RepeatMode.Reverse)),
            exit = slideOutVertically()
        ){


            Box(
                modifier = Modifier
                    .size(height = 200.dp, width = 200.dp)
                    .background(Color.Blue)
            )
        }

        // 각각 따로 애니메이션 적용시키기
        /*
        AnimatedVisibility(
            visible = boxVisible,
            // EnterTransition를 사용하면 애니메이션 효과를 적용하지 않는다
            enter = EnterTransition.None, // fadeIn(animationSpec = tween(durationMillis = 5500))
            exit = ExitTransition.None // fadeOut(animationSpec = tween(durationMillis = 5500))
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(modifier = Modifier.size(width = 150.dp, height = 150.dp)
                    .background(Color.Blue))
            }

            Spacer(modifier = Modifier.width(20.dp))

            // 부모의 animation과 box의 animation이 합쳐졌다.
            // 사실상 (enter의 경우) fadeIn + slideInVertically인 셈이다.
            Box(
                modifier = Modifier.animateEnterExit(
                    enter = fadeIn(animationSpec = tween(durationMillis = 5500)),//slideInVertically(animationSpec = tween(durationMillis = 5500)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 5500)) //slideOutVertically(animationSpec = tween(durationMillis = 5500))
                )
                    .size(width = 150.dp, height = 150.dp)
                    .background(Color.Red),
            ){

            }
        }
         */

        // 처음 시작할 때부터 애니메이션 자동으로 시작하기
        /*
        AnimatedVisibility(
            visibleState = state,
            enter = fadeIn(animationSpec = tween(5000)),
            exit = slideOutVertically()
        ) {
            Box(modifier = Modifier
                .size(width = 150.dp, height = 150.dp)
                .background(Color.Blue))
        }
         */
    }
}

@Composable
fun CustomButton(text: String, targetState: Boolean,
                 onClick: (Boolean) -> Unit, bgColor: Color = Color.Blue) {
    Button(
        onClick = { onClick(targetState) },
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            contentColor = Color.White),
    ) {
        Text(text)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    AnimateVisibilityTheme {
        MainScreen()
    }
}