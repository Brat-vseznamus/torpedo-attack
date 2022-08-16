package elements

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@Preview
fun FixedNavigator() {
    Navigator(120f)
}

@Composable
fun Navigator(angle: Float, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .size(300.dp, 200.dp)
    ) {
        drawArc(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.Gray,
                    Color.DarkGray
                ),
                radius = 210f,
                center = Offset(150f, 125f)
            ),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true,
            size = Size(300f, 250f)
        )

        drawArc(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(54, 32, 2),
                    Color(130, 81, 14)
                ),
                radius = 150f
            ),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(20f, 15f),
            size = Size(260f, 220f)
        )

        drawCircle(
            color = Color.DarkGray,
            radius = 70f,
            center = Offset(150f, 75f)
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(80, 87, 94),
                    Color(28, 192, 230)
                ),
                center = Offset(150f, 75f)
            ),
            radius = 60f,
            center = Offset(150f, 75f),
        )

        val n = 12


        val center = Offset(150f, 75f)
        val startLine = Offset(0f, 60f)
        val endLine = Offset(0f, 45f)


        for (i in 0 until n) {

            val roter = Matrix().apply {
                rotateZ((360f / n) * i)
            }

            drawLine(
                Color.White,
                start = roter.map(startLine).plus(center),
                end =roter.map(endLine).plus(center),
                strokeWidth = 3f
            )
        }

        val pathCenter = Offset(150f, 75f)
        val l = 40f

        drawPath(
            path = Path().apply {

                val shifts = listOf(
                    Offset(-5f, 0f),
                    Offset(-5f, -l),
                    Offset(-8f, -l),
                    Offset(0f, -8f - l),
                    Offset(8f, -l),
                    Offset(5f, -l),
                    Offset(5f, 0f)
                )

                val roter = Matrix().apply {
                    rotateZ(angle)
                }

                moveTo(pathCenter.x, pathCenter.y)

                for (shift in shifts) {
                    val rotShift = roter.map(shift)
                    lineTo(pathCenter.x + rotShift.x, pathCenter.y + rotShift.y)
                }
                close()
            },
            color = Color.Red
        )


        drawCircle(
            color = Color.DarkGray,
            radius = 20f,
            center = center
        )

        drawRoundRect(
            color = Color.DarkGray,
            topLeft = Offset(225f, 85f),
            size = Size(50f, 30f),
            cornerRadius = CornerRadius(5f)
        )
    }

    Text(
        text = "${angle.toInt()}°",
        color = Color.Green,
        fontSize = 18.sp,
        modifier = modifier
            .offset(225.dp, 88.dp)
    )
}

