# Finanzas Personales - App Android

Aplicación nativa Android desarrollada en Kotlin con Firebase.

## Secciones implementadas

| Sección | Descripción |
|---|---|
| **Login** | Autenticación con Firebase Auth |
| **Registro** | Creación de cuenta con nombre, email y contraseña |
| **Menú** | Bottom Navigation con 5 secciones |
| **Listado** | Lista de transacciones con filtros (Todos / Ingresos / Gastos) |
| **Detalle** | Vista detallada de cada transacción o meta |
| **Formulario** | Crear y editar transacciones y metas |
| **Configuración** | Perfil del usuario, notificaciones, cerrar sesión |
| **Créditos** | Información del estudiante y tecnologías usadas |

## Configuración Firebase

1. Crear proyecto en [Firebase Console](https://console.firebase.google.com)
2. Agregar app Android con package `com.finanzas.app`
3. Descargar `google-services.json` y colocarlo en `app/`
4. Habilitar **Authentication > Email/Password**
5. Habilitar **Firestore Database** y aplicar las reglas de `firestore.rules`

## Estructura del proyecto

```
app/src/main/java/com/finanzas/app/
├── data/
│   ├── model/          # Transaction, Goal, User
│   └── repository/     # AuthRepository, TransactionRepository, GoalRepository
├── ui/
│   ├── auth/           # SplashActivity, AuthActivity, LoginFragment, RegisterFragment
│   ├── main/           # MainActivity
│   ├── home/           # HomeFragment (resumen + transacciones recientes)
│   ├── transactions/   # Listado, Detalle, Formulario
│   ├── goals/          # Listado, Detalle, Formulario de metas
│   ├── settings/       # Configuración
│   └── credits/        # Créditos del estudiante
└── utils/              # Extensions (formatCurrency, showSnackbar)
```

## Tecnologías

- **Kotlin** + Android Studio
- **Firebase Auth** - Autenticación
- **Firebase Firestore** - Base de datos en la nube
- **Navigation Component** - Navegación entre pantallas
- **ViewModel + LiveData** - Arquitectura MVVM
- **Material Design 3** - Componentes UI
- **Coroutines** - Operaciones asíncronas

## Pasos para ejecutar

1. Clonar/abrir el proyecto en Android Studio
2. Agregar `google-services.json` en la carpeta `app/`
3. Sincronizar Gradle
4. Ejecutar en emulador o dispositivo físico (API 24+)

> **Nota:** Reemplaza los datos en `fragment_credits.xml` con tu información real.
