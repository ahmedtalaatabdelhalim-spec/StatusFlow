# StatusFlow — Testing Strategy & Manual Test Plan

## 1. Overview
This document outlines the testing strategy for **StatusFlow**, covering automated unit/integration tests and a step-by-step manual test plan for real WhatsApp Status publication under various Android conditions.

---

## 2. Automated Tests

### A. Unit Tests (`app/src/test/java/com/statusflow/app/`)
* **Recurrence Calculation**: Tests calculation of next alarm execution timestamp for daily, weekly, and custom day schedules.
* **State Machine Logic**: Validates state transitions (`DRAFT` -> `SCHEDULED` -> `ATTEMPTING` -> `PUBLISHED` / `FAILED` / `ACTION_REQUIRED`).
* **Database DAO Logic**: Tests Room database insertions, queries, updates, and deletions using memory databases.

### B. Instrumentation Tests (`app/src/androidTest/java/com/statusflow/app/`)
* **Compose UI Components**: Tests UI rendering of cards, buttons, and state indicators across dark/light themes and English/Arabic locales.

---

## 3. Manual Test Plan for Live WhatsApp Publication

### Test Environment Setup
* Device: Physical Android Phone running Android 10, 12, 14, or 15.
* WhatsApp Installed and Logged In.
* StatusFlow APK installed.

### Test Matrix

| Test Case | Condition | Expected Behavior | Verification Step |
|---|---|---|---|
| **TC-01** | Screen On, Unlocked, Accessibility On | StatusFlow wakes alarm, opens WhatsApp, Accessibility clicks "Send". | Status appears on user's WhatsApp Status feed. |
| **TC-02** | Screen Off, Unlocked | Device screen turns on, shares media to WhatsApp, posts status automatically. | Status published without physical touch. |
| **TC-03** | Screen Off, Secured (PIN/Fingerprint) | High-priority notification fires: "Your scheduled status is ready! Tap to post now." | User taps notification, unlocks phone, status shares directly. |
| **TC-04** | Device Reboot | AlarmScheduler re-arms all active `SCHEDULED` items automatically via `BootReceiver`. | Verification via StatusFlow Dashboard list. |
| **TC-05** | No Internet Connection | Status shared to WhatsApp; WhatsApp holds upload until internet restores. | Execution state recorded as `PUBLISHED_UNVERIFIED`. |
| **TC-06** | Recurring Daily Schedule | Next execution date updates to T+24h automatically after current execution completes. | Verify next run time on Dashboard. |
