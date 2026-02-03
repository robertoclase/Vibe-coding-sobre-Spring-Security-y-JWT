# Spring Security + JWT API

API REST con autenticación JWT usando Spring Boot 4.0.2 y Spring Security 6.

## 🚀 Iniciar la Aplicación

```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

---

## 👥 Usuarios Disponibles

La aplicación viene con 3 usuarios precargados:

| Usuario | Contraseña | Rol    |
|---------|------------|--------|
| admin   | admin      | ADMIN  |
| user    | user       | USER   |
| guest   | guest      | GUEST  |

---

## 📡 Endpoints de la API

### 🔓 Endpoints Públicos (Sin autenticación)

#### 1. Mensaje de Bienvenida
```bash
GET /api/public/hello
```

**Ejemplo con curl:**
```bash
curl http://localhost:8080/api/public/hello
```

**Respuesta:**
```json
{
  "message": "¡Hola! Este es un endpoint público",
  "data": "Acceso permitido sin autenticación",
  "success": true
}
```

#### 2. Información de la API
```bash
GET /api/public/info
```

**Ejemplo con curl:**
```bash
curl http://localhost:8080/api/public/info
```

---

### 🔐 Autenticación

#### 3. Login (Obtener Token JWT)
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin"
}
```

**Ejemplo con curl - Usuario ADMIN:**
```bash
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"admin\",\"password\":\"admin\"}"
```

**Ejemplo con curl - Usuario USER:**
```bash
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"user\",\"password\":\"user\"}"
```

**Ejemplo con curl - Usuario GUEST:**
```bash
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"guest\",\"password\":\"guest\"}"
```

**Respuesta exitosa:**
```json
{
  "message": "Login exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3ODcyMDAwMCwiZXhwIjoxNjc4NzIzNjAwfQ.xxx",
    "type": "Bearer",
    "expiresIn": 3600,
    "username": "admin"
  },
  "success": true
}
```

**Respuesta con credenciales incorrectas:**
```json
{
  "message": "Credenciales inválidas",
  "success": false
}
```

#### 4. Validar Token
```bash
POST /api/auth/validate?token=TU_TOKEN_AQUI
```

**Ejemplo con curl:**
```bash
curl -X POST "http://localhost:8080/api/auth/validate?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

### 🔒 Endpoints Protegidos (Requieren Token JWT)

**IMPORTANTE:** Todos los endpoints protegidos requieren el header:
```
Authorization: Bearer TU_TOKEN_JWT
```

#### 5. Datos Protegidos
```bash
GET /api/secure/data
```

**Ejemplo con curl:**
```bash
curl http://localhost:8080/api/secure/data ^
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

**Respuesta:**
```json
{
  "message": "Acceso a datos protegidos concedido",
  "data": "Usuario autenticado: admin",
  "success": true
}
```

#### 6. Información del Usuario
```bash
GET /api/secure/user-info
```

**Ejemplo con curl:**
```bash
curl http://localhost:8080/api/secure/user-info ^
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

**Respuesta:**
```json
{
  "message": "Información del usuario",
  "data": "Usuario: admin, Roles: [ROLE_ADMIN]",
  "success": true
}
```

#### 7. Solo Administradores
```bash
GET /api/secure/admin-only
```

**Este endpoint SOLO funciona con el usuario ADMIN.**

**Ejemplo con curl (usuario admin):**
```bash
curl http://localhost:8080/api/secure/admin-only ^
  -H "Authorization: Bearer TOKEN_DE_ADMIN"
```

**Respuesta exitosa (con rol ADMIN):**
```json
{
  "message": "Acceso de administrador",
  "data": "Solo admins aquí - Usuario: admin",
  "success": true
}
```

**Respuesta denegada (sin rol ADMIN):**
```
Status: 403 Forbidden
```

#### 8. Saludo Autenticado
```bash
GET /api/secure/hello
```

**Ejemplo con curl:**
```bash
curl http://localhost:8080/api/secure/hello ^
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

---

## 🧪 Flujo Completo de Prueba

### Paso 1: Obtener un Token
```bash
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"admin\",\"password\":\"admin\"}"
```

Copia el valor del campo `token` de la respuesta.

### Paso 2: Probar Endpoint Protegido
Reemplaza `TU_TOKEN_AQUI` con el token copiado:

```bash
curl http://localhost:8080/api/secure/data ^
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

### Paso 3: Probar Endpoint de Admin
```bash
curl http://localhost:8080/api/secure/admin-only ^
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

