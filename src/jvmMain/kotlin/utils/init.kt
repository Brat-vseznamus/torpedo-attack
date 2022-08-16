import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import java.nio.file.Files
import java.nio.file.Path

val windowSize = DpSize(1080.dp, 645.dp)


val seaMap = loadImageBitmap(Files.newInputStream(Path.of("./src/jvmMain/resources/sea.png")))

val shipMap = loadImageBitmap(Files.newInputStream(Path.of("./src/jvmMain/resources/ship.png")))
val boatMap = loadImageBitmap(Files.newInputStream(Path.of("./src/jvmMain/resources/fast_boat.png")))
val aircraftCarrierMap = loadImageBitmap(Files.newInputStream(Path.of("./src/jvmMain/resources/aircraft_carrier.png")))

val arrowMap = loadImageBitmap(Files.newInputStream(Path.of("./src/jvmMain/resources/arrow.png")))
val oceanMap = loadImageBitmap(Files.newInputStream(Path.of("./src/jvmMain/resources/ocean.jpg")))
val torpedo = loadImageBitmap(Files.newInputStream(Path.of("./src/jvmMain/resources/torpedo.png")))
val explosion = loadImageBitmap(Files.newInputStream(Path.of("./src/jvmMain/resources/explosion.png")))

val settings = loadImageBitmap(Files.newInputStream(Path.of("./src/jvmMain/resources/settings.png")))

val transformMatrix = { angleX: Float, angleY: Float, angleZ:Float ->
    Matrix().apply {
        rotateX(angleX)
        rotateY(angleY)
        rotateZ(angleZ)
    }
}

val deTransformMatrix = { angleX: Float, angleY: Float, angleZ:Float ->
    Matrix().apply {
        rotateZ(-angleZ)
        rotateY(-angleY)
        rotateX(-angleX)
    }
}