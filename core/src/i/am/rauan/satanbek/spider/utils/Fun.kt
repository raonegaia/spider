package i.am.rauan.satanbek.spider.utils

import com.badlogic.gdx.Gdx
import i.am.rauan.satanbek.spider.Droplet

class Fun() {
    companion object {
        fun hexToFloatArray(hex: String): ArrayList<Float> {
            var color = hex.replace("#", "").replace(" ", "")

            if (hex.length == 3) {
                color = "${hex[0]}${hex[0]}${hex[1]}${hex[1]}${hex[2]}${hex[2]}"
            }

            if (color.length != 6) arrayListOf<Float>(0f, 0f, 0f)

            val f: Int = hexCharToInt(color[0]) * 16 + hexCharToInt(color[1])
            val s: Int = hexCharToInt(color[2]) * 16 + hexCharToInt(color[3])
            val t: Int = hexCharToInt(color[4]) * 16 + hexCharToInt(color[5])
            val convertedColor = arrayListOf(f / 255f, s / 255f, t / 255f)

            return convertedColor
        }


        private fun hexCharToInt(ch: Char): Int {
            when(ch) {
                'a' -> return 10
                'b' -> return 11
                'c' -> return 12
                'd' -> return 13
                'e' -> return 14
                'f' -> return 15
                else -> return ch.toString().toInt()
            }
        }
    }
}