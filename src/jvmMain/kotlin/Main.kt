// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.animation.core.*
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.zIndex
import elements.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.math.*
import kotlin.random.Random
import kotlin.streams.toList


val startTime = System.nanoTime() / 1000

@Composable
@Preview
fun App(
    canonAngle: Float,
    fires: SnapshotStateList<TorpedoItem>,
    ships: SnapshotStateList<ShipItem>,
    percent: MutableState<Float>,
    score: MutableState<Float>,
    onSuccess: (Float) -> Unit
) {
    val startOffset = Offset(395f, 550f)

    var x0 by remember { mutableStateOf(0f) }
    var x1 by remember { mutableStateOf(0f) }
    var angleZ by remember { mutableStateOf(0f) }


    val positionHandler = { offset: Offset ->
        angleZ = angleBetween(startOffset, offset)
    }

    angleZ = canonAngle

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { pointerChangeInput, _ ->
                        positionHandler(pointerChangeInput.position)
                        println(pointerChangeInput.position)
                    }
                )
            }
    ) {

        val infiniteTransition = rememberInfiniteTransition()
        val infinitelyAnimatedFloat = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(14000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        var clicked by remember { mutableStateOf(false) }

        Image(
            bitmap = oceanMap,
            contentDescription = "ocean",
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.BottomCenter
        )

        val rotAngle by updateTransition(clicked).animateFloat(
            transitionSpec = {
                TweenSpec(
                    durationMillis = 1000,
                    easing = LinearEasing
                )
            }
        ) {
            if (!it) 0f else 90f
        }

        Image(
            bitmap = settings,
            contentDescription = "settings",
            modifier = Modifier
                .offset(1010.dp, 20.dp)
                .size(50.dp)
                .rotate(rotAngle)
                .clickable {
                    clicked = !clicked
                }
        )

        if (!clicked) {

            x0 = x1
            x1 = infinitelyAnimatedFloat.value

            percent.value = min(percent.value + abs(x1 - x0) * 5f, 1f)

            val fireOffset = Offset(540f, 600f)

            ships.forEach {
                it.update(abs(x1 - x0))
            }

            for (f in fires) {
                f.fire(
                    modifier = Modifier.offset(
                        fireOffset.x.toInt().dp,
                        fireOffset.y.toInt().dp
                    ),
                    checkDestroying = {
                        var res: ShipItem? = null
                        for (curShip in ships) {
                            if (curShip.shipState != ShipItem.ShipState.ALIVE) continue

                            val shipRelativeOffset = curShip.currOffset() - fireOffset + Offset(20f, 40f)
                            val diff = it - shipRelativeOffset

                            if (diff.getDistance() < 35f) {
                                res = curShip
                                break
                            }
                        }
                        res
                    }
                ) { ship ->
                    println("somehow this ship changed its state ${ship.hashCode()}")
                    ship.destroyFunction = {
                        println("destroyed by ${f.hashCode()}")
                        ship.shipState = ShipItem.ShipState.DESTROYED
                        ships.remove(ship)
                    }
                    ship.shipState = ShipItem.ShipState.FIRED
                    onSuccess(ship.score)
                }
            }

            fires.removeAll {
                it.isDestroyed()
            }

            ArrowWithLine(
                angleX = 70f,
                angleY = 0f,
                angleZ = angleZ,
                modifier = Modifier
                    .offset(540.dp, 600.dp)
            )

            Border(
                length = 1080,
                width = 300,
                modifier = Modifier
                    .offset(0.dp, 505.dp)
            )

            Navigator(
                angle = angleZ,
                modifier = Modifier
                    .offset(0.dp, 485.dp)
            )

            Score(
                modifier = Modifier
                    .offset(850.dp, 550.dp),
                value = score.value
            )

            TimerBar(
                modifier = Modifier
                    .offset(465.dp, 550.dp),
                percent = percent.value
            )
        } else {
            Box(
                modifier = Modifier
                    .offset(100.dp, 100.dp)
                    .alpha(rotAngle / 90f)
            ) {
                Canvas(
                    modifier = Modifier
                        .size(880.dp, 400.dp)
                ) {
                    drawRoundRect(
                        color = Color.Gray,
                        cornerRadius = CornerRadius(20f),
                        size = Size(880f, 400f)
                    )
                }

                Text(
                    text =
                    """
                    Добро пожаловать в игру!
                    Ваша задача сбивать корабли торпедами, которые запускаются при нажатии 
                    на SPACE, если пушка заряжена, что символизирует зеленый индикатор.
                    Чтобы направлять дуло орудия необходимо зажать левую/правую стрелку.
                    
                    
                    P.S. В игре есть баги, что, по всем канонам игры Civilisation, будет не мешать игре,
                    a только делать её веселее!
                    """.trimIndent(),
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .offset(20.dp, 20.dp)
                )
            }
        }
    }

}

fun main() = application {

    var angle by remember { mutableStateOf(0f) }
    val fires = mutableStateListOf<TorpedoItem>()
    val percent = mutableStateOf(0f)
    val score = mutableStateOf(0f)
    val ships = mutableStateListOf(ShipItem(speed = 1000f))


    LaunchedEffect(Unit) {
        while (true) {
            delay(Random.nextLong(2000) + 2000)
            if (ships.size < 5) {
                val speed = 1000f * (0.5f + 2 * Random.nextFloat())
                ships.add(
                    ShipItem(
                        speed = speed,
                        bitMap =
                            if (speed < 1000f) aircraftCarrierMap
                            else shipMap
                    )
                )
            }
        }
    }

    Window(
        title = "torpedo attack",
        onCloseRequest = ::exitApplication,
        onKeyEvent = {
            when {
                isSpaceTyped(it) -> {
                    if (percent.value == 1f) {
                        println("fire added")
                        fires.add(TorpedoItem(angle))
                        percent.value = 0f
                    }
                    true
                }
                isLeftTyped(it) -> {
                    angle -= 0.5f
                    true
                }
                isRightTyped(it) -> {
                    angle += 0.5f
                    true
                }
                else -> {
                    false
                }
            }
        },
        resizable = false,
        state = WindowState(size = windowSize)
    ) {
        App(angle, fires, ships, percent, score) { ds -> score.value += ds.toInt() }
    }
}
