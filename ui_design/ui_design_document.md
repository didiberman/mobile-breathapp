# Breathe Deep with Didi Being: UI Design Document

## Design Philosophy

The UI design for "Breathe Deep with Didi Being" embraces a serene underwater theme with teal/aqua color palette to create a calming, immersive experience. The design focuses on simplicity and intuitive navigation, allowing users to quickly access meditation sessions without distractions.

## Color Palette

### Primary Colors
- **Deep Teal**: #006064 - Used for app bar and primary elements
- **Aqua**: #00BCD4 - Used for interactive elements and accents
- **Light Aqua**: #B2EBF2 - Used for backgrounds and subtle elements
- **White**: #FFFFFF - Used for text and contrast elements

### Secondary Colors
- **Deep Blue**: #01579B - Used for depth and contrast
- **Soft Gold**: #FFD54F - Used for Buddha figure highlights and special elements
- **Coral**: #FF6E40 - Used sparingly for notifications and important actions

## Typography

- **Primary Font**: Roboto - Clean, modern, and highly readable
- **Accent Font**: Montserrat - For headings and emphasis
- **Meditation Timer**: Roboto Mono - For clear time display

## Key Screens

### 1. Splash Screen

The splash screen introduces users to the app with a gentle fade-in animation of the app logo - a stylized Buddha figure surrounded by subtle water ripples.

**Elements:**
- App logo (Buddha silhouette)
- App name in Montserrat font
- Subtle bubble animations rising from bottom
- Gentle fade-in transition (1.5 seconds)

### 2. Onboarding Screen

A simple, one-page onboarding screen that introduces the app's purpose and core features.

**Elements:**
- Welcome message: "Welcome to Breathe Deep"
- Brief description: "Find calm through guided underwater meditation"
- Buddha figure illustration (centered)
- Light rays emanating from top of screen
- "Get Started" button (aqua color)
- Skip option in top-right corner

### 3. Home Screen

The main navigation hub featuring the four session duration options in a grid layout.

**Elements:**
- App name in header
- Buddha figure (smaller version) in top section
- Four session duration cards (5, 10, 15, 30 minutes)
- Each card features:
  - Duration in large numerals
  - "Minutes" label
  - Subtle underwater background gradient
  - Ripple effect on touch
- Bottom navigation with:
  - Home (active)
  - History
  - Settings

### 4. Session Screen

The core meditation experience screen where users engage with the breathing exercise.

**Elements:**
- Large central Buddha figure
- Animated breathing circle that expands/contracts
- Current session time remaining (top)
- Pause/Resume button (bottom)
- Subtle bubble animations floating upward
- Light rays effect from top of screen
- Back button (returns to home with confirmation dialog)

### 5. Session Complete Screen

Appears after a session is successfully completed.

**Elements:**
- "Session Complete" message
- Session statistics (duration completed)
- Total meditation time accumulated
- Congratulatory message
- "Return Home" button
- "Start Another Session" button
- Celebration animation (gentle bubbles rising)

### 6. History Screen

Displays the user's meditation history and achievements.

**Elements:**
- Calendar view (monthly)
  - Days with completed sessions highlighted
  - Current day outlined
- Total statistics section
  - Total sessions completed
  - Total minutes meditated
  - Current streak (consecutive days)
- Recent sessions list
  - Date and time
  - Duration
  - Scrollable list with most recent at top

### 7. Settings Screen

Allows users to configure app preferences and notifications.

**Elements:**
- Notification settings
  - Daily reminder toggle
  - Reminder time picker
- Audio settings
  - Volume control
  - Background audio toggle
- App information
  - Version number
  - About section
  - Contact/feedback option

## UI Components

### 1. Animated Breathing Circle

The central interactive element during meditation sessions.

**Specifications:**
- Circular gradient from light aqua to deep teal
- Expands and contracts based on breathing pattern
- Inhale: 4 seconds expansion
- Hold: 1 second pause
- Exhale: 6 seconds contraction
- Rest: 1 second pause
- Subtle glow effect during expansion

