package com.example.simplebarchart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.simplebarchart.ui.theme.SimpleBarChartTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleBarChartTheme {
                BarChart(
                    data = listOf(100f, 200f, 150f, 300f, 250f),
                    labels = listOf("A", "B", "C", "D", "E"),
                    modifier = Modifier
                        .padding(16.dp)
                )

            }
        }
    }
}

@Composable
fun BarChart(data: List<Float>, labels: List<String>, modifier: Modifier = Modifier) {
    val maxData = data.maxOrNull() ?: 0f
    val animatedValues = remember { data.map { Animatable(0f) } }
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        data.forEachIndexed { index, value ->
            scope.launch {
                delay(index * 100L) // Stagger animation start times
                animatedValues[index].animateTo(
                    targetValue = value,
                    animationSpec = tween(durationMillis = 1000)
                )
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        data.forEachIndexed { index, value ->
            val animatedValue by animatedValues[index].asState()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Bar(
                    value = animatedValue,
                    maxValue = maxData,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = labels[index],
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun Bar(value: Float, maxValue: Float, color: Color) {
    val barHeight = 200.dp * (value / maxValue)
    Box(
        modifier = Modifier
            .height(200.dp)
            .width(24.dp)
            .padding(4.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Gray,
                size = size
            )
            drawRect(
                color = color,
                topLeft = androidx.compose.ui.geometry.Offset(0f, size.height - barHeight.toPx()),
                size = size.copy(height = barHeight.toPx())
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BarChartPreview() {
    SimpleBarChartTheme {
        BarChart(
            data = listOf(100f, 200f, 150f, 300f, 250f),
            labels = listOf("A", "B", "C", "D", "E"),
            modifier = Modifier
                .padding(16.dp)
                .height(200.dp)
        )
    }
}