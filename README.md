# Shale-Namma Pride - Android App Development using GenAI

Child-friendly school transparency Android app for government school headmasters, SDMC members, parents, and community members. Admin users can post daily updates; parents can view them in Kannada or English and submit feedback.

## Tech Stack

- Kotlin
- Jetpack Compose with Material 3
- MVVM style view models
- Firebase Authentication
- Cloud Firestore
- Firebase Storage
- Placeholder GenAI service ready for OpenAI/Gemini integration

## Firebase Setup

1. Create a Firebase project.
2. Add an Android app with package name `com.shalenammapride.app`.
3. Download `google-services.json` and place it at `app/google-services.json`.
4. In `app/build.gradle.kts`, uncomment:

```kotlin
id("com.google.gms.google-services")
```

and add this plugin declaration to the root `build.gradle.kts`:

```kotlin
id("com.google.gms.google-services") version "4.4.0" apply false
```

5. Enable Email/Password sign-in in Firebase Authentication.
6. Create a Cloud Firestore database.
7. Enable Firebase Storage.

The app compiles without credentials so the project can be opened first. Firebase calls need a configured app at runtime.

## Backend Collections

- `users`
- `dailyMeals`
- `facilities`
- `studentStars`
- `feedback`

Daily meals use the date as the document id, which enforces one meal update per day.

## Development Firestore Rules

Use only for development:

```js
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    function signedIn() { return request.auth != null; }
    function isAdmin() {
      return signedIn() &&
        get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role in ['ADMIN', 'HEADMASTER', 'SDMC'];
    }

    match /users/{uid} {
      allow read: if signedIn();
      allow create, update: if signedIn() && request.auth.uid == uid;
    }
    match /dailyMeals/{date} {
      allow read: if signedIn();
      allow create, update, delete: if isAdmin();
    }
    match /facilities/{id} {
      allow read: if signedIn();
      allow create, update, delete: if isAdmin();
    }
    match /studentStars/{id} {
      allow read: if signedIn();
      allow create, update, delete: if isAdmin();
    }
    match /feedback/{id} {
      allow create: if signedIn();
      allow read, update, delete: if isAdmin();
    }
  }
}
```

## Development Storage Rules

```js
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /schoolUpdates/{allPaths=**} {
      allow read: if request.auth != null;
      allow write: if request.auth != null;
    }
  }
}
```

## How Frontend Connects to Backend

Compose screens call view models. View models call repositories. Repositories use Firebase Auth, Firestore, and Storage through `FirebaseProvider`. Image uploads store files in Firebase Storage, then persist download URLs in Firestore documents.

## Run

1. Open this folder in Android Studio.
2. Let Gradle sync.
3. Add `google-services.json` and uncomment the Google Services plugin for a live Firebase backend.
4. Run the `app` configuration on an emulator or Android device.
