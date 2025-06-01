# BreathApp

A simple and calming Android application for guided breathing exercises and session tracking.

## Project Description

BreathApp is designed to help users practice mindful breathing techniques to reduce stress, improve focus, and promote relaxation. The app provides guided breathing sessions of various durations and tracks user progress, including total minutes meditated, number of sessions completed, and consecutive daily streaks.

The application features a clean user interface with engaging visual and audio feedback during breathing sessions, including expanding/contracting circles, floating bubbles, light rays, and ripple effects, accompanied by meditation audio.

## Features

*   **Guided Breathing Sessions:** Choose from sessions of 5, 10, 15, or 30 minutes.
*   **Session Tracking:** Records completed sessions, total meditation minutes, and total sessions.
*   **Streak Tracking:** Keeps track of consecutive days with completed sessions.
*   **Session History:** View a list of past sessions and filter by date.
*   **Interactive Animations:** Calming visual effects (expanding circle, bubbles, light rays, ripples) during sessions.
*   **Background Audio:** Meditation audio plays during sessions.
*   **Pause/Resume:** Ability to pause and resume sessions.

## Technical Details

*   Developed in Kotlin for Android.
*   Uses Android Architecture Components (ViewModel - not explicitly seen but good practice, LiveData/StateFlow, etc. - based on UI files, likely used for data binding).
*   Utilizes `MediaPlayer` for audio playback.
*   Custom `View` implementations (`BubbleView`, `LightRayView`, `RippleView`) for animations.
*   Session data stored locally using `SharedPreferences` (`SessionManager`).

## Getting Started

(Instructions on how to build and run the project - Add details here based on your project's setup, e.g., how to clone, open in Android Studio, and run)

