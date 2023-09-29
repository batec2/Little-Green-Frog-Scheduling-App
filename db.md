# Database

## Employees

- EmployeeID (PK)
- FirstName
- LastName
- Phone
- Email
- CanOpen
- CanClose

## ShiftTypeIDs

- ID (PK)
- Type

| ID  | Type       |
| --- | ---------- |
| 0   | Can't work |
| 1   | Day        |
| 2   | Night      |
| 3   | Full       |

## Availabilites

- EmployeeID
- Sunday
- Monday
- Tuesday
- Wednesday
- Thursday
- Friday
- Saturday

## Schedules

- ID (Autoincrement)
- Date
- EmployeeID (FK)
- ShiftTypeID
