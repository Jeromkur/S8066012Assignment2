# Animal Explorer — NIT3213 Final Assignment

Student ID: S8066012  
Project: S8066012Assignment2  
Package: com.vu.s8066012assignment2

## Overview

Animal Explorer is an Android application written in Kotlin.
Users log in through the assignment API, browse animal summaries,
and select an animal to view its full details.

## Features

- Login using a student ID and password.
- Input validation for empty login fields.
- API authentication and retrieval of the returned keypass.
- Animal dashboard using RecyclerView.
- Animal count displayed from the API response.
- Details screen showing all animal properties, including description.
- Loading indicators, error messages, and dashboard retry.
- ViewModels retain screen state during configuration changes.

Dashboard cards show animal summaries without the description.
The full description appears on the Details screen.

## Architecture and Organisation

The app uses Activities, ViewModels, repositories, and Hilt
dependency injection to separate responsibilities.

Location and Purpose 

- data/network : Retrofit API interface and request/response models 
- data/repository:Authentication and dashboard data access          
- di :  Hilt module providing networking dependencies 
- ui/login     :  Login ViewModel and UI states 
- ui/dashboard :  Dashboard Activity, ViewModel, and RecyclerView adapter 
- ui/details   :  Animal Details Activity |
- MainActivity.kt : Login screen and dashboard navigation 
- AssignmentApplication.kt  : Hilt application entry point 
- res/layout    : XML screen layouts and animal card layout 
- res/values   : String resources, colours, and themes 
- src/test   : Local unit tests

Activities display UI state and handle user interactions.
ViewModels manage loading, success, and error states.
Repositories call the API service.
Hilt provides the networking objects and repository dependencies.

## Dependencies

- Hilt 2.60.1 — dependency injection.
- KSP — annotation processing for Hilt.
- Retrofit 3.0.0 — HTTP API requests.
- Retrofit Moshi converter 3.0.0 — JSON conversion.
- Moshi Kotlin 1.15.2 — Kotlin data model support.
- RecyclerView 1.4.0 — scrolling animal list.
- AndroidX Lifecycle and Kotlin coroutines — ViewModel state and asynchronous work.
- Material Components — UI controls and styling.
- JUnit 4 — unit testing.
- kotlinx-coroutines-test 1.11.0 — controlled coroutine execution in tests.

Dependency declarations are in app/build.gradle.kts and
gradle/libs.versions.toml.

## API

Base URL:

https://nit3213apinew.onrender.com/

Endpoints:

- POST /footscray/auth — submits username and password and returns a keypass.
- GET /dashboard/{keypass} — retrieves entities and entityTotal.

The app passes the keypass returned by login into the dashboard
request rather than hardcoding the topic.

Enter your assigned student ID without the leading "s" and the
password specified by the lecturer. Credentials are entered through
the login screen.

The live app requires internet access.

## Build and Run

1. Clone or download this repository.
2. Open the project root in Android Studio.
3. Use an Android Studio version compatible with the project's
   Android Gradle Plugin.
4. Install Android SDK Platform 37 through SDK Manager.
5. Allow Gradle Sync to complete and download dependencies.
6. Create an emulator or connect a device running Android 7.0
   (API 24) or newer.
7. Select the app run configuration and click Run.

Build configuration:

- Minimum SDK: 24
- Compile SDK: 37
- Target SDK: 37
- Java source and target compatibility: 17

Build tooling: Android Gradle Plugin 9.3.2 and Gradle 9.5.0.
The Gradle daemon is configured to use JDK 25.
Java source and target compatibility remain 17.

Use Android Studio's compatible bundled Gradle JDK and the
Gradle wrapper included in this repository.

To build a debug APK from the project root on Windows:

```powershell
.\gradlew.bat assembleDebug
```

The APK is generated at:

app/build/outputs/apk/debug/app-debug.apk

## Unit Tests

Three project-specific unit tests cover the ViewModels:

 Test and Expected behaviour 

 successfulLoginReturnsExpectedKeypass = Login produces the expected keypass and sends the entered credentials |
 networkFailureProducesFriendlyError = A network exception produces a friendly login error state |
 successfulLoadingReturnsAnimalsAndTotal = Dashboard loading produces the expected animal list and total and passes the keypass |

Tests use fake ApiService implementations with real repositories.
They do not contact the live API or require real credentials.
Coroutine test dispatchers control asynchronous execution.

All three tests passed during development.

Run the two test classes from their green run icons in Android Studio,
or run all local debug unit tests from the project root:

```powershell
.\gradlew.bat testDebugUnitTest
```

On macOS or Linux:

```bash
./gradlew testDebugUnitTest
```

The HTML test report is generated at:

app/build/reports/tests/testDebugUnitTest/index.html

## Manual Verification

The app was run on an API 27 emulator. Checks included:

- Successful login and display of the animal dashboard.
- Display of seven animals from the API.
- Opening the animal Details screen.
- Display of a login error after an unsuccessful attempt.
- Dashboard display after rotating the emulator.

## Limitations

Live login and dashboard requests depend on API availability and
internet connectivity. The app does not provide persistent offline
storage. The unit tests verify selected ViewModel behaviours and do
not replace full UI or live API testing.