# Wallty Android App

Mobile client for the Virtual Wallet system.

## 📱 Overview
Wallty is an Android application that allows users to:
- Authenticate (login/logout)
- View their wallets
- Inspect wallet details
- Interact with backend services

## 🏗️ Architecture
The project follows a layered structure:

- **data/** → API + models
- **ui/** → Screens and components (Jetpack Compose)
- **viewmodel/** → Business logic
- **utils/** → Shared helpers

## ⚙️ Tech Stack
- Kotlin
- Jetpack Compose
- Retrofit
- MVVM Architecture

## 🔗 Backend
This app communicates with the Virtual Wallet backend API.

## 🚀 Getting Started

1. Clone the repo
2. Open in Android Studio
3. Set your `local.properties` (SDK path)
4. Run the app

## 📌 Notes
- `local.properties` is not tracked (machine-specific)
- Backend must be running for full functionality