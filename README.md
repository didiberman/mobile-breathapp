# Nature-Inspired Breathwork App

A beautiful, calming breathwork application with a nature-inspired design for Android devices.

## Features

- **Beautiful Nature-Inspired Design**: Serene forest themes and organic visual elements
- **Multiple Session Durations**: Choose from 5, 10, 15, or 30 minute sessions
- **Music Playback**: Play/pause controls for breathwork audio
- **Advanced Scheduling**: Schedule sessions multiple times per day or week

## Screenshots

The app includes the following screens:
- Splash Screen: A serene forest scene with the app logo
- Home Screen: Session duration selection with leaf icons
- Session Screen: Play/pause controls with circular progress indicator
- Reminder Screen: Advanced scheduling with frequency options

## Installation Instructions

### For Developers

1. **Set up Flutter Development Environment**:
   - Install [Flutter SDK](https://flutter.dev/docs/get-started/install)
   - Set up an IDE (VS Code or Android Studio recommended)
   - Run `flutter doctor` to verify your installation

2. **Clone and Run the Project**:
   ```bash
   # Clone the repository (if using version control)
   git clone <repository-url>
   
   # Navigate to project directory
   cd breathwork_app/flutter_app
   
   # Get dependencies
   flutter pub get
   
   # Run on connected device or emulator
   flutter run
   ```

3. **Build APK for Distribution**:
   ```bash
   # Generate a release build
   flutter build apk --release
   
   # The APK will be available at:
   # build/app/outputs/flutter-apk/app-release.apk
   ```

### For Non-Developers (Installing on Android)

1. **Enable Installation from Unknown Sources**:
   - Go to Settings > Security
   - Enable "Unknown Sources" to allow installation of apps from sources other than the Play Store
   - On newer Android versions, you might be prompted to allow installation when you try to install the app

2. **Install the APK**:
   - Transfer the APK file to your Android device
   - Locate the APK file using a file manager
   - Tap on the APK file to begin installation
   - Follow the on-screen prompts to complete installation

## Adding Your Music Files

The app is designed to play music files that you provide. To add your music files:

1. Create a folder named `audio` in your device's storage
2. Place your breathwork session audio files in this folder
3. Name the files according to their duration:
   - `5min_session.mp3`
   - `10min_session.mp3`
   - `15min_session.mp3`
   - `30min_session.mp3`

## Customization

You can customize the app by modifying the following:

- **Colors**: Edit the color scheme in `lib/main.dart`
- **Images**: Replace the background images in `assets/images/`
- **Session Durations**: Modify the available durations in `lib/models/app_state.dart`

## Troubleshooting

If you encounter any issues:

- Ensure Flutter is properly installed and updated
- Check that all dependencies are installed with `flutter pub get`
- For Android installation issues, verify that you've enabled installation from unknown sources
- Make sure your audio files are in the correct format (MP3 recommended)

## License

This project is provided for personal use.

## Credits

Designed and developed with ❤️ for a better breathwork experience.
