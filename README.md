# BellyBuddy - Android Health & Wellness Tracker

BellyBuddy is an Android application built to demonstrate modern mobile development practices. It serves as a foundation for a personal health tracker, allowing users to register, log in, and view user-specific data from a local database. The project emphasizes clean architecture, a reactive UI, and robust data persistence.

![BellyBuddy App Screenshot (Placeholder)](https://i.imgur.com/K5a0q9B.png)

*(Note: You can replace the placeholder image with a real screenshot of your app's dashboard.)*

---

## 🚀 Features

*   **User Authentication**: Secure local registration and login system.
*   **Dynamic Dashboard**: A central screen that displays user-specific data (name, weight) pulled live from the database.
*   **Offline-First Data Persistence**: All user data is stored locally in a Room database, making the app fully functional without an internet connection.
*   **Data Management**: A dedicated screen to view and delete all users in the database, demonstrating full CRUD (Create, Read, Update, Delete) capabilities.
*   **Modern UI**: A fully declarative UI built with Jetpack Compose, featuring a custom bottom navigation bar and Material Design 3 components.

---

## 🛠️ Tech Stack & Architecture

This project is built with a focus on modern, scalable, and maintainable Android development practices.

*   **Core**
    *   **Kotlin**: The official programming language for Android development.
    *   **Coroutines & Flow**: For managing asynchronous operations and handling reactive data streams.

*   **UI**
    *   **Jetpack Compose**: Android's modern, declarative UI toolkit for building native interfaces.
    *   **Material Design 3**: The latest design system for creating beautiful, consistent UIs.
    *   **Compose Navigation**: For navigating between different screens within the app.

*   **Architecture**
    *   **MVVM (Model-View-ViewModel)**: A robust architectural pattern that separates UI logic from business logic.
    *   **Repository Pattern**: To abstract the data source from the rest of the app.
    *   **Dependency Injection (Manual)**: Using a `ViewModelFactory` and a custom `Application` class to provide dependencies like the database and repository.

*   **Data**
    *   **Room Database**: A persistence library that provides an abstraction layer over SQLite for robust local data storage.

*   **Build**
    *   **KSP (Kotlin Symbol Processing)**: For modern, efficient annotation processing, used by Room.
    *   **Gradle with Version Catalogs (`libs.versions.toml`)**: For centralized and maintainable dependency management.

---

## 🏛️ Project Architecture

The application follows a standard clean architecture pattern, ensuring separation of concerns and making the codebase testable and scalable.
+---------------------+ |         UI          |  (Jetpack Compose Screens: Dashboard, Login, etc.) +---------------------+ v +---------------------+ |      ViewModel      |  (UserViewModel: Manages UI state and business logic) +---------------------+ v +---------------------+ |     Repository      |  (UserRepository: Single source of truth for data) +---------------------+ v +---------------------+ |     Data Source     |  (Room Database: DAO interfaces and local SQLite storage) +---------------------+

---

## ⚙️ Setup & Installation

To build and run this project locally, you will need:

1.  **Android Studio** (latest stable version recommended, e.g., Iguana or newer).
2.  An Android Emulator or a physical device running **Android API 26 (Oreo)** or higher.

Follow these steps:

1.  **Clone the repository:**
    