const functions = require('firebase-functions');
const admin = require('firebase-admin');
const twilio = require('twilio');

// Load environment variables for local development
if (process.env.NODE_ENV !== 'production') {
  require('dotenv').config();
}

admin.initializeApp();

// Twilio configuration
// Local: Uses .env file
// Production: Uses Firebase Functions config (set with: firebase functions:config:set)
const getTwilioConfig = () => {
  // Check environment variables first (local development)
  if (process.env.TWILIO_ACCOUNT_SID) {
    return {
      accountSid: process.env.TWILIO_ACCOUNT_SID,
      authToken: process.env.TWILIO_AUTH_TOKEN,
      phoneNumber: process.env.TWILIO_PHONE_NUMBER,
    };
  }

  // Fall back to Firebase Functions config (production)
  const twilioConf = functions.config().twilio || {};
  return {
    accountSid: twilioConf.account_sid,
    authToken: twilioConf.auth_token,
    phoneNumber: twilioConf.phone_number,
  };
};

const twilioConfig = getTwilioConfig();
const twilioClient = twilio(twilioConfig.accountSid, twilioConfig.authToken);
const TWILIO_PHONE_NUMBER = twilioConfig.phoneNumber;

/**
 * Send emergency SMS to user's emergency contacts
 * Callable function that requires authentication
 */
exports.sendEmergencySMS = functions.https.onCall(async (data, context) => {
  console.log('[sendEmergencySMS] Function called');
  console.log('[sendEmergencySMS] Data received:', JSON.stringify(data));
  console.log('[sendEmergencySMS] Context auth:', context.auth ? 'authenticated' : 'not authenticated');
  
  // Verify user is authenticated
  if (!context.auth) {
    console.error('[sendEmergencySMS] Authentication failed - no context.auth');
    throw new functions.https.HttpsError(
        'unauthenticated',
        'User must be authenticated to send emergency alerts.',
    );
  }

  const userId = context.auth.uid;
  console.log('[sendEmergencySMS] User ID:', userId);
  const {location, userName, timestamp} = data;

  // Validate input
  if (!location || !location.latitude || !location.longitude) {
    console.error('[sendEmergencySMS] Invalid location data:', location);
    throw new functions.https.HttpsError(
        'invalid-argument',
        'Location data is required.',
    );
  }
  
  console.log('[sendEmergencySMS] Location validated:', location);

  try {
    // Get user's emergency contacts from Firestore
    console.log('[sendEmergencySMS] Fetching user document from Firestore...');
    const userDoc = await admin.firestore()
        .collection('users')
        .doc(userId)
        .get();

    console.log('[sendEmergencySMS] User document exists:', userDoc.exists);
    if (!userDoc.exists) {
      console.error('[sendEmergencySMS] User document not found for userId:', userId);
      throw new functions.https.HttpsError(
          'not-found',
          'User profile not found. Please set up emergency contacts first.',
      );
    }

    const userData = userDoc.data();
    console.log('[sendEmergencySMS] User data:', JSON.stringify(userData, null, 2));
    const emergencyContacts = userData.emergencyContacts || [];
    console.log('[sendEmergencySMS] Emergency contacts count:', emergencyContacts.length);

    if (emergencyContacts.length === 0) {
      console.error('[sendEmergencySMS] ⚠️  NO EMERGENCY CONTACTS FOUND');
      console.error('[sendEmergencySMS] User has triggered emergency alert but has no contacts configured');
      console.error('[sendEmergencySMS] User ID:', userId);
      console.error('[sendEmergencySMS] User display name:', userData.displayName || 'N/A');
      throw new functions.https.HttpsError(
          'failed-precondition',
          'At this time you don\'t have any contacts listed. Please add your contacts.',
      );
    }

    // Create Google Maps link
    const mapsLink = `https://maps.google.com/?q=${location.latitude},${location.longitude}`;

    // Format emergency message
    const displayName = userName || userData.displayName || 'A user';
    const timeString = timestamp ?
      new Date(timestamp).toLocaleString() :
      new Date().toLocaleString();
    const message = `🚨 EMERGENCY ALERT 🚨\n\n${displayName} needs immediate help!` +
      `\n\nLocation: ${mapsLink}` +
      `\n\nTime: ${timeString}` +
      '\n\nThis is an automated emergency message from DocBee.';

    // Send SMS to all contacts
    console.log('[sendEmergencySMS] Preparing to send SMS to', emergencyContacts.length, 'contacts');
    console.log('[sendEmergencySMS] Twilio configuration check:');
    console.log('[sendEmergencySMS] - Account SID:', twilioConfig.accountSid ? 'SET' : 'NOT SET');
    console.log('[sendEmergencySMS] - Auth Token:', twilioConfig.authToken ? 'SET' : 'NOT SET');
    console.log('[sendEmergencySMS] - Phone Number:', TWILIO_PHONE_NUMBER || 'NOT SET');
    
    const sendPromises = emergencyContacts.map(async (contact, index) => {
      console.log(`[sendEmergencySMS] Processing contact ${index + 1}:`, contact.name);
      if (!contact.phoneNumber) {
        console.error(`[sendEmergencySMS] Contact ${contact.name} has no phone number`);
        return {success: false, contact: contact.name, error: 'No phone number'};
      }

      try {
        console.log(`[sendEmergencySMS] Sending SMS to ${contact.name} at ${contact.phoneNumber}`);
        const result = await twilioClient.messages.create({
          body: message,
          to: contact.phoneNumber,
          from: TWILIO_PHONE_NUMBER,
        });

        console.log(`[sendEmergencySMS] SMS sent successfully to ${contact.name}, SID:`, result.sid);
        return {
          success: true,
          contact: contact.name,
          messageId: result.sid,
        };
      } catch (error) {
        console.error(`[sendEmergencySMS] Failed to send SMS to ${contact.name}:`, error);
        console.error(`[sendEmergencySMS] Error details:`, error.message);
        return {
          success: false,
          contact: contact.name,
          error: error.message,
        };
      }
    });

    console.log('[sendEmergencySMS] Waiting for all SMS sends to complete...');
    const results = await Promise.all(sendPromises);
    console.log('[sendEmergencySMS] All SMS sends completed. Results:', JSON.stringify(results));

    // Log the emergency event
    console.log('[sendEmergencySMS] Logging emergency event to Firestore...');
    await admin.firestore()
        .collection('emergencyLogs')
        .add({
          userId,
          userName: displayName,
          location: {
            latitude: location.latitude,
            longitude: location.longitude,
          },
          timestamp: admin.firestore.FieldValue.serverTimestamp(),
          contactsNotified: results.filter((r) => r.success).length,
          results,
        });
    console.log('[sendEmergencySMS] Emergency event logged successfully');

    const successCount = results.filter((r) => r.success).length;
    const failureCount = results.filter((r) => !r.success).length;
    
    console.log(`[sendEmergencySMS] Summary: ${successCount} successful, ${failureCount} failed`);

    const response = {
      success: successCount > 0,
      message: `Emergency alert sent to ${successCount} contact(s).`,
      successCount,
      failureCount,
      details: results,
    };
    console.log('[sendEmergencySMS] Returning response:', JSON.stringify(response));
    return response;
  } catch (error) {
    console.error('[sendEmergencySMS] Critical error:', error);
    console.error('[sendEmergencySMS] Error stack:', error.stack);

    // Re-throw HttpsErrors as-is
    if (error instanceof functions.https.HttpsError) {
      console.error('[sendEmergencySMS] Re-throwing HttpsError:', error.message);
      throw error;
    }

    // Wrap other errors
    console.error('[sendEmergencySMS] Wrapping error as internal error');
    throw new functions.https.HttpsError(
        'internal',
        'Failed to send emergency alert. Please try again.',
        error.message,
    );
  }
});

