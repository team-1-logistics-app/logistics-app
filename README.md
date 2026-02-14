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

##  How to Run

1. Clone the repository
2. Open the project in IntelliJ IDEA
3. Run `Startup.java`
4. Use console commands to interact with the system

## Operational Workflow
- Customer → CreatePackage
- Employee/Manager → CreateRoute → AssignLocations to the route → AssignTruck to the route → AssignPackage to the route

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
### `Login`
```
Login <Username> <Password>
```

### `Logout`
```
Logout
```

### `ReadMail`
```
ReadMail
```

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
## System

### `Exit`

**Description:**
- Terminates the application.
- Stops the command processing loop.
- Closes the program gracefully.
- Ensures no further commands are accepted after execution.
---
## Role-Based Command Access

The system enforces role-based access control for command execution.

### Customer
- `CreatePackage`
- `ReadMail`

Customers are allowed to create packages and read their mail but cannot manage routes,
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
- `Register`
- `Login`
- `Logout`
- `Save`
- `Load`
- `Exit`

---

### Authentication Rule

To execute any operational command (except `Register`,`Login`,`Logout`,`Save`,`Load`,`Exit`),
a valid login session is required.

If no user is logged in, the system denies command execution.

---

---

## Full System Test Scenario (Extended)

This scenario demonstrates:

- Role-based access control
- Valid and invalid operations
- Route and truck assignment validation
- Package assignment validation
- Show commands
- Unassignment logic
- Persistence (Save / Load)
- Mailbox functionality

---

## Test Input

```text
Register john John Doe 123456 john@email.com CUSTOMER
Login john 123456
CreateRoute
CreatePackage Sydney Darwin 20 john@email.com
CreatePackage Sydney Melbourne 30 john@email.com
Logout
Register emp1 Ivan Petrov emp123 emp@email.com EMPLOYEE
Login emp1 emp123
CreateRoute
AssignTruck 3 SCANIA
AssignLocation 3 Sydney Feb 20 13:00
AssignTruck 3 SCANIA
AssignLocation 3 Darwin
AssignTruck 3 SCANIA
AssignTruck 3 SCANIA
AssignPackage 1 1001
AssignPackage 1 1001
AssignPackage 2 1001
ShowUsers
ShowRoute 3
ShowPackages
ShowTrucks
ShowPackage 1
ShowPackage 2
Logout
Register boss Anna Smith admin123 boss@email.com MANAGER
Login boss admin123
ShowUsers
UnassignPackage 999
UnassignPackage 1
UnassignTruck 1001 3
UnassignTruck 1001 3
UnassignLocation 3 Sydney
UnassignLocation 3 Darwin
UnassignLocation 3 Darwin
UnassignLocation 3 Sydney
AssignLocation 3 Melbourne Feb 24 16:00
Logout
AssignLocation 3 Darwin
Save
Load
Login john 123456
ReadMail
Logout
```

---

## Expected Output

