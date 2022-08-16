package elements

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import arrowMap

@Preview
@Composable
fun FixedArrow() {
    ArrowWithLine(70f, 0f, 60f,
        modifier = Modifier
            .offset(150.dp, 150.dp)
    )
}

@Composable
fun ArrowWithLine(angleX: Float, angleY: Float, angleZ: Float, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
//            .offset(395.dp, 50.dp)
            .size(10.dp, 10.dp)
            .clickable {
                println("da")
            }
    ) {

        withTransform(
            {
                transform(
//                    transformMatrix(angleX, angleY, angleZ)
                    Matrix().apply {
                        rotateX(angleX)
                        rotateY(angleY)
                        rotateZ(angleZ)
                    }
                )
            }
        ) {
            translate(
                left = 0f,
                top = -300f
            ) {
                withTransform(
                    {
                        rotate(-90f)
                        scale(0.15f)
                    }
                ) {
                    drawImage(
                        image = arrowMap,
                        alpha = 0.5f,
                        topLeft = Offset(0f, -arrowMap.height.toFloat() / 2)
                    )
//                    drawRect(
//                        color = Color.Red,
//                        alpha = 0.5f,
//                        size = Size(100f, 30f),
//                        topLeft = Offset(0f, -15f)
//                    )
                }
            }
            drawRect(
                color = Color.Green,
                size = Size(10f, 10f),
                topLeft = Offset(-5f, -5f)
            )
        }
    }
}