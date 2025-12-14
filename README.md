# 📱 MediaFinder - Práctica 5

**Consulta de Base de Datos vía APIs**

---

## 👨‍💻 Información del Alumno

- **Nombre:** Cortes Buendia Martin Francisco
- **Boleta:** 2022630507
- **Materia:** Desarrollo de Aplicaciones Móviles Nativas
- **Profesor:** Gabriel Hurtado Avilés
- **Fecha de Entrega:** 15 de Diciembre, 2024

---


## 📖 Descripción del Proyecto

**MediaFinder** es una aplicación Android nativa que permite a los usuarios buscar series y películas utilizando la API pública de TVMaze. La aplicación implementa un sistema completo de gestión de favoritos, historial de búsquedas y recomendaciones personalizadas, con persistencia local mediante Room Database y sincronización automática con la API remota.

### Objetivo Principal

Desarrollar una aplicación móvil con consultas avanzadas a bases de datos mediante APIs, incluyendo funcionalidades de búsqueda, recomendaciones y persistencia de datos con funcionamiento offline.

---

## ✨ Funcionalidades Implementadas

### ✅ Ejercicio 1: Integración de API REST y Persistencia

#### 1.1 Conexión a API REST
- ✅ Integración con TVMaze API (https://api.tvmaze.com/)
- ✅ Uso de Retrofit para consumo de servicios REST
- ✅ Manejo eficiente de datos en formato JSON
- ✅ Interceptor de logging para monitoreo de peticiones

#### 1.2 Persistencia de Sesión
- ✅ Sistema de login y registro de usuarios
- ✅ Sesión del usuario visible en toda la aplicación
- ✅ Información del usuario en TopBar (similar a redes sociales)
- ✅ Logout funcional

#### 1.3 Base de Datos Local
- ✅ Room Database con 4 tablas:
  - **users**: Usuarios registrados
  - **favorites**: Series favoritas por usuario
  - **search_history**: Historial de búsquedas
  - **show_cache**: Cache de series consultadas

#### 1.4 Sincronización de Datos
- ✅ Sincronización automática entre BD local y API remota
- ✅ Cache inteligente de resultados de búsqueda
- ✅ Actualización periódica de datos
- ✅ Fallback a cache local cuando no hay internet

---

### ✅ Ejercicio 2: Consumo de APIs Públicas

#### 2.1 TVMaze API
- ✅ Búsqueda de series por nombre
- ✅ Obtención de detalles completos (imagen, géneros, rating)
- ✅ Procesamiento de respuestas JSON
- ✅ Manejo de errores de red

#### 2.2 Sistema de Búsqueda
- ✅ Barra de búsqueda con sugerencias
- ✅ Resultados visuales con imágenes
- ✅ Información detallada: géneros, rating, fecha de estreno
- ✅ Guardado automático en historial

#### 2.3 Recomendaciones Personalizadas
- ✅ Algoritmo basado en géneros favoritos del usuario
- ✅ Análisis de frecuencia de géneros
- ✅ Top 10 recomendaciones únicas
- ✅ Actualización dinámica según favoritos

#### 2.4 Almacenamiento y Sincronización
- ✅ Todas las búsquedas se guardan localmente
- ✅ Cache de 24 horas para búsquedas
- ✅ Sincronización transparente para el usuario
- ✅ Modo offline completamente funcional

---

### ✅ Ejercicio 3: Búsqueda, Favoritos y Recomendaciones

#### 3.1 Historial de Búsquedas
- ✅ Historial personal por usuario
- ✅ Ordenado por fecha (más reciente primero)
- ✅ Click para repetir búsqueda
- ✅ Función para limpiar historial
- ✅ Muestra fecha y hora de cada búsqueda

#### 3.2 Sistema de Favoritos
- ✅ Agregar/quitar series de favoritos
- ✅ Lista personalizada por usuario
- ✅ Persistencia 100% local
- ✅ Sincronización con recomendaciones
- ✅ Indicador visual (corazón rojo/gris)

#### 3.3 Sistema de Recomendaciones
**Algoritmo implementado:**
1. Analiza géneros de favoritos del usuario
2. Identifica los 3 géneros más frecuentes
3. Busca series similares en cache y API
4. Filtra duplicados
5. Retorna top 10 recomendaciones

**Características:**
- ✅ Basado en favoritos reales del usuario
- ✅ Actualización dinámica al agregar favoritos
- ✅ Mensaje informativo si no hay favoritos
- ✅ Loading states durante generación

---

## 🛠️ Tecnologías Utilizadas

### Frontend
- **Jetpack Compose** - UI moderna y declarativa
- **Material 3** - Diseño consistente y moderno
- **Navigation Compose** - Navegación entre pantallas
- **Coil** - Carga eficiente de imágenes

### Backend Local
- **Room Database** - Persistencia local (SQLite)
- **Kotlin Coroutines** - Programación asíncrona
- **Flow** - Estado reactivo

### Networking
- **Retrofit 2.9.0** - Cliente HTTP REST
- **Gson** - Serialización/Deserialización JSON
- **OkHttp** - Logging de peticiones

### Arquitectura
- **MVVM** - Model-View-ViewModel
- **Clean Architecture** - Separación de capas
- **Repository Pattern** - Single Source of Truth
- **Hilt** - Inyección de dependencias
- **KSP** - Procesamiento de anotaciones (Kotlin Symbol Processing)

### Versiones
- **Kotlin:** 1.9.10
- **Compose BOM:** 2023.10.01
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)
- **Gradle:** 8.1.2

