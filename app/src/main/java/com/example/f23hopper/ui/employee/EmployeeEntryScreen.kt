package com.example.f23hopper.ui.employee

//import com.example.f23hopper.utils.StatusBarColorUpdateEffect
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.f23hopper.data.employee.Employee
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.ui.calendar.toolbarColor
import com.example.f23hopper.ui.icons.dayShiftIcon
import com.example.f23hopper.ui.icons.fullShiftIcon
import com.example.f23hopper.ui.icons.nightShiftIcon
import com.example.f23hopper.ui.icons.rememberLock
import com.example.f23hopper.ui.icons.unlockIcon
import com.example.f23hopper.utils.CalendarUtilities.isWeekend
import com.example.f23hopper.utils.StatusBarColorUpdateEffect
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import java.util.Locale

@Composable
fun EmployeeEntryScreen(
    navigateToEmployeeList: () -> Unit,
    sharedViewModel: EmployeeListViewModel,
) {
    StatusBarColorUpdateEffect(toolbarColor)//top status bar colour
    val coroutineScope = rememberCoroutineScope()
    EmployeeEntryBody(
        viewModel = sharedViewModel,
        employeeUiState = sharedViewModel.employeeUiState,
        employeeDetails = sharedViewModel.employeeUiState.employeeDetails,
        onEmployeeValueChange = sharedViewModel::updateUiState,
        onSaveClick = {
            coroutineScope.launch {
                sharedViewModel.saveEmployee()
            }
        },
        navigateToEmployeeList = navigateToEmployeeList,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeEntryBody(
    viewModel: EmployeeListViewModel,
    employeeUiState: EmployeeUiState,
    employeeDetails: EmployeeDetails,
    onEmployeeValueChange: (EmployeeDetails) -> Unit,
    onSaveClick: () -> Unit,
    navigateToEmployeeList: () -> Unit,
    showConfirmationDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
) {

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorScheme.secondaryContainer,
            ),
            title = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Add/Edit")
                }
            },
            navigationIcon = {
                IconButton(onClick = { navigateToEmployeeList() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, contentDescription = "Back To list"
                    )
                }
            },
            actions = {
                ElevatedButton(
                    modifier = Modifier, shape = RoundedCornerShape(10.dp), onClick = {
                        // trigger the onSaveClick action
                        onSaveClick()

                        // only navigate back if the confirmation dialog is not shown
                        if (!showConfirmationDialog.value) {
                            navigateToEmployeeList()
                        }
                    }, enabled = employeeUiState.isEmployeeValid
                ) {
                    Text(text = "Done")
                }
            },
            modifier = Modifier.height(50.dp),
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmployeeInfo(
                onEmployeeInfoChange = onEmployeeValueChange,
                employeeDetails = employeeDetails,
                viewModel = viewModel
            )
            OpenCloseCertificationSelector(
                onCertValueChange = onEmployeeValueChange, employeeDetails = employeeDetails
            )
            ScheduleSelector(
                onDaySelected = viewModel::onDaySelected, employeeDetails = employeeDetails
            )
        }
    }
}


