package com.example.f23hopper.ui.employee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.f23hopper.data.DayOfWeek
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.ui.icons.rememberPartlyCloudyNight
import com.example.f23hopper.ui.icons.rememberWbSunny
import kotlinx.coroutines.launch

@Composable
fun EmployeeEntryScreen() {
    val coroutineScope = rememberCoroutineScope()
    val viewModel = hiltViewModel<EmployeeEntryViewModel>()
    EmployeeEntryBody(
        employeeUiState = viewModel.employeeUiState,
        employeeDetails = viewModel.employeeUiState.employeeDetails,
        onEmployeeValueChange = viewModel::updateUiState,
        onSaveClick = {
            coroutineScope.launch {
                viewModel.saveEmployee()
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeEntryBody(
    employeeUiState: EmployeeUiState,
    employeeDetails: EmployeeDetails,
    onEmployeeValueChange: (EmployeeDetails) -> Unit,
    onSaveClick: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmployeeInfo(
                onEmployeeInfoChange = onEmployeeValueChange,
                employeeDetails = employeeDetails
            )
            OpenCloseCertificationSelector(
                onCertValueChange = onEmployeeValueChange,
                employeeDetails = employeeDetails
            )
            ScheduleSelector(
                onScheduleValueChange = onEmployeeValueChange,
                employeeDetails = employeeDetails
            )
            Button(onClick = onSaveClick, enabled = employeeUiState.isEmployeeValid) {
                Text(text = "Add")
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmployeeInfo(
    onEmployeeInfoChange: (EmployeeDetails) -> Unit = {},
    employeeDetails: EmployeeDetails
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

    val fields = listOf(
        FieldDetail(
            label = "First Name",
            value = employeeDetails.firstName,
            modifier = Modifier.onPreviewKeyEvent(handleKeyEvent),
            onValueChange = { onEmployeeInfoChange(employeeDetails.copy(firstName = it)) },
            validate = { it.matches(Regex("^[a-zA-Z-]+$")) },
            errorMessage = "Only letters and hyphens are allowed"
        ),
        FieldDetail(
            label = "Last Name",
            value = employeeDetails.lastName,
            modifier = Modifier.onPreviewKeyEvent(handleKeyEvent),
            onValueChange = { onEmployeeInfoChange(employeeDetails.copy(lastName = it)) },
            validate = { it.matches(Regex("^[a-zA-Z-]+$")) },
            errorMessage = "Only letters and hyphens are allowed"
        ),
        FieldDetail(
            label = "Email",
            value = employeeDetails.email,
            modifier = Modifier.onPreviewKeyEvent(handleKeyEvent),
            onValueChange = { onEmployeeInfoChange(employeeDetails.copy(email = it)) },
            validate = { it.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")) },
            errorMessage = "name@mail.com format accepted"
        ),

        FieldDetail(
            label = "Phone Number",
            formatter = ::formatPhoneNumber,
            value = employeeDetails.phoneNumber,
            modifier = Modifier.onPreviewKeyEvent(handleKeyEvent),
            onValueChange = { onEmployeeInfoChange(employeeDetails.copy(phoneNumber = it)) },
            validate = { it.matches(Regex("^\\+?[0-9\\-() ]+$")) },
            errorMessage = "Only numbers are allowed"
        ),
    )

    fields.forEach { field -> ValidatedOutlinedTextField(field) }
}

data class FieldDetail(
    val label: String,
    val value: String,
    val modifier: Modifier,
    val onValueChange: (String) -> Unit,
    val validate: (String) -> Boolean,
    val errorMessage: String,
    val formatter: ((String) -> String)? = null // Optional formatter function
)


@Composable
fun ValidatedOutlinedTextField(field: FieldDetail) {
    val isError = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = field.value)) }

    OutlinedTextField(
        value = textFieldValue,
        modifier = field.modifier,
        onValueChange = { newValue ->
            var formattedValue = newValue.text
            if (field.formatter != null) {
                formattedValue = field.formatter.invoke(newValue.text)
                textFieldValue = TextFieldValue(
                    text = formattedValue,
                    selection = TextRange(formattedValue.length)
                )
            } else {
                textFieldValue = newValue
            }
            field.onValueChange(formattedValue)
            val isValid = field.validate(textFieldValue.text)
            isError.value = textFieldValue.text.isNotEmpty() && !isValid
            if (!isValid) {
                val invalidChars = textFieldValue.text.filterNot { field.validate(it.toString()) }
                errorMessage.value = "${field.errorMessage}: $invalidChars"
            }
        },
        label = { Text(text = field.label) },
        isError = isError.value,
        supportingText = {
            if (isError.value) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorMessage.value,
                    color = colorScheme.error
                )
            }
        }
    )
}

@Composable
fun OpenCloseCertificationSelector(
    onCertValueChange: (EmployeeDetails) -> Unit = {},
    employeeDetails: EmployeeDetails
) {
    var checkedOpen by remember { mutableStateOf(false) }
    var checkedClose by remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Opening", fontSize = 20.sp)
        Spacer(modifier = Modifier.padding(start = 5.dp))
        Switch(
            checked = checkedOpen,
            onCheckedChange = {
                checkedOpen = it
                onCertValueChange(employeeDetails.copy(canOpen = it))
            },
        )
        Spacer(modifier = Modifier.padding(start = 5.dp))
        Text(text = "Closing", fontSize = 20.sp)
        Spacer(modifier = Modifier.padding(start = 5.dp))
        Switch(
            checked = checkedClose,
            onCheckedChange = {
                checkedClose = it
                onCertValueChange(employeeDetails.copy(canClose = it))
            },
        )

    }
}


@Composable
fun ScheduleSelector(
    onScheduleValueChange: (EmployeeDetails) -> Unit = {},
    employeeDetails: EmployeeDetails
) {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        DayOfWeek.values().forEach { day ->
            Spacer(modifier = Modifier.size(10.dp))
            DaySelector(
                dayOfWeek = day
            ) { updatedDay ->
                val updatedEmployeeDetails = when (day) {
                    DayOfWeek.MONDAY -> employeeDetails.copy(monday = updatedDay)
                    DayOfWeek.TUESDAY -> employeeDetails.copy(tuesday = updatedDay)
                    DayOfWeek.WEDNESDAY -> employeeDetails.copy(wednesday = updatedDay)
                    DayOfWeek.THURSDAY -> employeeDetails.copy(thursday = updatedDay)
                    DayOfWeek.FRIDAY -> employeeDetails.copy(friday = updatedDay)
                    DayOfWeek.SATURDAY -> employeeDetails.copy(saturday = updatedDay)
                    DayOfWeek.SUNDAY -> employeeDetails.copy(sunday = updatedDay)
                }
                onScheduleValueChange(updatedEmployeeDetails)
            }
        }
    }
}

