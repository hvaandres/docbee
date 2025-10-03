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
   ```
    ./gradlew podInstall
   pod install
   ```

## App Screenshots

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/088fc16c-e0ca-4787-b555-8dc2354ab832" alt="Login Page" width="250"/>
      <br/>Login Page
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/44547e78-940b-4335-b739-c296cf4a3d24" alt="Settings" width="250"/>
      <br/>Settings
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/6a9813e7-c6a9-4747-93e4-147cb339d166" alt="New User" width="250"/>
      <br/>New User
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/1aab59dd-62df-4acf-87df-491f426a8ce4" alt="Directory" width="250"/>
      <br/>Directory
    </td>
  </tr>
  <tr>
    <td align="center" colspan="2">
      <img src="https://github.com/user-attachments/assets/8db98f11-229d-4ad2-9ad7-66e03c8080bd" alt="Main Menu" width="250"/>
      <br/>Main Menu
    </td>
  </tr>
</table>


  
   


