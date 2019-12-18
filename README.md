# Color Predictor

## Example
```
import java.io.File
import javax.imageio.ImageIO

fun main() {
    val predector = ColorPredictor()

    val file = File("/Users/silverbaq/Github/color-predictor/data/image/test/blue/8007-home.jpg")
    val image = ImageIO.read(file)
    val target = predector.getTargetModelData("my-image", image)

    val k = 3
    val result = predector.PredictColor(target, k)
    println(result) // blue
}
```