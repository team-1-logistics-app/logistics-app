# Logistics Management System

A structured console-based logistics management system built in Java.

The application simulates real-world logistics operations including route creation,
truck assignment, package management, and role-based command execution.

## Project Structure

```
logistics-app/
├── src/                          # Application source code
│   └── com.austria.logistics/
│       ├── commands/             # Command Pattern implementation (Create, Assign, Show, User, etc.)
│       ├── constants/            # Centralized system messages and reusable string templates
│       ├── core/                 # Engine, Repository, CommandFactory and system orchestration logic
│       ├── exceptions/           # Custom domain exception hierarchy (extends LogisticsAppException)
│       ├── models/               # Domain entities, interfaces, enums and implementations
│       ├── utils/                # Validators, parsers and helper utilities
│       └── Startup.java          # Application entry point
│
├── tests/                        # JUnit 5 unit tests (commands, edge cases, exception scenarios)
│   └── com.austria.logistics/
│
├── saves/                        # Runtime-generated directory created upon execution of the Save command.
│                                 # Used for file-based persistence and data restoration.
│
├── README.md                     # Project documentation
└── .gitignore                    # Git ignored files configuration
```
## Architecture Overview

The application is structured around the Command Pattern.

User input is processed by the Engine, which delegates execution
to specific command classes via the CommandFactory.

Core architectural components:

- Engine - Controls the execution loop
- CommandFactory - Resolves command types dynamically
- Repository - Centralized in-memory data storage
- Domain Models - Encapsulate business logic and validation
- Custom Exception Hierarchy - Centralized error handling

## Supported Commands

## Creation

### `CreatePackage`
Creates a new package.

```
CreatePackage <StartLocation> <EndLocation> <Weight> <ContactInfo>
```

| Argument       | Type   | Description |
|---------------|--------|------------|
| StartLocation | String | Departure city |
| EndLocation   | String | Destination city |
| Weight        | String | Package weight |
| ContactInfo   | String | Contact email or information |

---

### `CreateRoute`
Creates an empty route.

```
CreateRoute
```

No arguments required.

---

## Assignment

### `AssignLocation`
Assigns a location to an existing route.

**First location:**
```
AssignLocation <RouteId> <City> <DepartureTime>
```

| Argument       | Description |
|---------------|------------|
| RouteId       | Route identifier |
| City          | City name |
| DepartureTime | Format: `MMM d HH:mm` (e.g. Feb 20 13:00) |

**Additional locations:**
```
AssignLocation <RouteId> <City>
```

---

### `AssignTruck`

```
AssignTruck <RouteId> <TruckType>
```

| Argument  | Description |
|----------|------------|
| RouteId  | Route identifier |
| TruckType| Enum value (e.g. SCANIA, MAN, ACTROS) |

---

### `AssignPackage`

```
AssignPackage <PackageId> <TruckId>
```

| Argument  | Description |
|----------|------------|
| PackageId| Package identifier |
| TruckId  | Truck identifier |

---

##  Unassignment

### `UnassignLocation`

```
UnassignLocation <RouteId> <City>
```

---

### `UnassignTruck`

```
UnassignTruck <TruckId> <RouteId>
```

---

### `UnassignPackage`

```
UnassignPackage <PackageId>
```

---

## Display

### `ShowRoute`
```
ShowRoute <RouteId>
```

### `ShowPackage`
```
ShowPackage <PackageId>
```

### `ShowPackages`
```
ShowPackages
```

### `ShowTrucks`
```
ShowTrucks
```

### `ShowUsers`
```
ShowUsers
```

---

## User Management

### `Register`
```
Register <Username> <FirstName> <LastName> <Password> <Email> [UserRole]
```

| Argument  | Required | Description |
|-----------|----------|------------|
| Username  | Yes      | Unique username used for authentication |
| FirstName | Yes      | User first name |
| LastName  | Yes      | User last name |
| Password  | Yes      | User password (stored and validated by the system) |
| Email     | Yes      | Valid email address used for contact and mailbox |
| UserRole  | Optional | Defines user permissions: CUSTOMER (default), EMPLOYEE, or MANAGER |
If no UserRole is provided, the system assigns CUSTOMER by default.

---

## Persistence

### `Save`
```
Save
```

**Description:**
- Serializes the current application state.
- Automatically creates the `saves/` directory if it does not exist.
- Stores all routes, packages, trucks, and users in separate files.
- Enables restoring the system state later.

---

### `Load`
```
Load
```

**Description:**
- Reads previously saved data from the `saves/` directory.
- Restores routes, packages, trucks, users, and their relationships.
- Overwrites the current in-memory state with the loaded data.
---

## Role-Based Command Access

The system enforces role-based access control for command execution.

### Customer
- `CreatePackage`

Customers are allowed to create packages but cannot manage routes,
assign trucks, or execute administrative operations.

---

### Employee
Can execute all operational logistics commands, including:
- `CreatePackage`
- `CreateRoute`
- `AssignLocation`
- `AssignTruck`
- `AssignPackage`
- `UnassignLocation`
- `UnassignTruck`
- `UnassignPackage`
- `ShowRoute`
- `ShowPackage`
- `ShowPackages`
- `ShowTrucks`

Employees cannot:
- Execute `ShowUsers`
---

### Manager
Managers have full operational access.

Can execute:
- All Employee commands
- `ShowUsers`

Managers can oversee system-wide user information and logistics operations.

---

### Commands That Do NOT Require Login

The following commands can be executed without authentication:

- `Save`
- `Load`

These commands handle file-based persistence and do not require user privileges.

---

### Authentication Rule

To execute any operational command (except `Save` and `Load`),
a valid login session is required.

If no user is logged in, the system denies command execution.
