# Quedadapp (Android)

App nativa Android (Kotlin + Jetpack Compose) de Quedadapp, hablando
con la misma API que ya usa la web
([`quedadapp`](https://github.com/Marinettoo/quedadapp), carpeta
`src/app/api/v1/...`).

## Primer arranque en Android Studio

1. **Abrir el proyecto**: `File → Open...` y seleccionar esta carpeta.
   Android Studio detectará el Gradle Wrapper y sincronizará solo.
2. Si pide "SDK location not found", es normal: crea `local.properties`
   en la raíz con la ruta de tu SDK (Android Studio suele rellenarlo
   él solo la primera vez). Ese archivo nunca se sube a git.
3. Para probar contra tu propio servidor en desarrollo (`npm run dev`
   en el proyecto `quedadapp`, puerto 3000):
   - **Emulador**: ya funciona tal cual — `10.0.2.2` es cómo el
     emulador ve el `localhost` de tu PC (ver `API_BASE_URL` en
     `app/build.gradle.kts`, variante `debug`).
   - **Móvil físico en la misma red Wi-Fi**: cambia esa URL por la IP
     de tu PC en la red local (algo como `http://192.168.1.X:3000/`).
4. La variante `release` apunta a `https://furbo.marinettoo.es/`
   (producción).

## Qué hay ya

- Login, registro y pantalla de "solicitud enviada" (cuenta pendiente
  de aprobación), con sesión guardada mediante un token cifrado en
  disco (`EncryptedSharedPreferences`).
- Pantalla de inicio mínima, solo para confirmar que el login y la
  sesión guardada funcionan de punta a punta.

## Qué falta (próximos bloques)

Próximos partidos, asistencia/portero, equipos, pagos, votaciones,
apuestas, estadísticas, historial y administración — se irán añadiendo
por partes, reutilizando la capa de red y sesión ya montada en
`data/`.