Si usaste el token de `admin`, funcionará. Si usaste el token de `user` o `guest`, recibirás un error 403.

---

## 🔑 ¿Por qué no puedo registrarme como admin en Postman?

**No existe endpoint de registro** en esta aplicación. Los usuarios están precargados en la base de datos.

Si necesitas más usuarios:
1. Abre el archivo `src/main/resources/import.sql`
2. Agrega nuevas líneas siguiendo el formato existente
3. Las contraseñas deben estar hasheadas con BCrypt
4. Reinicia la aplicación

---

## 🗂️ Estructura de Respuestas

Todas las respuestas siguen el formato `ApiResponse`:

```json
{
  "message": "Descripción del resultado",
  "data": "Datos adicionales (puede ser String, Object, etc.)",
  "success": true/false
}
```

---

## ⚠️ Errores Comunes

### Error 401 Unauthorized
- **Causa:** No enviaste el token o el token es inválido/expirado
- **Solución:** Obtén un nuevo token con `/api/auth/login`

### Error 403 Forbidden
- **Causa:** Tu usuario no tiene permisos para ese endpoint
- **Solución:** Usa un usuario con el rol adecuado (ej: `admin` para `/api/secure/admin-only`)

### Error 400 Bad Request
- **Causa:** JSON mal formado en el body
- **Solución:** Verifica la sintaxis del JSON

### Token Expirado
- **Los tokens expiran en 1 hora (3600 segundos)**
- **Solución:** Genera un nuevo token con `/api/auth/login`

---

## 📝 Importar en Postman

1. Abre Postman
2. Click en **Import**
3. Selecciona el archivo `Postman_Collection.json`
4. Ya tienes todos los endpoints configurados

**Para usar el token automáticamente:**
1. Después de hacer login, copia el token de la respuesta
2. En Postman, ve a **Authorization** tab
3. Selecciona **Bearer Token**
4. Pega el token

O mejor aún, crea una variable de entorno:
1. Click en el ícono del ojo (Environment quick look)
2. Agregar variable: `jwt_token`
3. Después del login, guarda el token en esta variable
4. Usa `{{jwt_token}}` en el campo de Authorization

---

## 🛠️ Tecnologías Utilizadas

- Spring Boot 4.0.2
- Spring Security 6
- JWT (jjwt 0.12.1)
- H2 Database (en memoria)
- BCrypt para contraseñas
- Lombok

---

## 🔒 Seguridad

- ✅ Contraseñas hasheadas con BCrypt
- ✅ Tokens JWT firmados con HMAC-SHA256
- ✅ Sesiones STATELESS (sin cookies)
- ✅ CSRF deshabilitado (apropiado para APIs)
- ✅ Autorización basada en roles
- ✅ Token expira en 1 hora

---

## 📦 Base de Datos

La aplicación usa H2 en memoria. Los datos se cargan desde `import.sql` al iniciar.

**Acceder a la consola H2:**
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: (dejar en blanco)

---

## 💡 Tips para Desarrolladores

### Ver logs detallados
El nivel de log está configurado en `DEBUG` para el paquete principal.

### Cambiar tiempo de expiración del token
Edita `application.properties`:
```properties
app.jwt.expiration=7200000
```

### Cambiar puerto
```properties
server.port=9090
```

---

## ✅ Checklist de Pruebas

- [ ] Endpoint público funciona sin token
- [ ] Login con `admin/admin` genera token
- [ ] Login con `user/user` genera token
- [ ] Login con credenciales incorrectas devuelve 401
- [ ] Endpoint protegido funciona con token válido
- [ ] Endpoint protegido devuelve 401 sin token
- [ ] `/api/secure/admin-only` funciona con token de admin
- [ ] `/api/secure/admin-only` devuelve 403 con token de user
- [ ] Token expira después de 1 hora
- [ ] Validar token funciona correctamente

---

## 🎯 Ejemplos PowerShell

Si estás en Windows con PowerShell:

```powershell
# Login
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -ContentType "application/json" -Body '{"username":"admin","password":"admin"}'
$token = $response.data.token

# Usar el token
Invoke-RestMethod -Uri "http://localhost:8080/api/secure/data" -Headers @{Authorization="Bearer $token"}

# Admin endpoint
Invoke-RestMethod -Uri "http://localhost:8080/api/secure/admin-only" -Headers @{Authorization="Bearer $token"}
```

---

**¡Tu API está lista para usar! 🚀**
