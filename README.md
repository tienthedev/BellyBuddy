# BellyBuddy - Android Health & Wellness Tracker

BellyBuddy is an Android application built to demonstrate modern mobile development practices. It serves as a foundation for a personal health tracker, allowing users to register, log in, and view user-specific data from a local database. The project emphasizes clean architecture, a reactive UI, and robust data persistence.



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


