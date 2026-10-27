# StatusFlow — Professional WhatsApp Status Scheduler for Android

**StatusFlow** is a modern, privacy-focused Android application designed to schedule and automate WhatsApp Status updates (Images, Videos, and Text) with exact alarm execution, recurring daily/weekly rules, and intelligent fallback notifications.

---

## Key Features

* **Flexible Scheduling**: Schedule one-time or recurring (daily, specific days of the week) status updates.
* **Media Support**: Photos, videos, and custom captions with persistent URI permissions.
* **Background Reliability**: Combines `AlarmManager.setExactAndAllowWhileIdle()`, `WorkManager`, and `BootReceiver` for reliable execution.
* **Optional Automation**: Utilizes an `AccessibilityService` to automate status publication when the screen is unlocked.
* **Privacy & Security**: All schedules, media paths, and logs are stored locally on the device in a SQLite Room database. No external cloud or third-party servers required.
* **Modern Material 3 UI**: Polished Compose interface supporting Light/Dark themes and Arabic RTL / English LTR layouts.

---

## Download & Installation

1. Go to the **Actions** tab of this repository.
2. Click on the latest successful workflow run.
3. Scroll down to **Artifacts** and download `StatusFlow-debug-apk`.
4. Extract the ZIP file and install the `app-debug.apk` on your Android device.

---

## Documentation

* [Feasibility Report](docs/FEASIBILITY_REPORT.md)
* [Testing Plan](docs/TESTING.md)

---

## License

Distributed under the MIT License.