data class FieldDetail(
    val label: String,
    val value: String,
    val modifier: Modifier,
    val onValueChange: (String) -> Unit,
    val validate: (String) -> Boolean,
    val errorMessage: String,
    val formatter: ((String) -> String)? = null, // Optional formatter function
    val showErrorChars: Boolean = true,
    val isNumber: Boolean = false,
)


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmployeeInfo(
    onEmployeeInfoChange: (EmployeeDetails) -> Unit = {},
    employeeDetails: EmployeeDetails,
    viewModel: EmployeeListViewModel
) {
    val focusManager = LocalFocusManager.current
    val handleKeyEvent: (KeyEvent) -> Boolean = {
        when {
            (it.type == KeyEventType.KeyDown && (it.key == Key.Tab || it.key == Key.Enter)) -> {
                focusManager.moveFocus(FocusDirection.Next)
                true
            }

            else -> false
        }
    }
    val hasAnyError = remember { mutableStateOf(false) }

    // dirty way of checking nickname, gathering all employees and checking against each one.
    var employees by remember { mutableStateOf<List<Employee>>(emptyList()) }
    LaunchedEffect(Unit) {
        viewModel.employeeRepository.getAllActiveEmployees().collect { list ->
            employees = list
        }
    }

    val fields = listOf(
        FieldDetail(
            label = "First Name*",
            value = employeeDetails.firstName,
            formatter = ::formatName,
            modifier = Modifier.onPreviewKeyEvent(handleKeyEvent),
            onValueChange = {
                if (!hasAnyError.value) onEmployeeInfoChange(
                    employeeDetails.copy(
                        firstName = it
                    )
                )
            },
            validate = { it.matches(alphaRegex) },
            isNumber = false,
            errorMessage = "Only letters, spaces, and '-. are allowed"
        ),
        FieldDetail(
            label = "Last Name*",
            value = employeeDetails.lastName,
            formatter = ::formatName,
            modifier = Modifier.onPreviewKeyEvent(handleKeyEvent),
            onValueChange = {
                if (!hasAnyError.value) onEmployeeInfoChange(
                    employeeDetails.copy(
                        lastName = it
                    )
                )
            },
            validate = { it.matches(alphaRegex) },
            isNumber = false,
            errorMessage = "Only letters, spaces, and ('-.) are allowed"
        ),
        FieldDetail(
            label = "Nickname*",
            value = employeeDetails.nickname,
            formatter = ::formatName,
            modifier = Modifier.onPreviewKeyEvent(handleKeyEvent),
            onValueChange = {
                if (!hasAnyError.value) onEmployeeInfoChange(
                    employeeDetails.copy(
                        nickname = it
                    )
                )
            },
            validate = {
                employees.none { other -> other.nickname == it && other.employeeId != employeeDetails.employeeId } && it.matches(
                    alphaRegex
                )
            },
            errorMessage = "Nickname already taken",
            isNumber = false,
            showErrorChars = true,
        ),
        FieldDetail(
            label = "Email*",
            formatter = ::formatEmail,
            value = employeeDetails.email,
            modifier = Modifier.onPreviewKeyEvent(handleKeyEvent),
            onValueChange = {
                if (!hasAnyError.value) onEmployeeInfoChange(
                    employeeDetails.copy(
                        email = it
                    )
                )
            },
            validate = { verifyEmail(it) },
            errorMessage = "name@mail.com format accepted",
            isNumber = false,
            showErrorChars = false
        ),

        FieldDetail(
            label = "Phone Number*",
            formatter = ::formatPhoneNumber,
            value = employeeDetails.phoneNumber,
            modifier = Modifier.onPreviewKeyEvent(handleKeyEvent),
            onValueChange = {
                if (!hasAnyError.value) onEmployeeInfoChange(
                    employeeDetails.copy(
                        phoneNumber = it
                    )
                )
            },
            validate = { verifyPhoneNumber(it) },
            errorMessage = "Between 7-15 numbers accepted",
            showErrorChars = false,
            isNumber = true,
        ),

        FieldDetail(
            label = "Max Shifts (per Week)",
            formatter = ::formatMaxShifts,
            value = employeeDetails.maxShifts?.toString() ?: "",
            modifier = Modifier.onPreviewKeyEvent(handleKeyEvent),
            onValueChange = {
                if (!hasAnyError.value) onEmployeeInfoChange(
                    employeeDetails.copy(
                        maxShifts = it.toIntOrNull() ?: 0
                    )
                )
            },
            validate = { validateShiftQty(it.toIntOrNull() ?: 0) },
            errorMessage = "Entered number must be between 1 and 12.",
            showErrorChars = false,
            isNumber = true,
        )
    )

    fields.forEach { field -> ValidatedOutlinedTextField(field, hasAnyError) }
}


