import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'screens/splash_screen.dart';
import 'screens/home_screen.dart';
import 'screens/session_screen.dart';
import 'screens/reminder_screen.dart';
import 'models/app_state.dart';

void main() {
  runApp(
    ChangeNotifierProvider(
      create: (context) => AppState(),
      child: const BreathworkApp(),
    ),
  );
}

class BreathworkApp extends StatelessWidget {
  const BreathworkApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Breathwork',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF2E8B57), // Forest Green
          primary: const Color(0xFF2E8B57),
          secondary: const Color(0xFF32CD32),
          background: const Color(0xFFF5F5DC),
        ),
        textTheme: const TextTheme(
          displayLarge: TextStyle(
            fontFamily: 'Quicksand',
            color: Color(0xFF2F4F4F),
          ),
          displayMedium: TextStyle(
            fontFamily: 'Quicksand',
            color: Color(0xFF2F4F4F),
          ),
          displaySmall: TextStyle(
            fontFamily: 'Quicksand',
            color: Color(0xFF2F4F4F),
          ),
          bodyLarge: TextStyle(
            fontFamily: 'Roboto',
            color: Color(0xFF2F4F4F),
          ),
          bodyMedium: TextStyle(
            fontFamily: 'Roboto',
            color: Color(0xFF2F4F4F),
          ),
        ),
        useMaterial3: true,
      ),
      initialRoute: '/',
      routes: {
        '/': (context) => const SplashScreen(),
        '/home': (context) => const HomeScreen(),
        '/session': (context) => const SessionScreen(),
        '/reminder': (context) => const ReminderScreen(),
      },
    );
  }
}
