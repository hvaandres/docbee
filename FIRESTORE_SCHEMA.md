# Firestore Data Schema

## Collections

### `users`
Stores user profiles and emergency contact information.

**Document ID**: Firebase Auth UID

**Fields**:
```typescript
{
  displayName: string;           // User's display name
  email: string;                 // User's email
  phoneNumber?: string;          // User's phone number (optional)
  createdAt: Timestamp;          // Account creation timestamp
  emergencyContacts: Array<{     // Up to 5 emergency contacts
    name: string;                // Contact's name
    phoneNumber: string;         // E.164 format: +1234567890
    relationship?: string;       // e.g., "Spouse", "Parent", "Friend"
  }>;
}
```

**Example**:
```json
{
  "displayName": "Alan Haro",
  "email": "alan@example.com",
  "phoneNumber": "+15551234567",
  "createdAt": "2025-12-12T05:00:00Z",
  "emergencyContacts": [
    {
      "name": "Maria Haro",
      "phoneNumber": "+15559876543",
      "relationship": "Spouse"
    },
    {
      "name": "Carlos Haro",
      "phoneNumber": "+15551112222",
      "relationship": "Parent"
    }
  ]
}
```

**Security Rules**:
- Users can only read/write their own document
- Document ID must match authenticated user's UID

---

### `emergencyLogs`
Audit log of all emergency alerts sent.

**Document ID**: Auto-generated

**Fields**:
```typescript
{
  userId: string;                // User who triggered the alert
  userName: string;              // Display name at time of alert
  location: {
    latitude: number;            // GPS latitude
    longitude: number;           // GPS longitude
  };
  timestamp: Timestamp;          // Server timestamp
  contactsNotified: number;      // Number of successful SMS sends
  results: Array<{               // Detailed results per contact
    success: boolean;
    contact: string;
    messageId?: string;          // Twilio message SID
    error?: string;              // Error message if failed
  }>;
}
```

**Example**:
```json
{
  "userId": "abc123xyz",
  "userName": "Alan Haro",
  "location": {
    "latitude": 40.7128,
    "longitude": -74.0060
  },
  "timestamp": "2025-12-12T05:05:30Z",
  "contactsNotified": 2,
  "results": [
    {
      "success": true,
      "contact": "Maria Haro",
      "messageId": "SM1234567890abcdef"
    },
    {
      "success": true,
      "contact": "Carlos Haro",
      "messageId": "SM0987654321fedcba"
    }
  ]
}
```

**Security Rules**:
- Users can only read their own logs
- Only Cloud Functions can write (prevents tampering)

**Indexes**:
- Composite: `userId` (ASC) + `timestamp` (DESC)
  - Used for: Rate limiting, history queries

---

## Phone Number Format

All phone numbers must be in **E.164 format**:
- Include country code
- No spaces, dashes, or parentheses
- Examples:
  - ✅ `+15551234567` (US)
  - ✅ `+525512345678` (Mexico)
  - ✅ `+447911123456` (UK)
  - ❌ `(555) 123-4567`
  - ❌ `5551234567`

---

## Rate Limiting

Emergency alerts are rate-limited to **1 per 5 minutes per user** to prevent:
- Accidental spam
- System abuse
- Excessive Twilio costs

Rate limit is checked via `checkEmergencyRateLimit` Cloud Function before allowing alert.