@Composable
fun ValidatedOutlinedTextField(
    field: FieldDetail, hasError: MutableState<Boolean>
) {
    // state to hold the current text field value
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = field.value)) }

    // state to determine if the current field has an error
    val isError = remember { mutableStateOf(false) }

    // state to hold the error message
    val errorMessage = remember { mutableStateOf("") }

    // the actual text field
    OutlinedTextField(
        value = textFieldValue,
        modifier = field.modifier,
        onValueChange = { newValue ->
            // format the text if a formatter is provided
            val formattedValue = field.formatter?.invoke(newValue.text) ?: newValue.text

            // update the text field value with the formatted text
            textFieldValue = TextFieldValue(
                text = formattedValue, selection = TextRange(formattedValue.length)
            )
            field.onValueChange(formattedValue)

            // validate the text field value
            val isValid = field.validate(formattedValue)
            isError.value = textFieldValue.text.isNotEmpty() && !isValid
            hasError.value = isError.value // Update the shared error state

            // only call onValueChange if the text field value is valid
            if (isValid) {
                field.onValueChange(formattedValue)
            } else {
                // construct the error message with invalid characters if needed
                val invalidChars = formattedValue.filterNot { field.validate(it.toString()) }
                errorMessage.value = field.errorMessage
                if (field.showErrorChars) {
                    errorMessage.value += ": $invalidChars"
                }
            }
        },
        label = { Text(text = field.label) },
        isError = isError.value,
        supportingText = {
            // display the error message if there's an error
            if (isError.value) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorMessage.value,
                    color = colorScheme.error
                )
            }
        },
        maxLines = 1,
        keyboardOptions = if (field.isNumber) {
            KeyboardOptions(keyboardType = KeyboardType.Number)
        } else {
            KeyboardOptions(keyboardType = KeyboardType.Email, autoCorrect = false)
        },
    )
}

@Composable
fun OpenCloseCertificationSelector(
    onCertValueChange: (EmployeeDetails) -> Unit = {}, employeeDetails: EmployeeDetails
) {
    var checkedOpen by remember { mutableStateOf(employeeDetails.canOpen) }
    var checkedClose by remember { mutableStateOf(employeeDetails.canClose) }

    // Define the primary color and the off color
    val primaryColor = colorScheme.primary
    val offColor = Color.Gray

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Opening Certification Button
        CertificationButton(
            text = "Can Open",
            checked = checkedOpen,
            onCheckedChange = { newValue ->
                checkedOpen = newValue
                onCertValueChange(employeeDetails.copy(canOpen = newValue))
            },
            primaryColor = primaryColor,
            offColor = offColor,
            icon = unlockIcon(),
        )

        Spacer(modifier = Modifier.size(15.dp))

        // Closing Certification Button
        CertificationButton(
            text = "Can Close",
            checked = checkedClose,
            onCheckedChange = { newValue ->
                checkedClose = newValue
                onCertValueChange(employeeDetails.copy(canClose = newValue))
            },
            primaryColor = primaryColor,
            offColor = offColor,
            icon = rememberLock(),
        )
    }
}

@Composable
fun CertificationButton(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    primaryColor: Color,
    offColor: Color,
    icon: ImageVector,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = text, fontSize = 20.sp)
        Spacer(modifier = Modifier.size(5.dp))
        Icon(imageVector = icon,
            contentDescription = text,
            tint = if (checked) primaryColor else offColor,
            modifier = Modifier
                .size(40.dp) // Increase icon size
                .clickable { onCheckedChange(!checked) })
    }
}

@Composable
fun ScheduleSelector(

    onDaySelected: (DayOfWeek, ShiftType) -> Unit,
    employeeDetails: EmployeeDetails
) {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        DayOfWeek.values().forEach { day ->
            Spacer(modifier = Modifier.size(10.dp))
            DaySelector(
                dayOfWeek = day, shiftStatus = when (day) {
                    DayOfWeek.MONDAY -> employeeDetails.monday
                    DayOfWeek.TUESDAY -> employeeDetails.tuesday
                    DayOfWeek.WEDNESDAY -> employeeDetails.wednesday
                    DayOfWeek.THURSDAY -> employeeDetails.thursday
                    DayOfWeek.FRIDAY -> employeeDetails.friday
                    DayOfWeek.SATURDAY -> employeeDetails.saturday
                    DayOfWeek.SUNDAY -> employeeDetails.sunday
                }
            ) { updatedDay ->
                onDaySelected(day, updatedDay)
            }
        }
    }
}


