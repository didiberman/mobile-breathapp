import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/app_state.dart';

class SessionScreen extends StatefulWidget {
  const SessionScreen({super.key});

  @override
  State<SessionScreen> createState() => _SessionScreenState();
}

class _SessionScreenState extends State<SessionScreen> with SingleTickerProviderStateMixin {
  late AnimationController _animationController;
  late Animation<double> _progressAnimation;
  int _remainingSeconds = 0;
  late Timer _timer;

  @override
  void initState() {
    super.initState();
    final appState = Provider.of<AppState>(context, listen: false);
    _remainingSeconds = appState.selectedDuration * 60;
    
    _animationController = AnimationController(
      vsync: this,
      duration: Duration(minutes: appState.selectedDuration),
    );
    
    _progressAnimation = Tween<double>(
      begin: 1.0,
      end: 0.0,
    ).animate(_animationController);
    
    // Start timer
    _timer = Timer.periodic(const Duration(seconds: 1), _updateTimer);
    
    // Auto-start playback
    WidgetsBinding.instance.addPostFrameCallback((_) {
      appState.togglePlayback();
      _animationController.forward();
    });
  }
  
  void _updateTimer(Timer timer) {
    if (_remainingSeconds > 0 && Provider.of<AppState>(context, listen: false).isPlaying) {
      setState(() {
        _remainingSeconds--;
      });
    } else if (_remainingSeconds <= 0) {
      _timer.cancel();
      _animationController.stop();
      // Session complete logic would go here
    }
  }
  
  @override
  void dispose() {
    _timer.cancel();
    _animationController.dispose();
    super.dispose();
  }

  String _formatTime(int seconds) {
    final minutes = seconds ~/ 60;
    final remainingSeconds = seconds % 60;
    return '$minutes:${remainingSeconds.toString().padLeft(2, '0')}';
  }

  @override
  Widget build(BuildContext context) {
    final appState = Provider.of<AppState>(context);
    
    return Scaffold(
      body: Container(
        decoration: const BoxDecoration(
          image: DecorationImage(
            image: AssetImage('assets/images/session_screen.png'),
            fit: BoxFit.cover,
          ),
        ),
        child: SafeArea(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Spacer(),
              // Progress indicator and play/pause button
              AnimatedBuilder(
                animation: _progressAnimation,
                builder: (context, child) {
                  return Stack(
                    alignment: Alignment.center,
                    children: [
                      SizedBox(
                        width: 200,
                        height: 200,
                        child: CircularProgressIndicator(
                          value: _progressAnimation.value,
                          strokeWidth: 8,
                          backgroundColor: Colors.white.withOpacity(0.3),
                          valueColor: const AlwaysStoppedAnimation<Color>(Colors.white),
                        ),
                      ),
                      Container(
                        width: 160,
                        height: 160,
                        decoration: BoxDecoration(
                          color: Colors.white.withOpacity(0.2),
                          shape: BoxShape.circle,
                        ),
                        child: IconButton(
                          icon: Icon(
                            appState.isPlaying ? Icons.pause : Icons.play_arrow,
                            size: 60,
                            color: Colors.white,
                          ),
                          onPressed: () {
                            appState.togglePlayback();
                            if (appState.isPlaying) {
                              _animationController.forward();
                            } else {
                              _animationController.stop();
                            }
                          },
                        ),
                      ),
                    ],
                  );
                },
              ),
              const Spacer(),
              // Time remaining
              Padding(
                padding: const EdgeInsets.only(bottom: 100),
                child: Text(
                  '${_formatTime(_remainingSeconds)} remaining',
                  style: const TextStyle(
                    fontSize: 32,
                    fontWeight: FontWeight.bold,
                    color: Colors.white,
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
