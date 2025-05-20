import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/app_state.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final appState = Provider.of<AppState>(context);
    
    return Scaffold(
      body: Container(
        decoration: const BoxDecoration(
          image: DecorationImage(
            image: AssetImage('assets/images/home_screen.png'),
            fit: BoxFit.cover,
          ),
        ),
        child: SafeArea(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const SizedBox(height: 80),
              // Session duration selection
              Expanded(
                child: GridView.count(
                  crossAxisCount: 2,
                  padding: const EdgeInsets.symmetric(horizontal: 20),
                  mainAxisSpacing: 20,
                  crossAxisSpacing: 20,
                  physics: const NeverScrollableScrollPhysics(),
                  children: appState.sessionDurations.map((duration) {
                    return GestureDetector(
                      onTap: () {
                        appState.setSelectedDuration(duration);
                      },
                      child: Container(
                        decoration: BoxDecoration(
                          color: Colors.white.withOpacity(0.3),
                          borderRadius: BorderRadius.circular(100),
                          border: Border.all(
                            color: appState.selectedDuration == duration
                                ? Colors.white
                                : Colors.transparent,
                            width: 3,
                          ),
                        ),
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            const Icon(
                              Icons.eco,
                              color: Color(0xFF2E8B57),
                              size: 40,
                            ),
                            const SizedBox(height: 10),
                            Text(
                              '$duration min',
                              style: const TextStyle(
                                fontSize: 24,
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
              ),
              
              // Begin session button
              Padding(
                padding: const EdgeInsets.all(20.0),
                child: ElevatedButton(
                  onPressed: () {
                    Navigator.pushNamed(context, '/session');
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: const Color(0xFF2E8B57),
                    foregroundColor: Colors.white,
                    minimumSize: const Size(double.infinity, 60),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(30),
                    ),
                  ),
                  child: const Text(
                    'Begin Session',
                    style: TextStyle(fontSize: 24),
                  ),
                ),
              ),
              
              // Reminder button
              TextButton.icon(
                onPressed: () {
                  Navigator.pushNamed(context, '/reminder');
                },
                icon: const Icon(Icons.notifications_outlined),
                label: const Text('Schedule Sessions'),
                style: TextButton.styleFrom(
                  foregroundColor: Colors.white,
                ),
              ),
              const SizedBox(height: 20),
            ],
          ),
        ),
      ),
    );
  }
}
