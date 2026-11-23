# android-app

A simple Android app written in Kotlin that shows the text messages (SMS) in your phone's inbox.

On first launch it asks for permission to read SMS. Messages are only read and shown on the phone; nothing is sent anywhere.

> **Note:** Google Play only allows the SMS permission for apps that are the phone's default SMS app or fit a few approved uses. Installing the APK directly (sideloading) works fine.

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
