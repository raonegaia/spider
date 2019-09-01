package i.am.rauan.satanbek.spider

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.TimeUtils
import i.am.rauan.satanbek.spider.constants.Global
import i.am.rauan.satanbek.spider.utils.Fun

class Droplet: ApplicationAdapter() {
    private lateinit var dropImage: Texture
    private lateinit var bucketImage: Texture
    private lateinit var dropSound: Sound
    private lateinit var rainMusic: Music

    private lateinit var camera: OrthographicCamera
    private lateinit var batch: SpriteBatch

    private lateinit var bucket: Rectangle

    // Rains.
    private var raindropWidth = 64f
    private var raindropHeight = 64f

    private var rainDrops: MutableList<Rectangle> = mutableListOf()
    private var lastDropTime: Long = 0

    // Fly.
    private val flyFrameRow = 1
    private val flyFrameColumn = 4
    private lateinit var flyAnimation: Animation<TextureRegion>
    private lateinit var fly: Texture
    private var stateTime: Float = 0f
    private lateinit var flyRectangle: Rectangle
    private val flySize = 64f

    override fun create() {
        Gdx.app.log(tag, "create(): ${Global.SCREEN_WIDTH}x${Global.SCREEN_HEIGHT}")

        // Load the images for the droplet and bucket, 64x64 pixels each.
        dropImage = Texture(Gdx.files.internal("droplet.png"))
        bucketImage = Texture(Gdx.files.internal("bucket.png"))

        // Load the drop sound and rain background "music".
        dropSound = Gdx.audio.newSound(Gdx.files.internal("waterdrop.wav"))
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("undertreeinrain.mp3"))

        // Start the playback of the background music immediately.
        rainMusic.isLooping = true
        rainMusic.play()

        camera = OrthographicCamera()
        camera.setToOrtho(false, Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT)

        batch = SpriteBatch()

        bucket = Rectangle()
        bucket.x = Global.SCREEN_WIDTH / 2  - 64 / 2
        bucket.y = 20f
        bucket.width = 64f
        bucket.height = 64f

        // Spawn raindrops.
        spawnRainDrop()

        // Fly.
        fly = Texture(Gdx.files.internal("fly_sprite.png"))
        var tmp = TextureRegion.split(fly, fly.width / flyFrameColumn, fly.height / flyFrameRow)

        Gdx.app.log(tag, "tmp: ${tmp[0]}, ${tmp.size}")

        val flyFrames = Array<TextureRegion>(flyFrameColumn * flyFrameRow)
        for (i in 0 until flyFrameRow) {
            for (j in 0 until flyFrameColumn) {
                flyFrames.add(tmp[i][j])
            }
        }

        flyAnimation = Animation<TextureRegion>(0.1f, flyFrames)
        stateTime = 0f
        flyRectangle = Rectangle()
        flyRectangle.x = Global.SCREEN_WIDTH - flySize * 3
        flyRectangle.y = Global.SCREEN_HEIGHT / 2 - flySize / 2
        flyRectangle.width = flySize
        flyRectangle.height = flySize
    }

    override fun render() {
        val bgColor = Fun.hexToFloatArray(Global.bacgroundColor)
        Gdx.gl.glClearColor(bgColor[0], bgColor[1], bgColor[2], 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.projectionMatrix = camera.combined
        batch.begin()
        batch.draw(bucketImage, bucket.x, bucket.y)
        for (it in rainDrops) {
            batch.draw(dropImage, it.x, it.y)
        }

        stateTime += Gdx.graphics.deltaTime

        Gdx.app.log(tag, "stateTime=$stateTime")

        var fly = flyAnimation.getKeyFrame(stateTime, true)
        batch.draw(fly, flyRectangle.x, flyRectangle.y)

        batch.end()

        if (Gdx.input.isTouched) {
            val touchPos = Vector3()
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPos)
            bucket.x = touchPos.x - 64 / 2

            if (bucket.x < 0) bucket.x = 0f
            if (bucket.x > Global.SCREEN_WIDTH - 64) bucket.x = Global.SCREEN_WIDTH - 64f
        }

        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRainDrop()

        val iterator = rainDrops.iterator()
        for (it in iterator) {
            it.y -= 200 * Gdx.graphics.deltaTime
            if (it.y + raindropWidth < 0) iterator.remove()

            if (it.overlaps(bucket)) {
                dropSound.play()
                iterator.remove()
            }
        }

    }

    private fun spawnRainDrop() {
        val raindrop = Rectangle()
        raindrop.x = MathUtils.random(0f, Global.SCREEN_WIDTH - raindropWidth)
        raindrop.y = Global.SCREEN_HEIGHT

        raindrop.width = raindropWidth
        raindrop.height = raindropHeight

        rainDrops.add(raindrop)
        lastDropTime = TimeUtils.nanoTime()
    }

    companion object {
        val tag: String = "Droplet"
    }
}