---

## 🏗️ Arquitectura

### Patrón MVVM + Clean Architecture

```
📱 UI Layer (Jetpack Compose)
    ↓
🎮 ViewModel Layer (StateFlow)
    ↓
📦 Repository Layer (Single Source of Truth)
    ↓ ↙
💾 Room DB ← → 🌐 Retrofit API
(Local)          (Remote)
```

### Estructura del Proyecto

```
com.escom.mediafinder/
├── data/
│   ├── local/          # Room Database
│   │   ├── Entities.kt
│   │   ├── Daos.kt
│   │   └── MediaFinderDatabase.kt
│   ├── model/          # Modelos de datos
│   │   └── Show.kt
│   ├── remote/         # Retrofit API
│   │   └── TVMazeApi.kt
│   └── repository/     # Repositorio único
│       └── MediaFinderRepository.kt
├── di/                 # Dependency Injection
│   └── AppModule.kt
└── ui/
    ├── navigation/     # Navegación
    ├── screens/        # Pantallas Compose
    ├── theme/          # Tema Material 3
    └── viewmodel/      # ViewModels
```

### Base de Datos Room

#### Tabla: users
```kotlin
- id: Int (PK)
- username: String
- password: String
- isAdmin: Boolean
```

#### Tabla: favorites
```kotlin
- id: Int (PK)
- userId: Int (FK)
- showId: Int
- showName: String
- imageUrl: String?
- genres: String?
- rating: Double?
- addedAt: Long
```

#### Tabla: search_history
```kotlin
- id: Int (PK)
- userId: Int (FK)
- query: String
- timestamp: Long
```

#### Tabla: show_cache
```kotlin
- showId: Int (PK)
- name: String
- genres: String?
- rating: Double?
- imageUrl: String?
- summary: String?
- cachedAt: Long
```

---

## 📸 Capturas de Pantalla

### 1. Login y Registro

#### Pantalla de Login
![Login Screen]
<img width="319" height="716" alt="Captura de pantalla 2025-12-14 a la(s) 5 12 50 p m" src="https://github.com/user-attachments/assets/3bac0d92-952a-41b5-8fa7-265105a2d5cf" />


*Sistema de autenticación con validación de usuarios*

#### Pantalla de Registro
![Register Screen]
<img width="319" height="716" alt="Captura de pantalla 2025-12-14 a la(s) 5 13 08 p m" src="https://github.com/user-attachments/assets/98751fea-9bd8-4563-91e6-f670e82b3916" />

*Formulario de registro de nuevos usuarios*

---

### 2. Búsqueda y Resultados

#### Pantalla Principal (Home)

![Home Screen]
<img width="319" height="716" alt="Captura de pantalla 2025-12-14 a la(s) 5 13 34 p m" src="https://github.com/user-attachments/assets/52e7d1a1-38e2-411c-bd20-564e62070858" />

*Barra de búsqueda y sesión del usuario visible*

