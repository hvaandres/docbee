# DocBee Emergency SMS - Setup Guide

Complete guide to set up and deploy the emergency SMS feature.

## Prerequisites

- Firebase account
- Twilio account
- Node.js 18+ (for Firebase Functions)
- Android Studio (for Android development)
- Xcode (for iOS development)

## Step 1: Firebase Setup

### 1.1 Create Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select existing "DocBee" project
3. Enable Google Analytics (optional)

### 1.2 Enable Authentication

1. In Firebase Console → Authentication → Get Started
2. Enable **Email/Password** provider
3. (Optional) Enable other providers as needed

### 1.3 Enable Firestore

1. In Firebase Console → Firestore Database → Create Database
2. Choose **Start in production mode**
3. Select your region

### 1.4 Get Firebase Config Files

**For Android:**
1. Firebase Console → Project Settings → Your Apps
2. Add Android app (if not exists)
   - Package name: `com.docbee.tealapp`
3. Download `google-services.json`
4. Place in: `composeApp/google-services.json`

**For iOS:**
1. Firebase Console → Project Settings → Your Apps
2. Add iOS app (if not exists)
   - Bundle ID: `com.docbee.tealapp.DocBee`
3. Download `GoogleService-Info.plist`
4. Place in: `iosApp/iosApp/GoogleService-Info.plist`

## Step 2: Twilio Setup

### 2.1 Create Twilio Account

1. Sign up at [twilio.com](https://www.twilio.com/)
2. Get a phone number (Voice & SMS capable)
3. Note your credentials:
   - Account SID
   - Auth Token
   - Phone Number (E.164 format: `+1234567890`)

### 2.2 Verify Test Numbers (Development)

During development, add test phone numbers in Twilio Console:
- Twilio Console → Phone Numbers → Verified Caller IDs

## Step 3: Firebase CLI Setup

### 3.1 Install Firebase CLI

```bash
npm install -g firebase-tools
```

### 3.2 Login to Firebase

```bash
firebase login
```

### 3.3 Initialize Firebase in Project

From project root:

```bash
firebase init
```

Select:
- **Firestore** (Use existing files)
- **Functions** (Use existing files)

Choose your Firebase project from the list.

For Functions:
- Language: **JavaScript**
- ESLint: **Yes**
- Install dependencies: **Yes**

## Step 4: Deploy Cloud Functions

### 4.1 Install Dependencies

```bash
cd functions
npm install
```

### 4.2 Configure Twilio Credentials

```bash
firebase functions:config:set \
  twilio.account_sid="YOUR_ACCOUNT_SID_HERE" \
  twilio.auth_token="YOUR_AUTH_TOKEN_HERE" \
  twilio.phone_number="+1234567890"
```

**Important:** Replace with your actual Twilio credentials!

### 4.3 Deploy Functions

From project root:

```bash
firebase deploy --only functions
```

### 4.4 Deploy Firestore Rules & Indexes

```bash
firebase deploy --only firestore:rules,firestore:indexes
```

## Step 5: Mobile App Setup

### 5.1 Sync Gradle Dependencies

```bash
./gradlew composeApp:dependencies
```

### 5.2 Build Android App

```bash
./gradlew composeApp:assembleDebug
```

### 5.3 Build iOS App

```bash
cd iosApp
xcodegen  # If using xcodegen
open iosApp.xcodeproj
```

Then build in Xcode.

## Step 6: Testing

### 6.1 Create Test User

1. Run the app
2. Sign up with email/password
3. Add emergency contacts in Firestore manually:

```javascript
// In Firebase Console → Firestore
// Collection: users
// Document ID: <your-user-id>
{
  "displayName": "Test User",
  "email": "test@example.com",
  "emergencyContacts": [
    {
      "name": "Emergency Contact 1",
      "phoneNumber": "+1234567890",  // Use verified Twilio number
      "relationship": "Friend"
    }
  ]
}
```

### 6.2 Test Emergency Alert

1. Open app
2. Grant location permissions when prompted
3. Press the red SOS button
4. Check that SMS is sent to emergency contacts

### 6.3 Local Testing with Emulators

```bash
# Terminal 1: Start Firebase emulators
cd functions
npm run serve

# Terminal 2: Run mobile app pointing to emulators
# Add to App initialization:
# Firebase.functions.useEmulator("localhost", 5001)
```

## Step 7: Production Deployment

### 7.1 Update Firebase Functions to Production

```bash
firebase deploy --only functions --project production
```

### 7.2 Android Release Build

```bash
./gradlew composeApp:assembleRelease
```

### 7.3 iOS Release Build

In Xcode:
1. Product → Archive
2. Distribute App → App Store Connect

## Troubleshooting

### Functions not deploying

- Check Node.js version: `node --version` (should be 18+)
- Clear functions cache: `rm -rf functions/node_modules && npm install`

### SMS not sending

- Verify Twilio credentials: `firebase functions:config:get`
- Check Twilio console for error logs
- Ensure phone numbers are in E.164 format

### Location not working

**Android:**
- Check permissions in Settings → Apps → DocBee
- Enable location services

**iOS:**
- Check permissions in Settings → DocBee
- Enable location services globally

### Firestore permission denied

- Verify user is authenticated
- Check Firestore rules are deployed
- User document must match authenticated user ID

## Cost Estimation

### Development (Testing)
- Firebase: Free tier (sufficient for testing)
- Twilio: ~$15 (trial credit)

### Production (100 users, 10 emergencies/month)
- Firebase Functions: Free (under 2M invocations/month)
- Firebase Firestore: Free (under 50K reads/day)
- Twilio SMS: 10 emergencies × 5 contacts = 50 SMS = ~$0.40/month

## Security Checklist

- ✅ Firebase config files in `.gitignore`
- ✅ Twilio credentials stored in Firebase Functions config (never in code)
- ✅ Firestore security rules deployed
- ✅ Firebase Authentication enabled
- ✅ Rate limiting implemented (1 alert per 5 minutes)
- ✅ HTTPS-only communication

## Next Steps

1. **Add user authentication UI** - Sign up/login screens
2. **Settings screen** - Allow users to manage emergency contacts
3. **History screen** - Show past emergency alerts
4. **Testing mode** - Don't send real SMS during development
5. **Push notifications** - Notify user when alert is sent
6. **Voice calls** - Future AI-powered emergency calls

## Support

For issues:
- Firebase: [Firebase Support](https://firebase.google.com/support)
- Twilio: [Twilio Support](https://www.twilio.com/help)
- Repository: Check GitHub issues
