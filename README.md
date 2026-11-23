# android-app

A simple starter Android app written in Kotlin. It shows a greeting and a button that counts taps.

## Build the APK

```bash
./gradlew assembleDebug
```

The APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

Every push to `main` also builds the APK on GitHub Actions. You can download it from the run's **Artifacts** section.

## Install on a phone

Copy `app-debug.apk` to your phone and open it. You'll need to allow installing apps from unknown sources. Or, with USB debugging enabled:

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```