#### Resultados de Búsqueda
![Search Results]
<img width="319" height="716" alt="Captura de pantalla 2025-12-14 a la(s) 5 14 15 p m" src="https://github.com/user-attachments/assets/87555a13-2b8d-43b0-97b7-91ff287087ab" />

*Lista de series con imágenes, géneros y ratings*

#### Detalle de Serie
![Show Detail]
<img width="319" height="128" alt="Captura de pantalla 2025-12-14 a la(s) 5 14 37 p m" src="https://github.com/user-attachments/assets/f0b57736-92d4-47b6-9f93-ee3e141523e9" />

*Información completa de la serie seleccionada*

---

### 3. Favoritos

#### Lista de Favoritos
![Favorites List]

<img width="319" height="712" alt="Captura de pantalla 2025-12-14 a la(s) 5 15 22 p m" src="https://github.com/user-attachments/assets/260ffa3f-af63-4f12-beb7-6da66d19fd15" />

*Series guardadas como favoritas por el usuario*

#### Agregar a Favoritos
![Add to Favorites]
<img width="319" height="127" alt="Captura de pantalla 2025-12-14 a la(s) 5 16 04 p m" src="https://github.com/user-attachments/assets/10abfc51-76c5-4bc9-ad7b-856762434d2f" />
<img width="319" height="127" alt="Captura de pantalla 2025-12-14 a la(s) 5 16 13 p m" src="https://github.com/user-attachments/assets/fd085416-3e2c-4dec-869a-660c9e320ae5" />
<img width="319" height="640" alt="Captura de pantalla 2025-12-14 a la(s) 5 16 26 p m" src="https://github.com/user-attachments/assets/991f384e-c4e9-4357-910e-927b38ae2a44" />

*Acción de agregar serie a favoritos (corazón rojo)*

#### Quitar de Favoritos
![Remove from Favorites]
<img width="319" height="251" alt="Captura de pantalla 2025-12-14 a la(s) 5 16 51 p m" src="https://github.com/user-attachments/assets/b84321a1-fdb7-4455-8e57-a72235a45555" />

<img width="319" height="251" alt="Captura de pantalla 2025-12-14 a la(s) 5 17 08 p m" src="https://github.com/user-attachments/assets/c421fa15-639f-4759-b5ce-cda8a158c7b2" />


*Acción de quitar serie de favoritos*

---

### 4. Historial de Búsquedas

#### Historial Personal
![Search History]
<img width="319" height="251" alt="Captura de pantalla 2025-12-14 a la(s) 5 17 28 p m" src="https://github.com/user-attachments/assets/689f6dd0-3bae-4eec-becd-a051364a9baf" />

*Búsquedas recientes del usuario con fecha y hora*

#### Limpiar Historial
![Clear History]
<img width="319" height="451" alt="Captura de pantalla 2025-12-14 a la(s) 5 17 42 p m" src="https://github.com/user-attachments/assets/f56b71a9-3828-414c-b90d-77e1461520b9" />

*Función para limpiar el historial de búsquedas*

---

### 5. Recomendaciones

#### Pantalla de Recomendaciones
![Recommendations]
<img width="319" height="710" alt="Captura de pantalla 2025-12-14 a la(s) 5 18 25 p m" src="https://github.com/user-attachments/assets/7d1fb274-a8dc-4025-99aa-2bffa079ac4e" />


*Recomendaciones personalizadas basadas en favoritos*

#### Sin Favoritos (Estado Vacío)
![No Favorites]
<img width="319" height="710" alt="Captura de pantalla 2025-12-14 a la(s) 5 18 45 p m" src="https://github.com/user-attachments/assets/79638df5-cf78-4cd6-ab8a-207c8e480ba5" />
<img width="319" height="710" alt="Captura de pantalla 2025-12-14 a la(s) 5 19 08 p m" src="https://github.com/user-attachments/assets/da5e5928-34cd-47ae-ba0d-eaa3dccd6241" />


*Mensaje cuando no hay favoritos para generar recomendaciones*

---

### 6. Persistencia y Modo Offline

