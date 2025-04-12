package com.traverse.panduanmemori.utils

import android.media.MediaPlayer
import java.io.File

class AudioPlayer {
    private var mediaPlayer: MediaPlayer? = null

    fun playAudioFromFile(audioFile: File, onCompletion: () -> Unit = {}) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioFile.absolutePath)
                prepare()
                start()
                setOnCompletionListener {
                    onCompletion()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}