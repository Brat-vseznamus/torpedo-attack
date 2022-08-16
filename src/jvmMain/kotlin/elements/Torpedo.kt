package elements

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import rotate3d
import torpedo
import kotlin.math.cos

@Composable
fun Torpedo(distance: Float, maxDistance: Float, angleX: Float, angleY: Float, angleZ: Float, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .size(10.dp, 10.dp)
    ) {
        rotate3d(angleX, angleY, angleZ) {
            translate(
                left = 0f,
                top = -distance
            ) {
                withTransform(
                    {
                        rotate(90f)
                        scale(0.15f * (maxDistance - distance * cos(angleZ * Math.PI.toFloat() / 180f)) / maxDistance)
                    }
                ) {
                    drawImage(
                        image = torpedo,
                        topLeft = Offset(0f, -torpedo.height.toFloat() / 2)
                    )
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

fun getTorpedoOffset(
    distance: Float,
    angleX: Float,
    angleY: Float,
    angleZ: Float): Offset
{
    return Matrix().apply {
        rotateX(angleX)
        rotateY(angleY)
        rotateZ(angleZ)
    }
        .map(Offset(0f, -distance))

}

data class TorpedoItem(val angle: Float) {


    enum class TorpedoState {
        AWAITING, ACTIVATED, DESTROYED
    }

    private var torpedoState = mutableStateOf(TorpedoState.AWAITING)

    @Composable
    fun fire(
        modifier: Modifier = Modifier,
        checkDestroying: (Offset) -> ShipItem? = {null},
        shootSuccess: @Composable (ShipItem) -> Unit = {}
    ) {
        val position by animateFloatAsState(
            targetValue =
                if (torpedoState.value != TorpedoState.AWAITING) (maxDistance / cos(angle * Math.PI.toFloat() / 180f))
                else 0f,
            animationSpec = tween(1000, easing = LinearEasing),
            finishedListener = {
                println("destroyed")
                torpedoState.value = TorpedoState.DESTROYED
            }
        )
        if (torpedoState.value == TorpedoState.AWAITING) {
            torpedoState.value = TorpedoState.ACTIVATED
        }
        if (torpedoState.value == TorpedoState.ACTIVATED) {
            val index = checkDestroying(getTorpedoOffset(position, angleX, angleY, angle))
            if (index != null) {
                println("Есть пробитие!")
                torpedoState.value = TorpedoState.DESTROYED
                shootSuccess(index)
            }

            Torpedo(
                distance = position,
                maxDistance = maxDistance,
                angleX = angleX,
                angleY = angleY,
                angleZ = angle,
                modifier = modifier
            )
        }
    }


    fun isDestroyed(): Boolean {
        return torpedoState.value == TorpedoState.DESTROYED
    }

    companion object {
        const val maxDistance = 1000f
        const val angleX = 70f
        const val angleY = 0f
    }
}