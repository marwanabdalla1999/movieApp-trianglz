# 🎬 Movie App (Jetpack Compose + MVI + Paging 3 + Room + Retrofit)

A modern Android movie app built with Jetpack Compose using the MVI architecture pattern. It displays a paginated list of popular movies and supports search with debounce. Movie details can be viewed by clicking on any movie item.

---

## ✨ Features

- Jetpack Compose UI with MVI architecture.
- Movie listing screen with:
  - Infinite scrolling using Paging 3.
  - Search functionality using debounce.
  - Caching with Room database.
- Movie details screen with full movie info.
- Offline-first support using Paging 3 + RemoteMediator.
- Clean architecture with clear domain, data, and presentation layers.

---

## 📱 Screens

### 1. Movie Listing Screen

- Displays a paginated list of popular movies.
- Supports search with real-time suggestions using debounce.
- Efficiently loads data from:
  - Cache (Room) when offline.
  - Remote source (Retrofit) when online.
- Handles loading states, errors, and empty search results gracefully.

### 2. Movie Details Screen

- Navigates to this screen upon clicking any movie in the list.
- Shows:
  - Movie poster.
  - Title and release date.
  - Overview/description.
  - Average vote rating.

---

## 🧠 Architecture

This app follows the **MVI (Model-View-Intent)** architecture pattern:

View (Jetpack Compose)
↕ (UI Events, UI State)
ViewModel (State Holder, Reducer, Intent Processor)
↕ (StateFlow, Intent Channels)
UseCases / Interactors
↕
Repositories (Interface)
↕
Remote & Local Data Sources (Retrofit, Room)


---

## 🧱 Data Model

`AppMovieModel` is the UI model used to display movie details in the app:

```kotlin
data class AppMovieModel(
    val id: Int,
    val title: String,
    val poster: String,
    val releaseDate: String,
    val overview: String,
    val voteAverage: Double
)
```

🔌 Tech Stack
UI: Jetpack Compose
Architecture: MVI + Clean Architecture
Paging: Paging 3 with RemoteMediator
Networking: Retrofit + OkHttp
Database: Room
Coroutines: Kotlin Flow & suspend functions
DI: Hilt (optional, if used)


🔍 Search with Debounce
Uses debounce operator on a MutableStateFlow to handle search input.
Reduces unnecessary API calls while typing.
Automatically updates the paginated list with search results.


🔄 Paging Flow
Room serves cached movies instantly.
RemoteMediator triggers API fetch if cache is empty or invalid.
API response is stored in Room using database.withTransaction {}.
UI collects from Pager.flow and displays paged data.

## Project Structure
```kotlin
project-root/
├── core/
│   ├── cache/       # Room DB, DAOs, entities
│   ├── network/     # Retrofit API services and network utils
│   └── ui/          # Shared UI components (e.g., AsyncImageView)
├── domain/
│   └── movie/       # Use cases and repository interfaces
├── data/
│   └── movie/       # Remote & local data sources, paging, repo implementation
└── features/
    └── movie/
        ├── movieList/      # Movie listing screen with search
        └── movieDetails/   # Movie details screen
```



## 🚀 How to Run

Clone the repo.
Add your API key.
Build and run the app.

