package com.didi.breathedeep.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.didi.breathedeep.R
import com.didi.breathedeep.data.SessionData
import com.didi.breathedeep.data.SessionManager
import com.didi.breathedeep.databinding.ActivityHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var sessionAdapter: SessionAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        sessionManager = SessionManager(this)
        
        setupUI()
        setupListeners()
        loadSessionData()
    }
    
    private fun setupUI() {
        // Setup RecyclerView
        sessionAdapter = SessionAdapter()
        binding.rvRecentSessions.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = sessionAdapter
        }
        
        // Setup bottom navigation
        binding.bottomNavigation.selectedItemId = R.id.navigation_history
    }
    
    private fun setupListeners() {
        // Setup calendar date selection
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            loadSessionsForDate(calendar.timeInMillis)
        }
        
        // Setup bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    true
                }
                R.id.navigation_history -> true
                R.id.navigation_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
    
    private fun loadSessionData() {
        // Load total stats
        binding.tvTotalMinutes.text = sessionManager.getTotalMinutes().toString()
        binding.tvTotalSessions.text = sessionManager.getTotalSessions().toString()
        
        // Load recent sessions
        val sessions = sessionManager.getCompletedSessions()
        if (sessions.isEmpty()) {
            binding.tvNoSessions.visibility = View.VISIBLE
            binding.rvRecentSessions.visibility = View.GONE
        } else {
            binding.tvNoSessions.visibility = View.GONE
            binding.rvRecentSessions.visibility = View.VISIBLE
            sessionAdapter.submitList(sessions)
        }
        
        // Highlight calendar dates with sessions
        highlightSessionDates(sessions)
    }
    
    private fun loadSessionsForDate(date: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val endOfDay = calendar.timeInMillis
        
        val sessions = sessionManager.getCompletedSessions().filter { 
            it.startTime >= startOfDay && it.startTime < endOfDay 
        }
        
        if (sessions.isEmpty()) {
            binding.tvNoSessions.visibility = View.VISIBLE
            binding.rvRecentSessions.visibility = View.GONE
        } else {
            binding.tvNoSessions.visibility = View.GONE
            binding.rvRecentSessions.visibility = View.VISIBLE
            sessionAdapter.submitList(sessions)
        }
    }
    
    private fun highlightSessionDates(sessions: List<SessionData>) {
        // This would be implemented to highlight dates in the calendar
        // that have completed sessions
        // For a real implementation, we would need to extend CalendarView
        // or use a third-party calendar library that supports date highlighting
    }
}

/**
 * Adapter for displaying session history items
 */
class SessionAdapter : androidx.recyclerview.widget.ListAdapter<SessionData, SessionAdapter.SessionViewHolder>(
    object : androidx.recyclerview.widget.DiffUtil.ItemCallback<SessionData>() {
        override fun areItemsTheSame(oldItem: SessionData, newItem: SessionData): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: SessionData, newItem: SessionData): Boolean {
            return oldItem == newItem
        }
    }
) {
    
    class SessionViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        // In a real implementation, we would use ViewBinding here
        val dateText: android.widget.TextView = itemView.findViewById(R.id.tvSessionDate)
        val durationText: android.widget.TextView = itemView.findViewById(R.id.tvSessionDuration)
        
        fun bind(session: SessionData) {
            val dateFormat = SimpleDateFormat("MMMM d", Locale.getDefault())
            dateText.text = dateFormat.format(Date(session.startTime))
            durationText.text = "${session.durationMinutes} min"
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
        return SessionViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
