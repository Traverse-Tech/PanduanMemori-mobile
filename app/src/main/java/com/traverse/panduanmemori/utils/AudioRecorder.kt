package com.traverse.panduanmemori.utils

import android.media.MediaRecorder
import android.os.Environment
import java.text.SimpleDateFormat
import java.util.*

class AudioRecorder {
    private var recorder: MediaRecorder? = null
    private var outputFile: String? = null

    fun startRecording(): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        outputFile = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)}/panduanmemory_buddy_$timestamp.m4a"

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile)
            prepare()
            start()
        }

        return outputFile ?: ""
    }

    fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }
}
