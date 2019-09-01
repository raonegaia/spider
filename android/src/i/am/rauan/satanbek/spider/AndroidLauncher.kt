package i.am.rauan.satanbek.spider

import android.os.Bundle
import android.util.DisplayMetrics

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import i.am.rauan.satanbek.spider.constants.Global.SCREEN_HEIGHT
import i.am.rauan.satanbek.spider.constants.Global.SCREEN_WIDTH

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup screen width and height.
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        SCREEN_WIDTH = dm.widthPixels.toFloat()
        SCREEN_HEIGHT = dm.heightPixels.toFloat()

        val config = AndroidApplicationConfiguration()

        initialize(Droplet(), config)
    }
}