#### Funcionamiento Offline
![Offline Mode]
<img width="319" height="710" alt="Captura de pantalla 2025-12-14 a la(s) 5 20 16 p m" src="https://github.com/user-attachments/assets/ccfa3cab-69be-4248-8dbb-5cde5d04e3fe" />
<img width="319" height="710" alt="Captura de pantalla 2025-12-14 a la(s) 5 20 44 p m" src="https://github.com/user-attachments/assets/4a47bffc-9562-4494-9334-ba75be88d300" />


*App funcionando sin conexión a internet*

#### Database Inspector
![Database Inspector]
<img width="319" height="688" alt="Captura de pantalla 2025-12-14 a la(s) 5 22 55 p m" src="https://github.com/user-attachments/assets/b3580535-ff2f-4e3d-8a06-fd9d5872b188" />

*Visualización de las tablas Room en Android Studio*

---


## 🚀 Instalación y Configuración

### Requisitos Previos
- Android Studio Hedgehog o superior
- JDK 17
- Android SDK 34
- Dispositivo físico o emulador con Android 7.0+

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/[tu-usuario]/MediaFinder.git
cd MediaFinder
```

2. **Abrir en Android Studio**
```
File → Open → Seleccionar carpeta MediaFinder
```

3. **Sync Gradle**
```
Esperar a que Gradle descargue todas las dependencias
```

4. **Ejecutar la aplicación**
```
Run → Run 'app' (Shift + F10)
```

### Configuración de la Base de Datos

La base de datos Room se crea automáticamente en el primer inicio de la aplicación. No requiere configuración adicional.

### API Utilizada

**TVMaze API**
- URL Base: `https://api.tvmaze.com/`
- No requiere API Key
- Acceso público gratuito

**Endpoints utilizados:**
```kotlin
GET /search/shows?q={query}  // Buscar series
GET /shows/{id}              // Detalles de serie
GET /shows?page={page}       // Shows populares
```

---

## 🧪 Pruebas Realizadas

### 1. Pruebas de API REST

#### Búsqueda de Series
- ✅ Búsqueda exitosa con resultados válidos
- ✅ Búsqueda sin resultados maneja correctamente
- ✅ Error de red manejado con fallback a cache
- ✅ Timeout de conexión manejado

#### Sincronización
- ✅ Datos se guardan en cache automáticamente
- ✅ Cache se utiliza cuando no hay internet
- ✅ Actualización periódica de datos

### 2. Pruebas de Persistencia

#### Room Database
- ✅ Inserción de usuarios funcional
- ✅ Login con credenciales correctas
- ✅ Favoritos persisten entre sesiones
- ✅ Historial se mantiene después de cerrar app

#### Modo Offline
- ✅ Favoritos funcionan sin internet
- ✅ Historial accesible offline
- ✅ Cache de búsquedas disponible
- ✅ App no crashea sin conexión

### 3. Pruebas de Funcionalidad

#### Sistema de Favoritos
- ✅ Agregar favoritos funciona correctamente
- ✅ Eliminar favoritos funciona correctamente
- ✅ Estado del corazón se actualiza visualmente
- ✅ Favoritos únicos por usuario

#### Recomendaciones
- ✅ Algoritmo genera recomendaciones relevantes
- ✅ Se actualiza al agregar/quitar favoritos
- ✅ Maneja correctamente cuando no hay favoritos
- ✅ Top 10 sin duplicados

#### Historial
- ✅ Cada búsqueda se guarda con timestamp
- ✅ Ordenamiento correcto (más reciente primero)
- ✅ Click en historial repite búsqueda
- ✅ Limpiar historial funciona

### 4. Pruebas de UI/UX

- ✅ Navegación entre pantallas fluida
- ✅ Loading states visibles
- ✅ Mensajes de error claros
- ✅ Estados vacíos informativos
- ✅ Responsive en diferentes tamaños de pantalla

---

## 💡 Conclusiones

### Logros Principales

1. **Implementación Exitosa de MVVM**
   - Se logró una separación clara de responsabilidades
   - El código es mantenible y escalable
   - Los ViewModels manejan correctamente el estado

2. **Sistema de Sincronización Robusto**
   - La sincronización entre Room y Retrofit funciona sin intervención del usuario
   - El modo offline es completamente funcional
   - El cache inteligente mejora la experiencia del usuario

