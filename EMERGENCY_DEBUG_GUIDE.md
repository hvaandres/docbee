# Emergency Button Debug Guide

## Issues Identified

1. **No Triple-Press Logic**: The emergency button was calling `sendEmergencyAlert()` on every single press, not requiring 3 presses
2. **Missing Debug Logging**: Insufficient logging throughout the emergency alert flow made it difficult to diagnose issues
3. **No Firebase Function Logging**: The backend functions had minimal logging

## Changes Made

### 1. Added Triple-Press Logic to EmergencyButton.kt

**Location**: `composeApp/src/commonMain/kotlin/com/docbee/tealapp/ui/EmergencyButton.kt`

**Changes**:
- Added press counter state that tracks button presses
- Implemented 2-second reset timer (if user doesn't complete 3 presses within 2 seconds, counter resets)
- Added visual feedback showing press count (e.g., "1/3", "2/3")
- Updated button text to show remaining presses needed
- Added comprehensive debug logging for each press

**Debug Output**:
```
[EmergencyButton] Button pressed! Current count: 0
[EmergencyButton] Press count now: 1
[EmergencyButton] Press 1/3 - waiting for more presses...
[EmergencyButton] Button pressed! Current count: 1
[EmergencyButton] Press count now: 2
[EmergencyButton] Press 2/3 - waiting for more presses...
[EmergencyButton] Button pressed! Current count: 2
[EmergencyButton] Press count now: 3
[EmergencyButton] Triple press detected! Triggering emergency alert.
```

### 2. Enhanced EmergencyViewModel.kt Logging

**Location**: `composeApp/src/commonMain/kotlin/com/docbee/tealapp/viewmodel/EmergencyViewModel.kt`

**Existing Debug Logs** (already present):
- Authentication check
- Rate limit check
- Location retrieval
- Alert sending

### 3. Enhanced EmergencyRepository.kt Logging

**Location**: `composeApp/src/commonMain/kotlin/com/docbee/tealapp/data/EmergencyRepository.kt`

**Added Debug Output**:
```
[EmergencyRepository] sendEmergencyAlert called
[EmergencyRepository] Location: lat=37.7749, lng=-122.4194
[EmergencyRepository] Username: John Doe
[EmergencyRepository] Request payload: {location={latitude=37.7749, longitude=-122.4194}, userName=John Doe, timestamp=1234567890}
[EmergencyRepository] Calling sendEmergencySMS function...
[EmergencyRepository] Function call returned successfully
[EmergencyRepository] Response data: {success=true, message=..., successCount=2, failureCount=0, ...}
[EmergencyRepository] Alert sent successfully: 2 succeeded, 0 failed
```

### 4. Comprehensive Firebase Functions Logging

**Location**: `functions/index.js`

**Added to `sendEmergencySMS` function**:
```javascript
[sendEmergencySMS] Function called
[sendEmergencySMS] Data received: {...}
[sendEmergencySMS] Context auth: authenticated
[sendEmergencySMS] User ID: abc123
[sendEmergencySMS] Location validated: {latitude: 37.7749, longitude: -122.4194}
[sendEmergencySMS] Fetching user document from Firestore...
[sendEmergencySMS] User document exists: true
[sendEmergencySMS] User data: {...}
[sendEmergencySMS] Emergency contacts count: 2
[sendEmergencySMS] Preparing to send SMS to 2 contacts
[sendEmergencySMS] Twilio configuration check:
[sendEmergencySMS] - Account SID: SET
[sendEmergencySMS] - Auth Token: SET
[sendEmergencySMS] - Phone Number: +15551234567
[sendEmergencySMS] Processing contact 1: Mom
[sendEmergencySMS] Sending SMS to Mom at +15559876543
[sendEmergencySMS] SMS sent successfully to Mom, SID: SM...
[sendEmergencySMS] Waiting for all SMS sends to complete...
[sendEmergencySMS] All SMS sends completed. Results: [...]
[sendEmergencySMS] Logging emergency event to Firestore...
[sendEmergencySMS] Emergency event logged successfully
[sendEmergencySMS] Summary: 2 successful, 0 failed
[sendEmergencySMS] Returning response: {...}
```

**Added to `checkEmergencyRateLimit` function**:
```javascript
[checkEmergencyRateLimit] Function called
[checkEmergencyRateLimit] Context auth: authenticated
[checkEmergencyRateLimit] User ID: abc123
[checkEmergencyRateLimit] Checking recent alerts from Firestore...
[checkEmergencyRateLimit] Recent alerts found: true
[checkEmergencyRateLimit] Last alert was 3 minutes ago
[checkEmergencyRateLimit] Rate limit active - 2 minutes remaining
```

## How to Debug

### 1. Start Firebase Emulator with Logging
```bash
cd functions
npm run serve
```

This will show all Firebase function logs in real-time.

### 2. Watch Android Logcat
```bash
adb logcat | grep -E "Emergency|sendEmergency|checkEmergency"
```

### 3. Watch iOS Console
In Xcode: View → Debug Area → Activate Console, then filter for "Emergency"

### 4. Test the Flow

1. **Press the button once** - Should see:
   - `[EmergencyButton] Press 1/3`
   - Button shows "1/3"
   - Text says "Press 2 more time(s)"

2. **Press the button twice more** - Should see:
   - `[EmergencyButton] Press 2/3`
   - `[EmergencyButton] Press 3/3`
   - `[EmergencyButton] Triple press detected!`
   - `[EmergencyViewModel] sendEmergencyAlert called`
   - Full flow through repository and Firebase functions

3. **Wait 2+ seconds between presses** - Should see:
   - `[EmergencyButton] Reset timeout - counter reset to 0`

## Troubleshooting

### Button not responding
- Check for `[EmergencyButton] Button pressed!` logs
- If missing, there's a UI event handling issue

### Triple press not triggering
- Verify counter increments: `[EmergencyButton] Press count now: X`
- Check if counter is resetting too early

### Firebase functions not called
- Check `[EmergencyViewModel]` logs for authentication or rate limit issues
- Verify `[EmergencyRepository] Calling sendEmergencySMS function...` appears

### Firebase functions not responding
- Check emulator is running: `lsof -i:5001`
- Look for `[sendEmergencySMS] Function called` in emulator logs
- If missing, there's a connection issue between app and emulator

### SMS not sending
- Check Twilio configuration logs in function output
- Verify `[sendEmergencySMS] - Account SID: SET` shows "SET" not "NOT SET"
- Look for individual contact sending logs

## Next Steps

Once debugging is complete and the issue is found:
1. Remove excessive debug logs (or reduce to warn/error level)
2. Add proper error handling based on findings
3. Update user-facing error messages as needed