```text
User john registered successfully!
####################
User john successfully logged in!
####################
You are not logged in as manager or employee!
####################
Package with id 1 was created!
####################
Package with id 2 was created!
####################
You logged out!
####################
User emp1 registered successfully!
####################
User emp1 successfully logged in!
####################
Route with id 3 was created!
####################
Route with id 3 doesn't have enough locations assigned yet, please assign at least 2 locations before assigning truck to it.
####################
Sydney is successfully added to route with id 3 .
####################
Route with id 3 doesn't have enough locations assigned yet, please assign at least 2 locations before assigning truck to it.
####################
Darwin is successfully added to route with id 3 .
####################
Truck Scania with id 1001 was assigned to route with id 3!
####################
Route with id 3 is already has assigned truck!
####################
Package with id 1 was assigned to truck Scania with id 1001!
####################
Package with id 1 is already assigned to truck Scania with id 1001
####################
Melbourne is not in the route.
####################
You are not logged in as manager!
####################
Current schedule for route with id 3:
The route has assigned truck Scania with id 1001.
City: Sydney, Scheduled time: Feb 20 13:00
City: Darwin, Scheduled time: Feb 22 10:14
####################
Package with id 1, start location Sydney, end location Darwin, weight 20, contact info john@email.com is assigned to truck Scania with id 1001. Estimated arrival time is: Feb 22 10:14
Package with id 2, start location Sydney, end location Melbourne, weight 30, contact info john@email.com is not assigned to a truck yet.
####################
Scania with id 1001 is assigned to route with id 3, current weight is 20 kg and max capacity is 42000 kg
Scania with id 1002 is not assigned.
Scania with id 1003 is not assigned.
Scania with id 1004 is not assigned.
Scania with id 1005 is not assigned.
Scania with id 1006 is not assigned.
Scania with id 1007 is not assigned.
Scania with id 1008 is not assigned.
Scania with id 1009 is not assigned.
Scania with id 1010 is not assigned.
Man with id 1011 is not assigned.
Man with id 1012 is not assigned.
Man with id 1013 is not assigned.
Man with id 1014 is not assigned.
Man with id 1015 is not assigned.
Man with id 1016 is not assigned.
Man with id 1017 is not assigned.
Man with id 1018 is not assigned.
Man with id 1019 is not assigned.
Man with id 1020 is not assigned.
Man with id 1021 is not assigned.
Man with id 1022 is not assigned.
Man with id 1023 is not assigned.
Man with id 1024 is not assigned.
Man with id 1025 is not assigned.
Actros with id 1026 is not assigned.
Actros with id 1027 is not assigned.
Actros with id 1028 is not assigned.
Actros with id 1029 is not assigned.
Actros with id 1030 is not assigned.
Actros with id 1031 is not assigned.
Actros with id 1032 is not assigned.
Actros with id 1033 is not assigned.
Actros with id 1034 is not assigned.
Actros with id 1035 is not assigned.
Actros with id 1036 is not assigned.
Actros with id 1037 is not assigned.
Actros with id 1038 is not assigned.
Actros with id 1039 is not assigned.
Actros with id 1040 is not assigned.
####################
Package with id 1, start location Sydney, end location Darwin, weight 20, contact info john@email.com is assigned to truck Scania with id 1001. Estimated arrival time is: Feb 22 10:14
####################
Package with id 2, start location Sydney, end location Melbourne, weight 30, contact info john@email.com is not assigned to a truck yet.
####################
You logged out!
####################
User boss registered successfully!
####################
User boss successfully logged in!
####################
Username: john, First Name: John, Last Name: Doe, Password: 123456, Email: john@email.com, User Role: Customer
Username: emp1, First Name: Ivan, Last Name: Petrov, Password: emp123, Email: emp@email.com, User Role: Employee
Username: boss, First Name: Anna, Last Name: Smith, Password: admin123, Email: boss@email.com, User Role: Manager
####################
No record with id 999 in the repository
####################
Package with id 1 is successfully unassigned!
####################
Truck Scania with id 1001 is successfully unassigned to route with id 3
####################
Truck Scania with id 1001 is not assigned to route with id 3
####################
You cannot remove the starting location in the route, before every other location is removed!
####################
Darwin is successfully removed from route with id 3 .
####################
Darwin is not in the route.
####################
Sydney is successfully removed from route with id 3 .
####################
Melbourne is successfully added to route with id 3 .
####################
You logged out!
####################
You are not logged in! Please login first!
####################
State successfully saved to file!
####################
Loaded state from file!
####################
User john successfully logged in!
####################
MESSAGE#1 - Package with id 1, start location Sydney, end location Darwin, weight 20, contact info john@email.com is assigned to truck Scania with id 1001. Estimated arrival time is: Feb 22 10:14
MESSAGE#2 - Package with id 2, start location Sydney, end location Melbourne, weight 30, contact info john@email.com is not assigned to a truck yet.
####################
You logged out!
####################
```