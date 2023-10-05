package com.example.f23hopper.ui.employee

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.f23hopper.data.shifttype.ShiftType
import com.example.f23hopper.ui.icons.rememberClearNight
import com.example.f23hopper.ui.icons.rememberPartlyCloudyNight
import com.example.f23hopper.ui.icons.rememberSunny
import com.example.f23hopper.ui.icons.rememberWbSunny
import com.example.f23hopper.ui.theme.Purple80
import com.example.f23hopper.ui.theme.PurpleGrey40
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
            WeekendSelector(
                onWeekendValueChange = onEmployeeValueChange,
                employeeDetails = employeeDetails
            )
            ScheduleSelector(
                onScheduleValueChange = onEmployeeValueChange,
                employeeDetails = employeeDetails
            )
            Button(
                onClick = onSaveClick,
                enabled = employeeUiState.isEmployeeValid,
            ) {
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
            validate = { it.matches(Regex("^[0-9-]+$")) },
            errorMessage = "Only numbers are allowed"
        ),
    )

    fields.forEach { field -> ValidatedOutlinedTextField(field) }
}

fun formatPhoneNumber(input: String): String {
    val digits = input.filter { it.isDigit() }
    return when {
        digits.length <= 3 -> digits
        digits.length <= 6 -> "${digits.substring(0, 3)}-${digits.substring(3)}"
        digits.length >= 10 -> "${digits.substring(0, 3)}-${
            digits.substring(
                3,
                6
            )
        }-${digits.substring(6, 10)}"

        else -> "${digits.substring(0, 3)}-${digits.substring(3, 6)}-${digits.substring(6)}"
    }
}

fun formatName(input: String): String {
    return input.filter { it.isLetter() || it == '-' }
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
fun WeekendSelector(
    onWeekendValueChange: (EmployeeDetails) -> Unit = {},
    employeeDetails: EmployeeDetails
) {
    var checkedOpen by remember { mutableStateOf(false) }
    var checkedClose by remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Opening")
        Spacer(modifier = Modifier.padding(start = 5.dp))
        Switch(
            checked = checkedOpen,
            onCheckedChange = {
                checkedOpen = it
                onWeekendValueChange(employeeDetails.copy(canOpen = it))
            },
        )
        Spacer(modifier = Modifier.padding(start = 5.dp))
        Text(text = "Closing")
        Spacer(modifier = Modifier.padding(start = 5.dp))
        Switch(
            checked = checkedClose,
            onCheckedChange = {
                checkedClose = it
                onWeekendValueChange(employeeDetails.copy(canClose = it))
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
        val daysOfWeek = listOf(
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
        )
        daysOfWeek.forEach { day ->
            Spacer(modifier = Modifier.size(10.dp))
            DaySelector(
                dayOfWeek = day
            ) { updatedDay ->
                val updatedEmployeeDetails = when (day) {
                    "Monday" -> employeeDetails.copy(monday = updatedDay)
                    "Tuesday" -> employeeDetails.copy(tuesday = updatedDay)
                    "Wednesday" -> employeeDetails.copy(wednesday = updatedDay)
                    "Thursday" -> employeeDetails.copy(thursday = updatedDay)
                    "Friday" -> employeeDetails.copy(friday = updatedDay)
                    "Saturday" -> employeeDetails.copy(saturday = updatedDay)
                    "Sunday" -> employeeDetails.copy(sunday = updatedDay)
                    else -> employeeDetails
                }
                onScheduleValueChange(updatedEmployeeDetails)
            }
        }
    }
}

@Composable
fun DaySelector(
    dayOfWeek: String,
    onSelectionChange: (ShiftType) -> Unit
) {
    var dayShift by remember { mutableStateOf(false) }
    var nightShift by remember { mutableStateOf(false) }

    val currentShiftType = when {
        dayShift && !nightShift -> ShiftType.DAY
        !dayShift && nightShift -> ShiftType.NIGHT
        dayShift && nightShift -> ShiftType.FULL
        else -> ShiftType.CANT_WORK
    }
    onSelectionChange(currentShiftType)

    Column {
        Text(text = currentShiftType.toString())
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .border(1.dp, Purple80, RoundedCornerShape(5.dp))
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Text(
                text = dayOfWeek,
                color = PurpleGrey40,
                fontSize = 30.sp,
                modifier = Modifier.weight(2f)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(1f)
            ) {
                dayButton("Day", onButtonChange = { dayShift = it })
                dayButton("Night", onButtonChange = { nightShift = it })
            }
        }
    }
}

@Composable
fun dayButton(
    icon: String,
    onButtonChange: (Boolean) -> Unit
) {
    var checked by remember { mutableStateOf(false) }
    val buttonColor = when {
        icon == "Day" && checked -> Color(0xFFdb5edb) // orange for Day
        icon == "Night" && checked -> Color(0xFF792ba6) // blue for Night
        else -> Color(0XFFc7afc7) // grey for inactive
    }

    IconToggleButton(
        checked = checked,
        onCheckedChange = {
            checked = it
            onButtonChange(it)
        },
//        modifier = Modifir
//            .clip(CircleShape)
//            .border(1.dp, PurpleGrey40, CircleShape)
//            .background(Color.White) // Background remains white
//            .size(35.dp),
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
