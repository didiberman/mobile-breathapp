package com.didi.breathedeep.data

import android.content.Context
import android.content.SharedPreferences
import java.util.*

/**
 * Manages meditation session data and history
 */
class SessionManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private var currentSessionId: String? = null
    
    /**
     * Records the start of a new meditation session
     */
    fun startSession(durationMinutes: Int) {
        val sessionId = UUID.randomUUID().toString()
        currentSessionId = sessionId
        
        val sessionData = SessionData(
            id = sessionId,
            startTime = System.currentTimeMillis(),
            durationMinutes = durationMinutes,
            completed = false
        )
        
        saveSession(sessionData)
    }
    
    /**
     * Marks the current session as completed
     */
    fun completeSession() {
        currentSessionId?.let { id ->
            val session = getSession(id)
            session?.let {
                val updatedSession = it.copy(
                    completed = true,
                    endTime = System.currentTimeMillis()
                )
                saveSession(updatedSession)
                
                // Update total minutes
                val currentTotal = getTotalMinutes()
                saveTotalMinutes(currentTotal + it.durationMinutes)
                
                // Update total sessions
                val currentSessions = getTotalSessions()
                saveTotalSessions(currentSessions + 1)
                
                // Update streak
                updateStreak()
            }
        }
    }
    
    /**
     * Retrieves a specific session by ID
     */
    private fun getSession(id: String): SessionData? {
        val sessionJson = prefs.getString(SESSION_PREFIX + id, null) ?: return null
        return SessionData.fromJson(sessionJson)
    }
    
    /**
     * Saves a session to persistent storage
     */
    private fun saveSession(session: SessionData) {
        prefs.edit().putString(SESSION_PREFIX + session.id, session.toJson()).apply()
        
        // Add to session list
        val sessionList = getSessionList().toMutableList()
        if (!sessionList.contains(session.id)) {
            sessionList.add(session.id)
            saveSessionList(sessionList)
        }
    }
    
    /**
     * Gets the list of all session IDs
     */
    private fun getSessionList(): List<String> {
        val listJson = prefs.getString(SESSION_LIST_KEY, null) ?: return emptyList()
        return try {
            listJson.split(",").filter { it.isNotEmpty() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * Saves the list of session IDs
     */
    private fun saveSessionList(list: List<String>) {
        prefs.edit().putString(SESSION_LIST_KEY, list.joinToString(",")).apply()
    }
    
    /**
     * Gets all completed sessions
     */
    fun getCompletedSessions(): List<SessionData> {
        return getSessionList()
            .mapNotNull { getSession(it) }
            .filter { it.completed }
            .sortedByDescending { it.startTime }
    }
    
    /**
     * Gets the total meditation minutes
     */
    fun getTotalMinutes(): Int {
        return prefs.getInt(TOTAL_MINUTES_KEY, 0)
    }
    
    /**
     * Saves the total meditation minutes
     */
    private fun saveTotalMinutes(minutes: Int) {
        prefs.edit().putInt(TOTAL_MINUTES_KEY, minutes).apply()
    }
    
    /**
     * Gets the total number of completed sessions
     */
    fun getTotalSessions(): Int {
        return prefs.getInt(TOTAL_SESSIONS_KEY, 0)
    }
    
    /**
     * Saves the total number of completed sessions
     */
    private fun saveTotalSessions(count: Int) {
        prefs.edit().putInt(TOTAL_SESSIONS_KEY, count).apply()
    }
    
    /**
     * Updates the current streak based on session history
     */
    private fun updateStreak() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val todayStart = calendar.timeInMillis
        
        // Check if there's a session today
        val hasTodaySession = getCompletedSessions().any { 
            it.startTime >= todayStart 
        }
        
        if (hasTodaySession) {
            // Get the last streak update day
            val lastStreakDay = prefs.getLong(LAST_STREAK_DAY_KEY, 0)
            val currentStreak = prefs.getInt(CURRENT_STREAK_KEY, 0)
            
            calendar.timeInMillis = lastStreakDay
            val lastDay = calendar.get(Calendar.DAY_OF_YEAR)
            val lastYear = calendar.get(Calendar.YEAR)
            
            calendar.timeInMillis = System.currentTimeMillis()
            val today = calendar.get(Calendar.DAY_OF_YEAR)
            val thisYear = calendar.get(Calendar.YEAR)
            
            // Check if this is a consecutive day
            if (lastStreakDay == 0L || 
                (thisYear == lastYear && today - lastDay == 1) ||
                (thisYear > lastYear && today == 1 && isLastDayOfYear(lastDay, lastYear))) {
                // Consecutive day, increment streak
                saveCurrentStreak(currentStreak + 1)
            } else if (thisYear == lastYear && today == lastDay) {
                // Same day, do nothing
            } else {
                // Streak broken, reset to 1
                saveCurrentStreak(1)
            }
            
            // Update last streak day
            prefs.edit().putLong(LAST_STREAK_DAY_KEY, todayStart).apply()
            
            // Update longest streak if needed
            val longestStreak = prefs.getInt(LONGEST_STREAK_KEY, 0)
            val updatedStreak = prefs.getInt(CURRENT_STREAK_KEY, 0)
            if (updatedStreak > longestStreak) {
                prefs.edit().putInt(LONGEST_STREAK_KEY, updatedStreak).apply()
            }
        }
    }
    
    /**
     * Checks if the given day is the last day of the year
     */
    private fun isLastDayOfYear(day: Int, year: Int): Boolean {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        calendar.set(Calendar.DAY_OF_MONTH, 31)
        return day == calendar.get(Calendar.DAY_OF_YEAR)
    }
    
    /**
     * Gets the current streak of consecutive days with meditation
     */
    fun getCurrentStreak(): Int {
        return prefs.getInt(CURRENT_STREAK_KEY, 0)
    }
    
    /**
     * Saves the current streak value
     */
    private fun saveCurrentStreak(streak: Int) {
        prefs.edit().putInt(CURRENT_STREAK_KEY, streak).apply()
    }
    
    /**
     * Gets the longest streak achieved
     */
    fun getLongestStreak(): Int {
        return prefs.getInt(LONGEST_STREAK_KEY, 0)
    }
    
    companion object {
        private const val PREFS_NAME = "breathe_deep_prefs"
        private const val SESSION_PREFIX = "session_"
        private const val SESSION_LIST_KEY = "session_list"
        private const val TOTAL_MINUTES_KEY = "total_minutes"
        private const val TOTAL_SESSIONS_KEY = "total_sessions"
        private const val CURRENT_STREAK_KEY = "current_streak"
        private const val LONGEST_STREAK_KEY = "longest_streak"
        private const val LAST_STREAK_DAY_KEY = "last_streak_day"
    }
}

/**
 * Data class representing a meditation session
 */
data class SessionData(
    val id: String,
    val startTime: Long,
    val durationMinutes: Int,
    val completed: Boolean,
    val endTime: Long = 0
) {
    /**
     * Converts the session data to JSON format
     */
    fun toJson(): String {
        return "$id|$startTime|$durationMinutes|$completed|$endTime"
    }
    
    companion object {
        /**
         * Creates a SessionData object from JSON
         */
        fun fromJson(json: String): SessionData? {
            return try {
                val parts = json.split("|")
                SessionData(
                    id = parts[0],
                    startTime = parts[1].toLong(),
                    durationMinutes = parts[2].toInt(),
                    completed = parts[3].toBoolean(),
                    endTime = parts[4].toLong()
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}
