package com.didi.breathedeep.util

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import java.io.IOException

/**
 * Manages audio playback for meditation sessions
 */
class AudioManager(private val context: Context) {
    
    private var mediaPlayer: MediaPlayer? = null
    
    /**
     * Plays audio from the specified resource ID
     */
    fun playAudio(resourceId: Int) {
        // Release any existing MediaPlayer
        releaseAudio()
        
        // Create and prepare new MediaPlayer
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            
            try {
                context.resources.openRawResourceFd(resourceId)?.use { fd ->
                    setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
                    prepare()
                    start()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * Pauses current audio playback
     */
    fun pauseAudio() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }
    
    /**
     * Resumes paused audio playback
     */
    fun resumeAudio() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }
    
    /**
     * Stops audio playback
     */
    fun stopAudio() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
        }
    }
    
    /**
     * Releases MediaPlayer resources
     */
    fun releaseAudio() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
    
    /**
     * Checks if audio is currently playing
     */
    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }
}
