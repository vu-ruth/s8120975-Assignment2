# s8120975Assignment2

Android app for NIT3213. It signs in to the Footscray class API, lists photographic techniques, and opens a detail screen for the one you tap.

- Student: Ruth (s8120975)
- Class location: Footscray
- Android Studio project name: `s8120975Assignment2`

## Open and run

1. Install Android Studio with Android SDK 34 and JDK 17.
2. Open the `s8120975Assignment2` folder (the folder that contains `settings.gradle.kts`), not the parent folder.
3. Let Gradle sync. If it asks for the SDK path, point it at your Android SDK.
4. Run the `app` configuration on an emulator or a device running Android 8 (API 26) or newer.

From a terminal, in this folder:

```bat
gradlew.bat assembleDebug
gradlew.bat test
```

On macOS or Linux, use `./gradlew` instead of `gradlew.bat`.

The first sign-in can take up to a minute. The API host sleeps when nobody has used it, and the app waits up to 60 seconds for it to wake.

## Sign in

| Field | Value |
| --- | --- |
| Username | `8120975` (or `s8120975`) |
| Password | `Ruth` |

The password is case sensitive. A wrong student ID or first name stays on the sign-in screen and shows an error. After a successful sign-in the app opens the technique catalogue. Press back on a detail screen to return to that list. Press back on the catalogue to leave the app.

## What each screen does

- **Sign in** sends a POST request to `https://nit3213apinew.onrender.com/footscray/auth` with the student ID and first name. The response `keypass` is `photography`.
- **Catalogue** sends a GET request to `/dashboard/photography` and shows each technique in a RecyclerView. Rows show the name, year, subject, photographer, and equipment. They do not show the description.
- **Detail** shows every field for the tapped technique, including the description.

The catalogue heading uses `entityTotal` from the API (7 techniques).

## Project structure

Dependency injection is Koin. JSON is read with Moshi.

- `di` starts Koin and wires the API, the gateways, and the ViewModels.
- `net` is Retrofit and the JSON bodies.
- `model` is the technique and the student-ID formatter.
- `repo` is the sign-in and catalogue gateways.
- `screens/signin`, `screens/catalogue`, and `screens/detail` are the three screens. Each screen has a ViewModel.

## Tests

Unit tests cover the sign-in, catalogue, and detail ViewModels, student ID formatting, and both gateways against a local fake server.

```bat
gradlew.bat test
```

Reports are written to `app/build/reports/tests/testDebugUnitTest/index.html`.
