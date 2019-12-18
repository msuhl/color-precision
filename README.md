# Color Predictor

## Example
```
import java.io.File
import javax.imageio.ImageIO

fun main() {
        // Making target from local file
        val file = File("<filepath>")
        val image = ImageIO.read(file)
        val colorPredictor = ColorPredictor()
        //val target = colorPredictor.getTargetModelData("my-image", image)
        
        // Makeing target from url
        val target = colorPredictor.getTargetModelData("my-image", "https://<url>")
    
        val model = colorPredictor.loadModel("<filepath>/model.json")
        val k = 3
        val result = colorPredictor.PredictColor(target, model, k)
        print(result) // red
}
```

## Setup

- Add it in your root build.gradle at the end of repositories:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

- Add the dependency

```
dependencies {
        implementation 'com.github.Silverbaq:color-precision:0.1-test'
}
```

- Download the Premade [model](https://github.com/Silverbaq/color-precision/blob/0.0.1/data/image/model.json) (or make your own). Place it so you can parse it into the code.