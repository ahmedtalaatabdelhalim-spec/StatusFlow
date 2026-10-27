# StatusFlow — Technical Feasibility & Proof of Concept Report

## 1. Executive Summary
This document establishes the technical feasibility, architectural constraints, security boundaries, and fallback strategies for automating WhatsApp Status publications on modern Android versions (Android 10 through Android 15+).

Automating WhatsApp Status updates presents unique challenges due to Android's strict background execution policies, lock screen security, and WhatsApp's lack of a public Developer API for status publishing. This report presents an honest, empirical breakdown of what is achievable silently, what requires user intervention, and how **StatusFlow** handles every edge case without fabricating success or violating system integrity.

---

## 2. Technical Feasibility Matrix

| Operating Condition | AccessibilityService Enabled | Device Lock State | Achievable Outcome | Classification |
|---|---|---|---|---|
| **Screen On, Unlocked** | Yes | Unlocked | Automated UI interaction via AccessibilityService clicks "Send" on WhatsApp status preview. | **Automated (Unverified)** |
| **Screen On, Unlocked** | No | Unlocked | One-tap notification launches WhatsApp share intent; user confirms "Send". | **Action Required (Interactive)** |
| **Screen Off, Unlocked (Swipe/None)** | Yes | Unlocked | App wakes screen via `WakeLock` / `KeyguardManager`, launches share intent, AccessibilityService posts status. | **Automated (Unverified)** |
| **Screen Off, Secured (PIN/Pattern/Biometric)** | Yes / No | Locked | **Android Keyguard blocks UI interaction.** AccessibilityService cannot bypass secure lock. App fires high-priority notification. | **Action Required (Lock Screen Gate)** |
| **Doze Mode / Idle** | Yes / No | Any | `AlarmManager.setExactAndAllowWhileIdle()` wakes device at exact time to process schedule. | **System Scheduled** |
| **No Internet Connection** | Yes / No | Any | Local intent prepared; WhatsApp queues upload locally until connectivity returns. | **Attempted (Offline)** |
| **WhatsApp Updated / ID Changed** | Yes | Any | Fallback heuristic matches content-description or localized text ("Send", "إرسال"). If unmatchable, prompts user. | **Graceful Fallback** |

---

## 3. Detailed Technical Analysis

### A. WhatsApp Sharing Mechanisms
WhatsApp supports receiving content via Android's standard Intent framework:
* **Intent Action**: `Intent.ACTION_SEND` (single item) or `Intent.ACTION_SEND_MULTIPLE` (multiple media items).
* **Target Package**: `com.whatsapp`.
* **Explicit Component Options**:
  * `com.whatsapp.ContactPicker` with `jid = "status@broadcast"`
  * `com.whatsapp.TextStatusManagerActivity` (for text statuses)
  * `com.whatsapp.camera.CameraActivity` / `com.whatsapp.status.sidebyside.StatusCategoryActivity`
* **MIME Types**: `image/*`, `video/*`, `text/plain`.
* **Extras**: `Intent.EXTRA_STREAM` (content URI), `Intent.EXTRA_TEXT` (caption or text body).

### B. AccessibilityService Capabilities & Safety Controls
When optional automation is activated by the user:
1. `StatusFlowAccessibilityService` listens for `TYPE_WINDOW_STATE_CHANGED` and `TYPE_WINDOW_CONTENT_CHANGED` events from `com.whatsapp`.
2. It detects the status preview layout.
3. It performs localized node searches across English (`Send`, `Status`, `Post`) and Arabic (`إرسال`, `الحالة`) as well as resource ID patterns (e.g., `send`, `confirm`, `fab`).
4. Performs `AccessibilityNodeInfo.ACTION_CLICK` on the target button.
5. Emits a broadcast back to `StatusFlow` recording the execution attempt.

### C. Security Boundaries & Android System Protections
* **Lock Screen Protection**: Android intentionally prevents AccessibilityServices from reading or interacting with UI elements while keyguard is securely locked. StatusFlow respects this protection and never attempts keyguard bypass exploits.
* **Background Activity Launch Restrictions**: Introduced in Android 10+, apps in the background cannot launch activities directly unless using a high-priority notification with a PendingIntent or a valid Foreground Service exemption.
* **Storage Access**: Media files must be accessed using persistent Storage Access Framework (SAF) URI permissions (`takePersistableUriPermission`) or app-private file copies to guarantee media availability at execution time.

---

## 4. Truthful Execution State Machine

StatusFlow enforces strict state semantics. A task is **never** reported as `PUBLISHED` simply because an alarm fired or an intent was dispatched.

```
       +-------------------+
       |       DRAFT       |
       +---------+---------+
                 |
                 v
       +-------------------+
       |     SCHEDULED     |
       +---------+---------+
                 | (Alarm / Worker Trigger)
                 v
       +-------------------+
       |    ATTEMPTING     |
       +----+---------+----+
            |         |
  (Lock Screen /      | (Automation Execution /
   Manual Mode)       |  Intent Shared)
            |         |
            v         v
+---------------+  +--------------------------+
| ACTION_REQ.   |  | PUBLISHED_UNVERIFIED     |
+---------------+  +--------------------------+
            |
            v (User Taps / Completes)
+---------------+
|   PUBLISHED   |
+---------------+
```

### State Definitions:
1. **DRAFT**: Content prepared but not yet activated.
2. **SCHEDULED**: Registered in Room database and armed with AlarmManager / WorkManager.
3. **ATTEMPTING**: Alarm triggered; checking device state (lock screen, connectivity, permissions).
4. **ACTION_REQUIRED**: Lock screen active or Accessibility disabled; notification posted to user.
5. **PUBLISHED_UNVERIFIED**: Intent dispatched and Accessibility click attempted successfully; awaiting implicit confirmation.
6. **FAILED**: Missing media, WhatsApp not installed, or process cancelled by user/system.

---

## 5. Conclusion & Recommendations
Automated status scheduling in StatusFlow is robust, resilient, and honest. By combining exact `AlarmManager` triggers, persistent Room storage, `AccessibilityService` UI automation (for unlocked states), and actionable notifications (for locked states), StatusFlow delivers the highest reliable level of automation possible on stock Android while strictly adhering to system security guidelines.