@Composable
fun DaySelector(
    dayOfWeek: DayOfWeek,
    onSelectionChange: (ShiftType) -> Unit
) {
    val isWeekend = dayOfWeek.isWeekend()

    var shiftSelected by remember { mutableStateOf(false) }
    var dayShift by remember { mutableStateOf(false) }
    var nightShift by remember { mutableStateOf(false) }

    val currentShiftType = determineShiftType(isWeekend, shiftSelected, dayShift, nightShift)
    onSelectionChange(currentShiftType)

    Box(contentAlignment = Alignment.Center) {
        DayOfWeekTextBox(dayOfWeek, currentShiftType)

        if (isWeekend) {
            WeekendButtonRow { shiftSelected = it }
        } else {
            WeekdayButtonRow(dayShift, nightShift) { day, night ->
                dayShift = day
                nightShift = night
            }
        }
    }
}

@Composable
fun WeekendButtonRow(onShiftSelectedChanged: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(1.8f))
        DayButton(
            "Day",
            onButtonChange = onShiftSelectedChanged,
            modifier = Modifier.padding(end = 26.dp)
        )
    }
}

@Composable
fun WeekdayButtonRow(
    dayShift: Boolean,
    nightShift: Boolean,
    onDayNightShiftChanged: (Boolean, Boolean) -> Unit
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
            modifier = Modifier.padding(end = 8.dp)
        )
        DayButton("Night", onButtonChange = { onDayNightShiftChanged(dayShift, it) })
    }
}

@Composable
fun DayOfWeekTextBox(dayOfWeek: DayOfWeek, currentShiftType: ShiftType) {
    OutlinedTextField(
        value = dayOfWeek.toString(),
        onValueChange = {}, // no action as this shouldn't be editable
        label = { Text(text = currentShiftType.toString(), fontSize = 16.sp) },
        enabled = false,
        textStyle = TextStyle(fontSize = 24.sp, color = Color.DarkGray),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
    )
}


@Composable
fun DayButton(
    icon: String,
    onButtonChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var checked by remember { mutableStateOf(false) }
    val buttonColor = when {
        icon == "Day" && checked -> colorScheme.primary
        icon == "Night" && checked -> colorScheme.primary
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
                imageVector = rememberWbSunny(),
                contentDescription = "Sun",
                tint = buttonColor
            )

            "Night" -> Icon(
                imageVector = rememberPartlyCloudyNight(),
                contentDescription = "Moon",
                tint = buttonColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmployeeEntryScreenPreview() {
    //ScheduleSelector()
}