3. **Arquitectura Limpia y Profesional**
   - La estructura del proyecto sigue las mejores prácticas de Android
   - El uso de Hilt facilita la inyección de dependencias
   - El código es testeable y modular

4. **UI Moderna con Jetpack Compose**
   - La interfaz es intuitiva y atractiva
   - Material 3 proporciona consistencia visual
   - Las animaciones mejoran la experiencia

### Retos Enfrentados

1. **Compatibilidad Kapt vs KSP**
   - **Problema:** Kapt no es compatible con Java 17+
   - **Solución:** Migración a KSP (Kotlin Symbol Processing)
   - **Aprendizaje:** KSP es más rápido y el futuro de Android

2. **Error de jlink en Mac**
   - **Problema:** Espacios en la ruta de Android Studio causaban fallos
   - **Solución:** Limpiar cache de Gradle y configurar JDK correcto
   - **Aprendizaje:** Importancia de la configuración del entorno

3. **Sincronización de Datos**
   - **Problema:** Decidir cuándo usar cache vs API
   - **Solución:** Implementar lógica try-catch con fallback inteligente
   - **Aprendizaje:** Balance entre performance y datos actualizados

4. **Sistema de Recomendaciones**
   - **Problema:** Generar recomendaciones relevantes
   - **Solución:** Algoritmo basado en frecuencia de géneros
   - **Aprendizaje:** Análisis de datos del usuario para personalización

### Conocimientos Adquiridos

- Manejo avanzado de Jetpack Compose
- Implementación profesional de Room Database
- Consumo de APIs REST con Retrofit
- Arquitectura MVVM y Clean Architecture
- Inyección de dependencias con Hilt
- Programación reactiva con Coroutines y Flow
- Manejo de estados y navegación en Compose

### Mejoras Futuras

1. **Funcionalidad de Administrador**
   - Implementar panel admin para ver todos los usuarios
   - Dashboard con estadísticas de búsquedas

2. **Login con Google**
   - Integración con Firebase Auth
   - Login social para mejor UX

3. **Detalles de Serie**
   - Pantalla dedicada con información completa
   - Trailers y actores

4. **Notificaciones Push**
   - Alertas de nuevos episodios de favoritos
   - Recomendaciones semanales

5. **Testing Automatizado**
   - Unit tests para ViewModels y Repository
   - UI tests con Compose Testing

---

## 📚 Referencias

### Documentación Oficial

1. Android Developers. (2024). *Jetpack Compose*. https://developer.android.com/jetpack/compose

2. Android Developers. (2024). *Room Persistence Library*. https://developer.android.com/training/data-storage/room

3. Square. (2024). *Retrofit*. https://square.github.io/retrofit/

4. Android Developers. (2024). *Guide to app architecture*. https://developer.android.com/topic/architecture

5. Google. (2024). *Hilt - Dependency injection for Android*. https://developer.android.com/training/dependency-injection/hilt-android

### APIs Utilizadas

6. TVMaze. (2024). *TVMaze API Documentation*. https://www.tvmaze.com/api

### Bibliotecas y Herramientas

7. Coil. (2024). *Image loading for Android backed by Kotlin Coroutines*. https://coil-kt.github.io/coil/

8. JetBrains. (2024). *Kotlin Coroutines*. https://kotlinlang.org/docs/coroutines-overview.html

9. Google. (2024). *Material Design 3*. https://m3.material.io/

### Tutoriales y Recursos

10. Philipp Lackner. (2024). *MVVM + Clean Architecture Tutorial*. YouTube.

11. Android Developers. (2024). *Codelab: Using Room with Flow*. https://developer.android.com/codelabs/android-room-with-a-view-kotlin

---

## 👨‍💻 Autor

**[Tu Nombre Completo]**
- Boleta: [Tu Número de Boleta]
- Email: [tu-email@alumno.ipn.mx]
- GitHub: [tu-usuario]

---

## 📄 Licencia

Este proyecto fue desarrollado como parte de la Práctica 5 del curso de Desarrollo de Aplicaciones Móviles Nativas en ESCOM - IPN.

---

**Desarrollado con ❤️ usando Jetpack Compose**

*Práctica 5 - Desarrollo de Aplicaciones Móviles Nativas*

*ESCOM - Instituto Politécnico Nacional*

*Diciembre 2024*
