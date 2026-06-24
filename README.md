# Tower Defense Progress API

Backend REST API built with **Spring Boot** for a Unity Tower Defense game.

This backend handles user authentication, JWT security, password hashing, and per-account player progress.  
Each player has their own unlocked levels and stars earned for every level.

---

## Features

- User registration
- User login
- JWT authentication
- BCrypt password hashing
- Per-user player progress
- Level unlock system
- Star saving system
- Reset progress endpoint
- Input validation
- Global exception handling
- MySQL database integration
- Unity client integration

---

## Tech Stack

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- JWT
- BCrypt
- Maven

---

## How It Works

After a player registers or logs in, the backend returns a JWT token.

The Unity client stores this token locally and sends it with every protected request using the `Authorization` header:

```http
Authorization: Bearer <token>
```

The backend validates the token, identifies the current user, and loads or updates only that user's progress.

---

## Database Structure

The backend uses three main entities:

### User

Stores account information.

| Field | Description |
|---|---|
| id | User ID |
| username | Unique username |
| password | BCrypt hashed password |

---

### PlayerProgress

Stores the general progress of a player.

| Field | Description |
|---|---|
| id | Progress ID |
| maxLevelUnlocked | Highest unlocked level |
| user | The user that owns this progress |

---

### LevelProgress

Stores the stars earned by a player on each level.

| Field | Description |
|---|---|
| id | Level progress ID |
| levelNumber | Level number |
| starUnlocked | Stars earned on that level |
| user | The user that owns this level progress |

---

## API Endpoints

---

# Authentication

## Register

```http
POST /api/auth/register
```

### Request body

```json
{
  "username": "cosmin",
  "password": "1234"
}
```

### Response

```json
{
  "token": "jwt_token_here"
}
```

When a new user registers, the backend automatically creates:

- a player progress entry with `maxLevelUnlocked = 1`
- 10 level progress entries
- all levels with `0` stars

---

## Login

```http
POST /api/auth/login
```

### Request body

```json
{
  "username": "cosmin",
  "password": "1234"
}
```

### Response

```json
{
  "token": "jwt_token_here"
}
```

---

# Player Progress

All player progress endpoints require a valid JWT token.

Example header:

```http
Authorization: Bearer <token>
```

---

## Get Player Progress

```http
GET /api/player/progress
```

### Response example

```json
{
  "maxLevelUnlocked": 2,
  "levels": [
    {
      "levelNumber": 1,
      "stars": 3
    },
    {
      "levelNumber": 2,
      "stars": 0
    },
    {
      "levelNumber": 3,
      "stars": 0
    }
  ]
}
```

The Unity client uses this endpoint after login or auto-login to load the player's progress.

---

## Update Level Result

```http
PUT /api/player/levels/{levelNumber}
```

Example:

```http
PUT /api/player/levels/1
```

### Request body

```json
{
  "stars": 3
}
```

### Rules

- `levelNumber` must be between `1` and `10`
- `stars` must be between `1` and `3`
- locked levels cannot be updated
- stars can only improve, they do not decrease
- completing the current max unlocked level unlocks the next level
- max level cannot go above level `10`

Example:

If the player has:

```json
{
  "maxLevelUnlocked": 1
}
```

and completes level 1, the backend updates the progress to:

```json
{
  "maxLevelUnlocked": 2
}
```

---

## Reset Progress

```http
POST /api/player/progress/reset
```

This resets only the currently authenticated user's progress.

It sets:

```text
maxLevelUnlocked = 1
all level stars = 0
```

### Response example

```json
{
  "maxLevelUnlocked": 1,
  "levels": [
    {
      "levelNumber": 1,
      "stars": 0
    },
    {
      "levelNumber": 2,
      "stars": 0
    },
    {
      "levelNumber": 3,
      "stars": 0
    }
  ]
}
```

---

## Validation Rules

---

## Username

Usernames must:

- be between `3` and `20` characters
- contain only letters, numbers and underscore
- not contain spaces

Valid examples:

```text
cosmin
cosmin123
cosmin_boss
```

Invalid examples:

```text
cosmin boss
cosmin-boss
cosmin@boss
```

---

## Password

Passwords must:

- be between `4` and `30` characters
- not contain spaces

Valid examples:

```text
1234
password123
my_password
```

Invalid examples:

```text
123 4
 password
password 
```

---

## Error Examples

### Username already exists

```json
{
  "message": "Username already exists"
}
```

---

### Invalid credentials

```json
{
  "message": "Invalid username or password"
}
```

---

### Locked level

```json
{
  "message": "Level is locked"
}
```

---

### Invalid level

```json
{
  "message": "Invalid level"
}
```

---

### Validation error

```json
{
  "username": "Username can only contain letters, numbers and underscore"
}
```

---

## Example Flow

### 1. Register a new player

```http
POST /api/auth/register
```

The backend returns a JWT token.

---

### 2. Unity saves the token locally

The Unity client stores the token in `PlayerPrefs`.

---

### 3. Unity loads player progress

```http
GET /api/player/progress
```

The backend returns the player's unlocked levels and stars.

---

### 4. Player completes a level

```http
PUT /api/player/levels/1
```

Request body:

```json
{
  "stars": 3
}
```

The backend saves the result and unlocks the next level if needed.

---

### 5. Unity updates the local game session

The Unity client updates its local `GameSession.Progress` with the backend response.

---

## Application Properties Example

Example `application.properties`:

```properties
spring.application.name=tower-defense-progress-api

spring.datasource.url=jdbc:mysql://localhost:3306/tower_defense_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=your_secret_key_must_be_long_enough
```

Do not expose real production database passwords or JWT secrets in a public repository.

---

## Running the Project

Clone the repository:

```bash
git clone <repository-url>
cd tower-defense-progress-api
```

Run the project with Maven:

```bash
mvn spring-boot:run
```

Or build the project:

```bash
mvn clean package
```

Then run the generated `.jar` file:

```bash
java -jar target/tower-defense-progress-api-0.0.1-SNAPSHOT.jar
```

By default, the API runs on:

```text
http://localhost:8080
```

---

## Unity Integration

The Unity client connects to the backend using:

```csharp
http://localhost:8080
```

After login or register, Unity stores the JWT token and loads the player's progress:

```http
GET /api/player/progress
```

When a level is completed, Unity sends:

```http
PUT /api/player/levels/{levelNumber}
```

When the player resets progress, Unity sends:

```http
POST /api/player/progress/reset
```

For a real release, the backend must be hosted online because `localhost` only works on the developer's machine.

---

## Local Development Notes

This backend is currently intended for local development and testing with the Unity client.

The full system includes:

```text
Unity Client
     ↓
Spring Boot REST API
     ↓
Spring Security + JWT
     ↓
MySQL Database
```

---

## Project Status

Current version includes:

- complete account system
- JWT login and register
- password hashing with BCrypt
- per-user progress
- level unlocking
- star saving
- reset progress
- Unity integration
- auto-login support on the Unity side
- sign out support on the Unity side

This backend is ready for local development and testing with the Unity Tower Defense client.
