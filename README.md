# Configuración de Firebase en KMP (Kotlin Multiplatform)

Este proyecto utiliza Firebase Authentication para las plataformas Android e iOS. A continuación se describen los pasos necesarios para integrar los archivos de configuración de Firebase en la arquitectura multiplataforma.

---

## Estructura esperada de archivos

- `composeApp/src/androidMain`  
  Contiene el código nativo para Android.

- `composeApp/src/iosMain`  
  Contiene el código nativo para iOS.

- `composeApp/src/commonMain`  
  Contiene el código común (Kotlin Multiplatform).

- **Archivos de configuración Firebase:**
  - `google-services.json`: debe colocarse en  
    `composeApp/src`
  - `GoogleService-Info.plist`: debe colocarse en  
    `iosApp/iosApp`

> *Puedes agregar las imágenes de referencia de la estructura de carpetas aquí.*

---

## Pasos para la configuración

1. **Agregar archivos de configuración de Firebase:**

  - Colocar el archivo `google-services.json` en la carpeta:
    ```
    composeApp/src
    ```

  - Colocar el archivo `GoogleService-Info.plist` en la carpeta:
    ```
    iosApp/iosApp
    ```

2. **Sincronizar dependencias de CocoaPods:**

   Desde la raíz del proyecto KMP, ejecutar:
   ```bash
   ./gradlew podInstall
   ```

   Después

   ```bash
   pod install
   ```