/**
 * Rate limiting: Track emergency alerts per user
 * Prevents abuse by limiting to 1 alert per 5 minutes
 */
exports.checkEmergencyRateLimit = functions.https.onCall(async (data, context) => {
  console.log('[checkEmergencyRateLimit] Function called');
  console.log('[checkEmergencyRateLimit] Context auth:', context.auth ? 'authenticated' : 'not authenticated');
  
  if (!context.auth) {
    console.error('[checkEmergencyRateLimit] Authentication failed');
    throw new functions.https.HttpsError('unauthenticated', 'Authentication required');
  }

  const userId = context.auth.uid;
  console.log('[checkEmergencyRateLimit] User ID:', userId);
  const RATE_LIMIT_MINUTES = 5;

  // Check last emergency alert
  console.log('[checkEmergencyRateLimit] Checking recent alerts from Firestore...');
  const recentAlerts = await admin.firestore()
      .collection('emergencyLogs')
      .where('userId', '==', userId)
      .orderBy('timestamp', 'desc')
      .limit(1)
      .get();

  console.log('[checkEmergencyRateLimit] Recent alerts found:', !recentAlerts.empty);
  if (recentAlerts.empty) {
    console.log('[checkEmergencyRateLimit] No recent alerts - allowing request');
    return {allowed: true};
  }

  const lastAlert = recentAlerts.docs[0].data();
  const lastAlertTime = lastAlert.timestamp.toDate();
  const timeSinceLastAlert = Date.now() - lastAlertTime.getTime();
  const minutesSince = Math.floor(timeSinceLastAlert / 1000 / 60);
  console.log('[checkEmergencyRateLimit] Last alert was', minutesSince, 'minutes ago');

  if (minutesSince < RATE_LIMIT_MINUTES) {
    const minutesRemaining = RATE_LIMIT_MINUTES - minutesSince;
    console.log('[checkEmergencyRateLimit] Rate limit active -', minutesRemaining, 'minutes remaining');
    return {
      allowed: false,
      minutesRemaining,
      message: `Please wait ${minutesRemaining} more minute(s) before sending another alert.`,
    };
  }

  console.log('[checkEmergencyRateLimit] Rate limit passed - allowing request');
  return {allowed: true};
});