### 2. Buddha Figure

The iconic visual element that anchors the app's identity.

**Specifications:**
- Simplified silhouette style
- Gold/amber color palette
- Seated in lotus position
- Subtle ambient glow
- Placed centrally in composition
- Scales appropriately across different screens

### 3. Bubble Animations

Ambient animations that enhance the underwater theme.

**Specifications:**
- Various sized translucent bubbles
- Random generation from bottom of screen
- Gentle upward floating motion
- Subtle path variation
- Occasional size pulsing
- 30% opacity base with variation

### 4. Light Rays Effect

Creates a serene atmosphere suggesting sunlight filtering through water.

**Specifications:**
- Emanates from top of screen
- Soft white/blue color
- 10-20% opacity
- Subtle movement animation
- Varies in intensity based on screen
- More prominent during active meditation

### 5. Session Duration Cards

The primary interactive elements on the home screen.

**Specifications:**
- Rounded rectangle cards (16dp radius)
- Gradient background from deep teal to aqua
- Large duration number (centered)
- "Minutes" label below number
- Elevation effect (4dp)
- Ripple effect on touch
- Equal sizing in 2x2 grid layout

### 6. Navigation Bar

Provides consistent navigation throughout the app.

**Specifications:**
- Fixed at bottom of screen
- Three primary icons:
  - Home (house icon)
  - History (calendar icon)
  - Settings (gear icon)
- Active icon highlighted in aqua
- Inactive icons in light aqua
- Subtle bounce animation on selection
- Labels below icons

## Animations and Transitions

### 1. Screen Transitions

**Water Ripple Transition:**
- Circular ripple effect emanating from touch point
- 0.3 second duration
- Easing: FastOutSlowInEasing

**Fade Transitions:**
- Used for session start/end
- 0.5 second duration
- Easing: LinearOutSlowInEasing

### 2. Interactive Animations

**Button Feedback:**
- Subtle scale (1.05x) on press
- Ripple effect from touch point
- Color shift to lighter shade

**Breathing Circle Animation:**
- Smooth size interpolation
- Synchronized with breathing pattern
- Subtle color shift during phases
- Gentle glow pulsing

### 3. Ambient Animations

**Bubble Float:**
- Continuous generation
- Random size variation (5-20dp)
- Variable speed (slower for larger bubbles)
- Path variation with slight horizontal drift
- Z-order variation (some bubbles appear in front of elements)

**Light Ray Movement:**
- Slow oscillation
- Subtle intensity variation
- 30-second complete cycle
- Synchronized across rays with phase difference

## Responsive Design Considerations

### Phone (Small Screens)
- Vertical layout prioritization
- Session cards in 2x2 grid
- Reduced animation complexity
- Buddha figure scaled to 30% of screen height
- Bottom navigation with labels

### Tablet (Medium Screens)
- Horizontal layout for home screen (Buddha left, sessions right)
- Session cards in horizontal row
- Full animation complexity
- Buddha figure scaled to 40% of screen height
- Bottom navigation with labels

### Accessibility Considerations
- High contrast mode option
- Scalable text (supports system font size)
- Touch targets minimum 48dp
- Color choices tested for color blindness compatibility
- TalkBack support with meaningful content descriptions

## Implementation Notes

### Animation Performance
- Use hardware acceleration
- Reduce particle count on lower-end devices
- Implement battery-aware animation scaling
- Cache complex drawables
- Use Jetpack Compose animations for optimal performance

### Asset Management
- Vector drawables for UI elements when possible
- Optimized PNG assets for complex illustrations
- Multiple density support (mdpi through xxxhdpi)
- Asset preloading for critical screens

## Screen Mockups

The following sections would include detailed mockups for each screen. In an actual implementation, these would be image files showing the exact visual design of each screen.

1. Splash Screen Mockup
2. Onboarding Screen Mockup
3. Home Screen Mockup
4. Session Screen Mockup
5. Session Complete Screen Mockup
6. History Screen Mockup
7. Settings Screen Mockup
