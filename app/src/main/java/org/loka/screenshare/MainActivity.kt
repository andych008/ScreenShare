package org.loka.screenshare

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import org.loka.screensharekit.EncodeBuilder
import org.loka.screensharekit.ScreenShareKit
import org.loka.screensharekit.callback.AudioCallBack
import org.loka.screensharekit.callback.H264CallBack
import org.loka.screensharekit.callback.RGBACallBack
import java.io.File
import java.nio.ByteBuffer

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val isDumpEs = false

    private lateinit var file: File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.start).setOnClickListener {
            requestCapture()
        }

        findViewById<Button>(R.id.stop).setOnClickListener {
            ScreenShareKit.stop()
        }
        file = File(getExternalFilesDir("")?.absolutePath + "/video.es");
        if (file.exists()) {
            file.delete()
        }
    }


    private fun requestCapture() {
        ScreenShareKit.init(this).onH264(object :H264CallBack{
            override fun onH264(
                buffer: ByteBuffer,
                isKeyFrame: Boolean,
                width: Int,
                height: Int,
                ts: Long
            ) {
                Log.i(TAG, "ts: $ts, isKeyFrame: $isKeyFrame, size: ${buffer.remaining()}")
                val array = ByteArray(buffer.remaining())
                buffer.get(array)
                if (isDumpEs) {
                    file.appendBytes(array)
                }
            }

        }).onStart({
            //用户同意采集，开始采集数据
        }).start()

    }
}