@Composable
fun DaySelector(
    shiftStatus: ShiftType, dayOfWeek: DayOfWeek, onSelectionChange: (ShiftType) -> Unit
) {
    val isWeekend = dayOfWeek.isWeekend()

    var shiftSelected by remember {
        mutableStateOf(shiftStatus == ShiftType.FULL)
    }
    var dayShift by remember {
        mutableStateOf(shiftStatus == ShiftType.DAY || shiftStatus == ShiftType.FULL)
    }
    var nightShift by remember {
        mutableStateOf(shiftStatus == ShiftType.NIGHT || shiftStatus == ShiftType.FULL)
    }

    val currentShiftType = determineShiftType(isWeekend, shiftSelected, dayShift, nightShift)
    onSelectionChange(currentShiftType)

    Box(contentAlignment = Alignment.Center) {
        DayOfWeekTextBox(dayOfWeek, currentShiftType)

        if (isWeekend) {
            WeekendButtonRow(shiftStatus = shiftStatus) { shiftSelected = it }
        } else {
            WeekdayButtonRow(dayShift, nightShift, shiftStatus = shiftStatus) { day, night ->
                dayShift = day
                nightShift = night
            }
        }
    }
}


@Composable
fun WeekendButtonRow(
    shiftStatus: ShiftType,
    onShiftSelectedChanged: (Boolean) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(1.8f))
        DayButton(
            "Full",
            onButtonChange = onShiftSelectedChanged,
            modifier = Modifier.padding(end = 26.dp),
            status = shiftStatus == ShiftType.FULL
        )
    }
}

@Composable
fun WeekdayButtonRow(
    dayShift: Boolean,
    nightShift: Boolean,
    shiftStatus: ShiftType,
    onDayNightShiftChanged: (Boolean, Boolean) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.weight(1.8f))
        DayButton(
            "Day",
            onButtonChange = { onDayNightShiftChanged(it, nightShift) },
            modifier = Modifier.padding(end = 8.dp),
            status = shiftStatus == ShiftType.FULL || shiftStatus == ShiftType.DAY

        )
        DayButton(
            "Night",
            onButtonChange = { onDayNightShiftChanged(dayShift, it) },
            status = shiftStatus == ShiftType.FULL || shiftStatus == ShiftType.NIGHT
        )
    }
}

@Composable
fun DayOfWeekTextBox(dayOfWeek: DayOfWeek, currentShiftType: ShiftType) {
    OutlinedTextField(
        value = dayOfWeek.getDisplayName(
            java.time.format.TextStyle.FULL,
            Locale.getDefault()
        ), // Shows "Sunday" instead of "SUNDAY"
        onValueChange = {}, // no action as this shouldn't be editable
        label = { Text(text = currentShiftType.toString(), fontSize = 16.sp) },
        enabled = false,
        textStyle = TextStyle(fontSize = 24.sp, color = colorScheme.secondary),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
    )
}


@Composable
fun DayButton(
    icon: String, onButtonChange: (Boolean) -> Unit, modifier: Modifier = Modifier, status: Boolean
) {
    var checked by remember { mutableStateOf(status) }
    val buttonColor = when {
        checked -> colorScheme.primary
        else -> Color.LightGray
    }
    IconToggleButton(
        modifier = modifier,
        checked = checked,
        onCheckedChange = {
            checked = it
            onButtonChange(it)
        },
    ) {
        when (icon) {
            "Day" -> Icon(
                imageVector = dayShiftIcon(), contentDescription = "Sun", tint = buttonColor
            )

            "Full" -> Icon(
                imageVector = fullShiftIcon(), contentDescription = "Sun/Moon", tint = buttonColor
            )

            "Night" -> Icon(
                imageVector = nightShiftIcon(), contentDescription = "Moon", tint = buttonColor
            )
        }
    }
}

@Composable
fun DayFilter() {
    remember { mutableStateOf(true) }
    val week = listOf("M", "T", "W", "R", "F", "S", "U")
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Column {
            Row {
                Text(text = "here")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                week.forEach { day ->
                    Button(modifier = Modifier.size(30.dp), onClick = { /*TODO*/ }) {
                        Text(text = day, color = colorScheme.primary)
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                week.forEach { day ->
                    Button(modifier = Modifier.size(30.dp), onClick = { /*TODO*/ }) {
                        Text(text = day, color = Color.White)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmployeeEntryScreenPreview() {
    DayFilter()
}
