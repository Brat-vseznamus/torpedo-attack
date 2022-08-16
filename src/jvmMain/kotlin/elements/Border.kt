package elements

import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@Preview
fun ActualBorder() {
    Border(400, 100)
}

@Composable
fun Border(length: Int, width: Int, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .size(length.dp, width.dp)
    ) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(64, 60, 57),
                    Color(117, 113, 109)
                ),
                startY = 30f,
                endY = 50f
            ),
            size = Size(
                length.toFloat(),
                width.toFloat()
            )
        )

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(115, 65, 13),
                    Color(54, 32, 2)
                ),
                startY = 0f,
                endY = 30f
            ),
            size = Size(
                length.toFloat(),
                30f
            )
        )
    }
}

@Preview
@Composable
fun TestScore() {
    Score(
        value = 200f,
        modifier = Modifier.
                offset(30.dp, 30.dp)
    )
}


@Composable
fun Score(value: Float, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .size(100.dp, 40.dp)
    ) {
        drawRoundRect(
            color = Color.Gray,
            topLeft = Offset(-10f, -10f),
            size = Size(220f, 60f),
            cornerRadius = CornerRadius(10f)
        )

        drawRect(
            color = Color.Black,
            topLeft = Offset(0f, 0f),
            size = Size(200f, 40f)
        )

        for (i in 0..3) {
            drawCircle(
                color = Color.DarkGray,
                radius = 5f,
                center = Offset((i / 2) * 200f, (i % 2) * 40f)
            )
        }
    }

    Text(
        modifier = modifier
            .offset(5.dp, 0.dp),
        text = "Score: ${value}",
        color = Color.Red,
        fontSize = 28.sp,
        fontStyle = FontStyle.Italic
    )
}


@Composable
@Preview
fun TestTimerBar() {
    TimerBar(1f)
}


@Composable
fun TimerBar(
    percent: Float,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(100.dp, 40.dp)
    ) {
        drawRoundRect(
            color = Color.Gray,
            topLeft = Offset(-10f, -10f),
            size = Size(170f, 60f),
            cornerRadius = CornerRadius(10f)
        )

        drawRoundRect(
            color = Color(67, 92, 54),
            topLeft = Offset(0f, 0f),
            size = Size(150f, 40f),
            cornerRadius = CornerRadius(10f)
        )

        drawRoundRect(
            color = Color(155, 227, 118),
            topLeft = Offset(0f, 0f),
            size = Size(150f * percent, 40f),
            cornerRadius = CornerRadius(10f),
            alpha = 0.3f
        )
    }
    if (percent == 1f) {

        val infiniteTransition = rememberInfiniteTransition()
        val infinitelyAnimatedFloat = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )


        Text(
            modifier = modifier
                .offset(40.dp, 0.dp)
                .alpha(infinitelyAnimatedFloat.value),
            text = "Okay",
            color = Color.White,
            fontSize = 28.sp
        )
    }
}