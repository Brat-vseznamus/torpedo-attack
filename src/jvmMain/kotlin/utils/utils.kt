import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.key.*
import kotlin.math.PI
import kotlin.math.atan

fun isButtonTyped(keyEvent: KeyEvent, keyCode: Int): Boolean {
    return keyEvent.key.nativeKeyCode == keyCode
            && keyEvent.type == KeyEventType.KeyDown
}

fun isSpaceTyped(keyEvent: KeyEvent) = isButtonTyped(keyEvent, 32)
fun isLeftTyped(keyEvent: KeyEvent) = isButtonTyped(keyEvent, 37)
fun isRightTyped(keyEvent: KeyEvent) = isButtonTyped(keyEvent, 39)


fun DrawScope.rotate3d(angleX: Float, angleY: Float, angleZ: Float, content: DrawScope.() -> Unit) {
    withTransform(
        {
            transform(
                Matrix().apply {
                    rotateX(angleX)
                    rotateY(angleY)
                    rotateZ(angleZ)
                }
            )
        }
    ) {
        content()
    }
}


fun angleBetween(from: Offset, to: Offset): Float {
    val dx = to.x - from.x
    val dy = from.y - to.y
    return if (dx == 0f) {
        0f
    } else {
        if (dy == 0f) {
            if (dx > 0) {
                90f
            } else {
                -90f
            }
        } else {
            180 * atan(dx / dy / PI).toFloat()
        }
    }
}
