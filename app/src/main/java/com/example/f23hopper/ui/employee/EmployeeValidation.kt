package com.example.f23hopper.ui.employee

import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.schedule.Shift
import com.example.f23hopper.data.shifttype.ShiftType

val alphaRegex = Regex("^[a-zA-Z-'. ]+$")

fun hasCriticalShifts(employee: Employee, allShifts: List<Shift>): Boolean {
    val shiftsWithEmployee = allShifts.filter { it.employee == employee }

    return shiftsWithEmployee.any { shift ->
        isCriticalShift(employee, shift, allShifts)
    }
}

private fun isCriticalShift(employee: Employee, shift: Shift, allShifts: List<Shift>): Boolean {
    val shiftsOnSameDay = allShifts.filter { it.schedule.date == shift.schedule.date }
    val otherEmployeesOnSameDay = shiftsOnSameDay.filter { it.employee != employee }

    return when (shift.schedule.shiftType) {
        ShiftType.FULL -> isOnlyOpenerOrCloser(employee, otherEmployeesOnSameDay)
        ShiftType.DAY -> isOnlyOpener(employee, otherEmployeesOnSameDay)
        ShiftType.NIGHT -> isOnlyCloser(employee, otherEmployeesOnSameDay)
        else -> false
    }
}

private fun isOnlyOpenerOrCloser(employee: Employee, otherEmployees: List<Shift>): Boolean {
    return isOnlyOpener(employee, otherEmployees) || isOnlyCloser(employee, otherEmployees)
}

private fun isOnlyOpener(employee: Employee, otherEmployees: List<Shift>): Boolean {
    return employee.canOpen && otherEmployees.none { it.employee.canOpen }
}

private fun isOnlyCloser(employee: Employee, otherEmployees: List<Shift>): Boolean {
    return employee.canClose && otherEmployees.none { it.employee.canClose }
}

fun validateInput(uiState: EmployeeDetails): Boolean {
    // Use the validation functions from your Composable for each field
    val isFirstNameValid = uiState.firstName.matches(alphaRegex)
    val isLastNameValid = uiState.lastName.matches(alphaRegex)
    val isNicknameValid = uiState.nickname.isBlank() || uiState.nickname.matches(alphaRegex)
    val isEmailValid = verifyEmail(uiState.email)
    val isPhoneNumberValid = uiState.phoneNumber.matches(Regex("^\\+?[0-9\\-() ]+$"))

    // Return true only if all validations pass
    return isFirstNameValid && isLastNameValid && isNicknameValid && isEmailValid && isPhoneNumberValid
}

fun verifyEmail(email: String): Boolean {
    //  Internet Message Format (RFC 5322) and the domain name criteria defined in RFC 1034 and RFC 1035.

    // maximum length for the local part is 64 characters
    // maximum length for the domain part is 255 characters
    // maximum total length is 320 characters
    if (email.length > 320) return false

    val parts = email.split("@")
    if (parts.size != 2) return false

    val localPart = parts[0]
    val domainPart = parts[1]

    if (localPart.length > 64 || domainPart.length > 255) return false

    // check if the domain part contains at least one dot and doesn't start/end with a dot
    if (!domainPart.contains(".") || domainPart.startsWith(".") || domainPart.endsWith(".")) return false

    // enhanced regex for local part and domain part validation
    val localPartRegex = Regex("^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+\$")
    val domainPartRegex = Regex("^[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

    return localPart.matches(localPartRegex) && domainPart.matches(domainPartRegex)
}

fun generateNickname(): String {
    return listOf(
        "Sparky", "Ace", "Shadow", "Gizmo", "Maverick", "Rogue", "Zeus", "Bandit",
        "Bolt", "Chief", "Dash", "Echo", "Falcon", "Gadget", "Hawk", "Iceman",
        "Jester", "Krypto", "Lynx", "Mystic", "Nebula", "Orion", "Phantom", "Quicksilver",
        "Racer", "Saber", "Titan", "Ulysses", "Vortex", "Wizard", "Xenon", "Yoda",
        "Zephyr", "Blaze", "Cosmo", "Drift", "Eclipse", "Flame", "Glitch", "Hurricane",
        "Inferno", "Jolt", "Knight", "Laser", "Mirage", "Nova", "Omega", "Pulse", "Quantum",
        "Rift"
    ).random()
}
