package elements

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import explosion
import kotlinx.coroutines.delay
import shipMap
import windowSize


data class ShipItem(
    val offset: Offset = initialOffset,
    val speed: Float = 1000f,
    val bitMap: ImageBitmap = shipMap
) {

    enum class ShipState {
        ALIVE, FIRED, DESTROYED
    }

    var shipState = ShipState.ALIVE
        set(value: ShipState) {
//            println("ship ${this.hashCode()} wants to change state from ${this.shipState} to $value")
            if (value != ShipState.ALIVE) {
                fired = true
            }
            field = value
        }

    val score = speed * 0.1f

    private var currPosition = 0f
    private var currDirection = 1f
    private var fired = false
    private var isAlive = true

    var destroyFunction: () -> Unit = {}

    @Composable
    fun update(
        dx: Float
    ) {
        if (shipState == ShipState.ALIVE) {
            val actualDx = speed * dx * currDirection
            if (currPosition + actualDx > yRightBorder
                || currPosition + actualDx < yLeftBorder
            ) {
                currDirection *= -1
            } else {
                currPosition += actualDx
            }
        }


        val destroyingAlpha by animateFloatAsState(
            targetValue = if (!isAlive) 0f else 1f,
            animationSpec = tween(
                durationMillis = 1000,
                delayMillis = 100,
                easing = LinearEasing
            ),
            finishedListener = {
//                println("ship destroyed ${this.hashCode()} but it is $isAlive")
//                shipState = ShipState.DESTROYED
                destroyFunction()
            }
        )

        if (shipState != ShipState.DESTROYED) {
            Box(
                modifier = Modifier
                    .offset(
                        (offset.x + currPosition).dp,
                        (offset.y).dp
                    )
                    .alpha(destroyingAlpha)
            ) {
                Image(
                    bitmap = bitMap,
                    contentDescription = "Ship",
                    modifier = Modifier
                        .size(100.dp)
                        .scale(currDirection, 1f)
                )

                val tr = updateTransition(fired)

                val alpha by tr.animateFloat(
                    transitionSpec = {
                        TweenSpec(
                            durationMillis = 100,
                            easing = LinearEasing
                        )
                    }
                ) {
                    if (!it) 0f else 1f
                }

                if (shipState != ShipState.ALIVE) {
                    if (isAlive) {
                        isAlive = false
                    }
                    Image(
                        bitmap = explosion,
                        contentDescription = "ship destroyed",
                        modifier = Modifier.size(100.dp),
                        alpha = alpha
                    )
                }
            }
        }
    }

    fun currOffset(): Offset {
        return offset + Offset(currPosition, 0f)
    }


    companion object {
        val yRightBorder = windowSize.width.value
        val yLeftBorder = -20f
        val initialOffset = Offset(0f, 250f)
    }
}
