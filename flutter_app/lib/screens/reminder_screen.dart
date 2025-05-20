import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:intl/intl.dart';
import '../models/app_state.dart';

class ReminderScreen extends StatefulWidget {
  const ReminderScreen({super.key});

  @override
  State<ReminderScreen> createState() => _ReminderScreenState();
}

class _ReminderScreenState extends State<ReminderScreen> {
  int _selectedDuration = 10;
  TimeOfDay _selectedTime = TimeOfDay.now();
  FrequencyType _frequencyType = FrequencyType.weekly;
  int _frequencyCount = 3;
  List<bool> _selectedDays = List.generate(7, (index) => false);

  @override
  void initState() {
    super.initState();
    final appState = Provider.of<AppState>(context, listen: false);
    _selectedDuration = appState.selectedDuration;
    
    // Default to selecting Tuesday and Thursday
    _selectedDays[2] = true; // Tuesday
    _selectedDays[4] = true; // Thursday
  }

  Future<void> _selectTime(BuildContext context) async {
    final TimeOfDay? picked = await showTimePicker(
      context: context,
      initialTime: _selectedTime,
    );
    if (picked != null && picked != _selectedTime) {
      setState(() {
        _selectedTime = picked;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final appState = Provider.of<AppState>(context);
    
    return Scaffold(
      body: Container(
        decoration: const BoxDecoration(
          image: DecorationImage(
            image: AssetImage('assets/images/updated_reminder_screen.png'),
            fit: BoxFit.cover,
          ),
        ),
        child: SafeArea(
          child: SingleChildScrollView(
            child: Padding(
              padding: const EdgeInsets.all(20.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Back button and title
                  Row(
                    children: [
                      IconButton(
                        icon: const Icon(Icons.arrow_back, color: Color(0xFF2E8B57)),
                        onPressed: () => Navigator.pop(context),
                      ),
                      const Text(
                        'Schedule Sessions',
                        style: TextStyle(
                          fontSize: 24,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFF2E8B57),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 20),
                  
                  // Calendar view is part of the background image
                  const SizedBox(height: 220),
                  
                  // Session duration selection
                  const Text(
                    'Select Session',
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF2E8B57),
                    ),
                  ),
                  const SizedBox(height: 10),
                  GridView.count(
                    crossAxisCount: 2,
                    shrinkWrap: true,
                    physics: const NeverScrollableScrollPhysics(),
                    mainAxisSpacing: 10,
                    crossAxisSpacing: 10,
                    childAspectRatio: 3,
                    children: appState.sessionDurations.map((duration) {
                      return GestureDetector(
                        onTap: () {
                          setState(() {
                            _selectedDuration = duration;
                          });
                        },
                        child: Container(
                          decoration: BoxDecoration(
                            color: Colors.white.withOpacity(0.7),
                            borderRadius: BorderRadius.circular(30),
                            border: Border.all(
                              color: _selectedDuration == duration
                                  ? const Color(0xFF2E8B57)
                                  : Colors.transparent,
                              width: 2,
                            ),
                          ),
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              const Icon(
                                Icons.eco,
                                color: Color(0xFF2E8B57),
                              ),
                              const SizedBox(width: 5),
                              Text(
                                '$duration min',
                                style: const TextStyle(
                                  fontSize: 16,
                                  fontWeight: FontWeight.bold,
                                  color: Color(0xFF2E8B57),
                                ),
                              ),
                            ],
                          ),
                        ),
                      );
                    }).toList(),
                  ),
                  const SizedBox(height: 20),
                  
                  // Time picker
                  GestureDetector(
                    onTap: () => _selectTime(context),
                    child: Container(
                      width: double.infinity,
                      padding: const EdgeInsets.symmetric(vertical: 15),
                      decoration: BoxDecoration(
                        color: Colors.white.withOpacity(0.7),
                        borderRadius: BorderRadius.circular(30),
                      ),
                      child: Center(
                        child: Text(
                          '${_selectedTime.format(context)}',
                          style: const TextStyle(
                            fontSize: 24,
                            fontWeight: FontWeight.bold,
                            color: Color(0xFF2E8B57),
                          ),
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 20),
                  
                  // Frequency selector
                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 5),
                    decoration: BoxDecoration(
                      color: Colors.white.withOpacity(0.7),
                      borderRadius: BorderRadius.circular(30),
                    ),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text(
                          'Frequency',
                          style: TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                            color: Color(0xFF2E8B57),
                          ),
                        ),
                        DropdownButton<String>(
                          value: '$_frequencyCount times per ${_frequencyType == FrequencyType.daily ? 'day' : 'week'}',
                          icon: const Icon(Icons.arrow_drop_down, color: Color(0xFF2E8B57)),
                          underline: Container(),
                          style: const TextStyle(
                            fontSize: 18,
                            color: Color(0xFF2E8B57),
                          ),
                          onChanged: (String? newValue) {
                            if (newValue != null) {
                              setState(() {
                                if (newValue.contains('day')) {
                                  _frequencyType = FrequencyType.daily;
                                } else {
                                  _frequencyType = FrequencyType.weekly;
                                }
                                _frequencyCount = int.parse(newValue.split(' ')[0]);
                              });
                            }
                          },
                          items: [
                            '1 times per day',
                            '2 times per day',
                            '3 times per day',
                            '1 times per week',
                            '2 times per week',
                            '3 times per week',
                            '4 times per week',
                            '5 times per week',
                          ].map<DropdownMenuItem<String>>((String value) {
                            return DropdownMenuItem<String>(
                              value: value,
                              child: Text(value),
                            );
                          }).toList(),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 20),
                  
                  // Day selector (only show for weekly frequency)
                  if (_frequencyType == FrequencyType.weekly)
                    Container(
                      padding: const EdgeInsets.symmetric(vertical: 10),
                      decoration: BoxDecoration(
                        color: Colors.white.withOpacity(0.7),
                        borderRadius: BorderRadius.circular(30),
                      ),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: [
                          _dayButton(0, 'S'),
                          _dayButton(1, 'M'),
                          _dayButton(2, 'T'),
                          _dayButton(3, 'W'),
                          _dayButton(4, 'T'),
                          _dayButton(5, 'F'),
                          _dayButton(6, 'S'),
                        ],
                      ),
                    ),
                  const SizedBox(height: 30),
                  
                  // Add reminder button
                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton(
                      onPressed: () {
                        // Create and add the reminder
                        final reminder = Reminder(
                          sessionDuration: _selectedDuration,
                          time: _selectedTime,
                          frequencyType: _frequencyType,
                          frequencyCount: _frequencyCount,
                          weekdays: _selectedDays,
                        );
                        appState.addReminder(reminder);
                        
                        // Show confirmation and navigate back
                        ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(
                            content: Text('Reminder added successfully'),
                            backgroundColor: Color(0xFF2E8B57),
                          ),
                        );
                        Navigator.pop(context);
                      },
                      style: ElevatedButton.styleFrom(
                        backgroundColor: const Color(0xFF2E8B57),
                        foregroundColor: Colors.white,
                        padding: const EdgeInsets.symmetric(vertical: 15),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(30),
                        ),
                      ),
                      child: const Text(
                        'Add Reminder',
                        style: TextStyle(fontSize: 20),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
  
  Widget _dayButton(int index, String label) {
    return GestureDetector(
      onTap: () {
        setState(() {
          _selectedDays[index] = !_selectedDays[index];
        });
      },
      child: Container(
        width: 36,
        height: 36,
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          color: _selectedDays[index]
              ? const Color(0xFF2E8B57)
              : Colors.transparent,
        ),
        child: Center(
          child: Text(
            label,
            style: TextStyle(
              color: _selectedDays[index] ? Colors.white : const Color(0xFF2E8B57),
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
      ),
    );
  }
}
