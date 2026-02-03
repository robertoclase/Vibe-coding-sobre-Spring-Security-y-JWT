# 🔐 Spring Security + JWT - Implementación Completa

## 📖 Contenido del Proyecto

Este proyecto implementa un sistema completo de autenticación y autorización usando **Spring Security 6** y **JWT (JSON Web Tokens)** con la librería **jjwt** en **Spring Boot 4**.

---

## 🎯 Requisitos Previos

- **Java**: 21 (incluido en Spring Boot 4)
- **Maven**: 3.6 o superior
- **IDE**: IntelliJ IDEA, Eclipse o VSCode
- **Postman o cURL**: Para probar los endpoints

---

## 📁 Estructura de Carpetas

```
SpringSecurityyJWT/
├── src/
│   ├── main/
│   │   ├── java/com/salesianostriana/dam/SpringSecurityyJWT/springsecurityyjwt/
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java       # Endpoints de autenticación
│   │   │   │   ├── PublicController.java     # Endpoints públicos
│   │   │   │   └── SecureController.java     # Endpoints protegidos
│   │   │   ├── security/
│   │   │   │   ├── SecurityConfig.java       # Configuración de seguridad
│   │   │   │   ├── JwtService.java           # Servicio JWT
│   │   │   │   └── JwtAuthenticationFilter.java  # Filtro JWT
│   │   │   └── dto/
│   │   │       ├── LoginRequest.java         # DTO login
│   │   │       ├── AuthResponse.java         # DTO respuesta auth
│   │   │       └── ApiResponse.java          # DTO respuesta genérica
│   │   └── resources/
│   │       └── application.properties        # Configuración JWT
│   └── test/
├── pom.xml                                   # Dependencias Maven
├── GUIA_JWT.md                               # Guía de uso detallada
└── README.md                                 # Este archivo
```

---

## 🚀 Instalación y Configuración

### 1. Descargar Dependencias

```bash
cd SpringSecurityyJWT
mvn clean install
```

### 2. Configurar JWT (application.properties)

Las propiedades JWT ya están configuradas:

```properties
# Clave secreta (cambiar en producción)
app.jwt.secret=myVerySecureSecretKeyThatIsAtLeast256BitsLongForHSWithJavaJWT1234567890ABCDEF

# Expiración en milisegundos (3600000 = 1 hora)
app.jwt.expiration=3600000
```

### 3. Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

O desde el IDE:
- Click derecho en `SpringSecurityyJwtApplication.java`
- Seleccionar "Run"

La aplicación estará disponible en: `http://localhost:8080`

---

## 👥 Usuarios de Prueba

| Usuario | Contraseña | Roles |
|---------|-----------|-------|
| admin | admin | ADMIN, USER |
| user | user | USER |
| guest | guest | GUEST |

---

## 🔑 Flujo de Autenticación

```
┌─────────────────────────────────────────────────────────────┐
│ 1. Usuario envía credenciales a /api/auth/login             │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│ 2. AuthController autentica contra UserDetailsService       │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│ 3. JwtService genera token JWT con claims                   │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│ 4. Cliente recibe token: {"token": "eyJ...", ...}           │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│ 5. Cliente envía request con:                               │
│    Authorization: Bearer eyJ...                             │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│ 6. JwtAuthenticationFilter intercepta y valida token        │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│ 7. SecurityContext establece autenticación si es válido      │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│ 8. Controller procesa request con usuario autenticado        │
└─────────────────────────────────────────────────────────────┘
```

---

## 📋 Endpoints Disponibles

### 🔓 Públicos (sin autenticación)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/login` | Login y obtener JWT |
| POST | `/api/auth/validate` | Validar token JWT |
| GET | `/api/public/hello` | Saludo público |
| GET | `/api/public/info` | Info de la API |

### 🔒 Protegidos (requieren JWT)

| Método | Endpoint | Descripción | Roles |
|--------|----------|-------------|-------|
| GET | `/api/secure/data` | Datos protegidos | USER, ADMIN |
| GET | `/api/secure/user-info` | Info del usuario | USER, ADMIN |
| GET | `/api/secure/admin-only` | Solo admin | ADMIN |
| GET | `/api/secure/hello` | Saludo seguro | USER, ADMIN |

---

## 🧪 Ejemplos de Uso

### Con cURL

#### 1. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'
```

**Respuesta:**
```json
{
  "message": "Login exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "expiresIn": 3600,
    "username": "admin"
  },
  "success": true
}
```

#### 2. Acceder a endpoint protegido
```bash
TOKEN="eyJhbGciOiJIUzI1NiJ9..."

curl -X GET http://localhost:8080/api/secure/data \
  -H "Authorization: Bearer $TOKEN"
