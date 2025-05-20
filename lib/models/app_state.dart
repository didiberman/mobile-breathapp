import 'package:flutter/material.dart';

class AppState extends ChangeNotifier {
  // Session duration options in minutes
  final List<int> sessionDurations = [5, 10, 15, 30];
  
  // Currently selected session duration
  int _selectedDuration = 10; // Default to 10 minutes
  
  // Playback state
  bool _isPlaying = false;
  
  // Reminders
  List<Reminder> _reminders = [];
  
  // Getters
  int get selectedDuration => _selectedDuration;
  bool get isPlaying => _isPlaying;
  List<Reminder> get reminders => _reminders;
  
  // Setters with notifications
  void setSelectedDuration(int duration) {
    if (sessionDurations.contains(duration)) {
      _selectedDuration = duration;
      notifyListeners();
    }
  }
  
  void togglePlayback() {
    _isPlaying = !_isPlaying;
    notifyListeners();
  }
  
  void addReminder(Reminder reminder) {
    _reminders.add(reminder);
    notifyListeners();
  }
  
  void removeReminder(Reminder reminder) {
    _reminders.remove(reminder);
    notifyListeners();
  }
}

class Reminder {
  final int sessionDuration;
  final TimeOfDay time;
  final FrequencyType frequencyType;
  final int frequencyCount;
  final List<bool> weekdays; // For weekly frequency, which days are selected
  
  Reminder({
    required this.sessionDuration,
    required this.time,
    required this.frequencyType,
    required this.frequencyCount,
    required this.weekdays,
  });
}

enum FrequencyType {
  daily,
  weekly
}
