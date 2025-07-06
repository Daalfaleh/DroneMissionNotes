🚁 Drone Mission Notes
<div align="center">
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Kotlin-blue.svg" alt="Language">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg" alt="UI Framework">
  <img src="https://img.shields.io/badge/Architecture-MVVM-red.svg" alt="Architecture">
  <img src="https://img.shields.io/badge/Min%20SDK-24-yellow.svg" alt="Min SDK">
  <img src="https://img.shields.io/badge/License-MIT-green.svg" alt="License">
</div>
<div align="center">
  <h3>📱 A Modern Android App for Drone Operators</h3>
  <p>Efficiently record, manage, and review drone mission notes with a beautiful Material 3 interface</p>
</div>

🌟 Features
✅ Core Functionality

📝 Mission Note Management - Create, edit, and delete detailed mission records
📍 GPS Integration - Auto-fill location coordinates or enter manually
🔍 Smart Search - Find notes by title, description, or location
📌 Pin Important Notes - Keep critical missions at the top
🎨 Modern UI - Beautiful Material 3 design with Jetpack Compose

🏆 Advanced Features

🌓 Dark/Light Themes - Automatic system theme with manual override
⚡ Real-time Updates - Instant UI updates with reactive programming
📱 Responsive Design - Optimized for all screen sizes
🔒 Offline First - No internet required for core functionality
⚙️ Settings Management - Theme control and data management options


📱 Screenshots
Main ScreenAdd MissionDark ThemeSettingsClean list view with searchIntuitive form with GPSEye-friendly dark modeSimple settings panel

🛠 Tech Stack
CategoryTechnologyVersionLanguageKotlin1.9.22UI FrameworkJetpack ComposeBOM 2024.02.00ArchitectureMVVM + Repository Pattern-Data StorageIn-Memory StateFlow-NavigationNavigation Compose2.7.6LocationAndroid LocationManager-Design SystemMaterial 3Dynamic ThemingAsync ProgrammingCoroutines + Flow1.7.3

🏗 Architecture
📦 Drone Mission Notes
├── 🎨 UI Layer (Jetpack Compose)
│   ├── 📄 Screens (List, Add/Edit, Settings)
│   ├── 🧩 Components (Cards, Search, Location Picker)
│   └── 🎭 Themes (Material 3, Dark/Light)
├── 🧠 Business Logic (ViewModels)
│   ├── 📊 State Management
│   ├── 🔄 Data Flow
│   └── 🔍 Search Logic
├── 💾 Data Layer
│   ├── 📦 Repository Pattern
│   ├── 🗃️ Data Storage
│   └── 🔗 Data Models
└── 🛠 Utils
    ├── 📍 Location Helper
    └── 📅 Date Utilities
MVVM Pattern Benefits:

🔒 Separation of Concerns - Clean, maintainable code
🔄 Reactive UI - Automatic updates with state changes
🧪 Testable - Easy unit testing of business logic
📈 Scalable - Easy to add new features


🚀 Getting Started
Prerequisites

✅ Android Studio Hedgehog (2023.1.1) or later
✅ Android SDK 24+ (minimum) / 34+ (target)
✅ Kotlin 1.9.0+
✅ JDK 8+

Installation

Clone the repository
bashgit clone https://github.com/Daalfaleh/DroneMissionNotes.git
cd DroneMissionNotes

Open in Android Studio

Launch Android Studio
Select "Open an existing project"
Navigate to the cloned directory


Sync dependencies
bash./gradlew build

Run the app

Connect an Android device (API 24+) or start an emulator
Click Run (▶️) or press Shift + F10



Permissions
The app requires these permissions for full functionality:

ACCESS_FINE_LOCATION - For precise GPS coordinates
ACCESS_COARSE_LOCATION - For network-based location


📖 How to Use
Creating a Mission Note

Tap the floating action button (+)
Enter mission title (required)
Add detailed description
Set location (GPS auto-fill or manual entry)
Save the mission

Managing Notes

📌 Pin Notes - Tap the star icon to pin important missions
✏️ Edit - Tap any note to modify details
🗑️ Delete - Tap the delete icon with confirmation
🔍 Search - Use the search bar to find specific missions

App Settings

🌓 Theme Toggle - Switch between light and dark modes
📍 Location Settings - Manage GPS permissions
🗑️ Data Management - Clear all notes option


🎯 Key Features Explained
📌 Smart Pin System
kotlin// Pinned notes automatically sort to top
notes.sortedWith(
    compareByDescending<MissionNote> { it.isPinned }
        .thenByDescending { it.timestamp }
)
🔍 Intelligent Search
kotlin// Search across multiple fields
fun doesMatchSearchQuery(query: String): Boolean {
    return listOf(title, description, location)
        .any { it.contains(query, ignoreCase = true) }
}
📍 GPS Integration
kotlin// One-tap location capture
suspend fun getCurrentLocation(): Location? {
    return locationManager.getLastKnownLocation(GPS_PROVIDER)
}

🧪 Testing
Manual Testing Checklist

✅ Create mission notes
✅ Edit existing notes
✅ Delete notes with confirmation
✅ Pin/unpin functionality
✅ Search functionality
✅ GPS location capture
✅ Theme switching
✅ Settings management

Running Tests
bash# Unit tests
./gradlew test

# UI tests (with connected device)
./gradlew connectedAndroidTest

📁 Project Structure
app/src/main/java/com/droneapp/missionnotes/
├── data/
│   ├── database/
│   │   ├── SimpleDataStore.kt
│   │   └── entities/MissionNote.kt
│   └── repository/
│       └── MissionRepository.kt
├── ui/
│   ├── components/
│   │   ├── MissionNoteCard.kt
│   │   ├── SearchBar.kt
│   │   └── LocationPicker.kt
│   ├── screens/
│   │   ├── MissionListScreen.kt
│   │   ├── AddEditMissionScreen.kt
│   │   └── SettingsScreen.kt
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── viewmodel/
│       ├── MissionViewModel.kt
│       └── MissionViewModelFactory.kt
├── utils/
│   ├── LocationHelper.kt
│   └── DateUtils.kt
└── MainActivity.kt


Technical Improvements

🔄 Offline Sync - Queue operations for connectivity
🏷️ Tagging System - Categorize missions
📱 Widget Support - Home screen quick access
🔔 Notifications - Mission reminders