```

#### 3. Endpoint público (sin token)
```bash
curl -X GET http://localhost:8080/api/public/hello
```

---

## 📊 Descripción de Componentes

### 🔐 SecurityConfig.java
- Configura Spring Security con JWT
- Define usuarios en memoria
- Establece rutas públicas y protegidas
- Configura password encoder (BCrypt)
- Agrega filtro JWT a la cadena de seguridad

### 🎫 JwtService.java
- Genera tokens JWT con claims
- Valida tokens
- Extrae información del token (username, fecha expiración)
- Verifica si token es válido

### 🚪 JwtAuthenticationFilter.java
- Intercepta cada request HTTP
- Extrae token del header `Authorization: Bearer`
- Valida token usando JwtService
- Establece autenticación en SecurityContext

### 🔗 AuthController.java
- `/api/auth/login` - Autentica usuario y devuelve JWT
- `/api/auth/validate` - Valida un token JWT

### 🌐 PublicController.java
- Endpoints accesibles sin autenticación
- No requieren token JWT

### 🔒 SecureController.java
- Endpoints que requieren JWT válido
- Acceso basado en roles (ADMIN, USER)

### 📦 DTOs
- `LoginRequest` - Credenciales de login
- `AuthResponse` - Token JWT generado
- `ApiResponse` - Respuesta estándar del API

---

## 🔧 Configuración Avanzada

### Cambiar tiempo de expiración del token

Editar `application.properties`:
```properties
# 30 minutos
app.jwt.expiration=1800000

# 24 horas
app.jwt.expiration=86400000

# 7 días
app.jwt.expiration=604800000
```

### Usar clave secreta desde variable de entorno

```bash
export JWT_SECRET="my-secure-secret-key-change-in-production"
export JWT_EXPIRATION="3600000"
```

En `application.properties`:
```properties
app.jwt.secret=${JWT_SECRET:defaultSecret}
app.jwt.expiration=${JWT_EXPIRATION:3600000}
```

### Agregar más usuarios

En `SecurityConfig.java`, método `userDetailsService()`:
```java
UserDetails newUser = User.builder()
    .username("newuser")
    .password(passwordEncoder().encode("password123"))
    .roles("USER")
    .build();
```

---

## ✅ Testing Checklist

- [ ] Login exitoso con credenciales válidas
- [ ] Login fallido con credenciales inválidas (401)
- [ ] Acceso a endpoint público sin token
- [ ] Acceso a endpoint protegido con token válido (200)
- [ ] Acceso a endpoint protegido sin token (401)
- [ ] Acceso a endpoint admin-only sin rol ADMIN (403)
- [ ] Validar token JWT
- [ ] Token expirado rechazado

---

## 🚨 Troubleshooting

### Error: Cannot resolve symbol 'jsonwebtoken'
**Solución:** Ejecutar `mvn clean install` para descargar las dependencias de jjwt

### Error: 401 Unauthorized en endpoint protegido
**Causas:**
- Token no enviado en header Authorization
- Token expirado
- Token inválido

**Solución:** Obtener nuevo token con login

### Error: 403 Forbidden en /api/secure/admin-only
**Causa:** Usuario no tiene rol ADMIN

**Solución:** Login con usuario "admin" en lugar de "user"

---

## 📚 Dependencias Principales

```xml
<!-- Spring Boot 4 -->
<spring-boot-starter-security>
<spring-boot-starter-web>

<!-- JWT -->
<io.jsonwebtoken:jjwt-api>
<io.jsonwebtoken:jjwt-impl>
<io.jsonwebtoken:jjwt-jackson>

<!-- Utilidades -->
<org.projectlombok:lombok>
```

---

## 🎓 Conceptos Clave

### JWT (JSON Web Token)
- Token sin estado (stateless)
- Contiene información encriptada (claims)
- Firmado con clave secreta
- Formato: `header.payload.signature`

### Claims
- Información dentro del JWT (username, roles, expiración, etc.)
- No encriptados, pero firmados
- Se pueden extraer sin validar firma (no seguro)

### Bearer Token
- Formato estándar: `Authorization: Bearer <token>`
- El servidor extrae el token después de "Bearer "

### Stateless
- No se almacenan sesiones en servidor
- Cada request contiene toda la información necesaria
- Escalable y distribuido

---

## 🔒 Seguridad en Producción

1. **Cambiar clave secreta** a algo muy largo y aleatorio
2. **Usar HTTPS** en lugar de HTTP
3. **Implementar Refresh Tokens** para renovar tokens
4. **Almacenar usuarios en BD** en lugar de memoria
5. **Validar entrada** (input validation)
6. **Rate limiting** para prevenir fuerza bruta
7. **Rotación de claves** cada cierto tiempo
8. **Logs de auditoría** para movimientos sospechosos

---

## 📖 Recursos Adicionales

- [jjwt Documentation](https://github.com/jwtk/jjwt)
- [Spring Security Docs](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io/) - Debugger y documentación
- [Spring Boot Docs](https://spring.io/projects/spring-boot)

---

## 👨‍💻 Autor

Proyecto educativo para aprender Spring Security y JWT

---

## 📄 Licencia

Este proyecto es de uso educativo.
# Vibe-coding-sobre-Spring-Security-y-JWT
