# DocBee Emergency SMS - Cloud Functions

Firebase Cloud Functions for sending emergency SMS alerts via Twilio.

## Setup Instructions

### 1. Firebase Authentication
```bash
firebase login
```

### 2. Initialize Firebase (if not already done)
```bash
# From project root
firebase init
# Select: Functions, Firestore
# Choose existing project or create new one
# Language: JavaScript
# ESLint: Yes
# Install dependencies: Yes
```

### 3. Install Dependencies
```bash
cd functions
npm install
```

### 4. Configure Twilio Credentials
Get your credentials from [Twilio Console](https://console.twilio.com/):
- Account SID
- Auth Token
- Twilio Phone Number

Set them in Firebase:
```bash
firebase functions:config:set \
  twilio.account_sid="YOUR_ACCOUNT_SID" \
  twilio.auth_token="YOUR_AUTH_TOKEN" \
  twilio.phone_number="+1234567890"
```

### 5. Deploy Functions
```bash
# From project root
firebase deploy --only functions

# Or deploy specific function
firebase deploy --only functions:sendEmergencySMS
```

### 6. Deploy Firestore Rules & Indexes
```bash
firebase deploy --only firestore:rules,firestore:indexes
```

## Local Testing

### Run Functions Emulator
```bash
cd functions
npm run serve
```

The emulator will run at `http://localhost:5001`

### Test with Firebase Functions Shell
```bash
npm run shell
```

## Cloud Functions

### `sendEmergencySMS`
- **Type**: HTTPS Callable
- **Auth**: Required
- **Purpose**: Send emergency SMS to all user's contacts
- **Input**:
  ```json
  {
    "location": {
      "latitude": 40.7128,
      "longitude": -74.0060
    },
    "userName": "John Doe",
    "timestamp": 1234567890000
  }
  ```
- **Output**:
  ```json
  {
    "success": true,
    "message": "Emergency alert sent to 3 contact(s).",
    "successCount": 3,
    "failureCount": 0,
    "details": [...]
  }
  ```

### `checkEmergencyRateLimit`
- **Type**: HTTPS Callable
- **Auth**: Required
- **Purpose**: Check if user can send another alert (rate limiting)
- **Rate Limit**: 1 alert per 5 minutes

## Firestore Data Structure

### Users Collection (`/users/{userId}`)
```json
{
  "displayName": "John Doe",
  "email": "john@example.com",
  "emergencyContacts": [
    {
      "name": "Jane Doe",
      "phoneNumber": "+1234567890",
      "relationship": "Spouse"
    }
  ]
}
```

### Emergency Logs (`/emergencyLogs/{logId}`)
```json
{
  "userId": "abc123",
  "userName": "John Doe",
  "location": {
    "latitude": 40.7128,
    "longitude": -74.0060
  },
  "timestamp": "Firestore Timestamp",
  "contactsNotified": 3,
  "results": [...]
}
```

## Cost Estimation

- **Firebase Functions**: Free tier includes 2M invocations/month
- **Twilio SMS**: ~$0.0079 per SMS (US)
- **Example**: 100 emergencies/month × 5 contacts = 500 SMS = ~$4/month

## Security

- All functions require Firebase Authentication
- Twilio credentials stored securely in Firebase config
- Rate limiting prevents abuse (1 alert per 5 minutes)
- Firestore rules enforce user data isolation
