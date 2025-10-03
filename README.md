# DocBee - Alert family members about your emergency

## Project Description
This project has been created with the main idea of helping people report **incidents** or **accidents** to their family members.  
Users can share details about their **current situation**, the **type of problem**, and their **location**.  

For example, if someone falls and is in danger, the app allows them to send their location so others can quickly find them and provide better assistance.

---

## Firebase Configuration in KMP

This project uses **Firebase Authentication** for both Android and iOS platforms.  
Below are the steps required to integrate the Firebase configuration files into the multiplatform architecture.

---

## Expected File Structure

- `composeApp/src/androidMain`  
  Contains native code for Android.

- `composeApp/src/iosMain`  
  Contains native code for iOS.

- `composeApp/src/commonMain`  
  Contains the shared (Kotlin Multiplatform) code.

- **Firebase Configuration Files:**
  - `google-services.json`: should be placed in  
    `composeApp/src`
  - `GoogleService-Info.plist`: should be placed in  
    `iosApp/iosApp`

> *You can add reference images of the folder structure here.*

---

## Configuration Steps

1. **Add Firebase Configuration Files:**

   - Place the `google-services.json` file in:
     ```
     composeApp/src
     ```

   - Place the `GoogleService-Info.plist` file in:
     ```
     iosApp/iosApp
     ```

2. **Sync CocoaPods Dependencies:**

   From the root of the KMP project, run:
   ```bash
   ./gradlew podInstall
   pod install
  ```


