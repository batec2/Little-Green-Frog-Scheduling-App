package com.example.f23hopper.ui.employee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.f23hopper.data.shifttype.ShiftType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
    Scaffold(
        topBar = {
            TopAppBar(title = {})
        }
    ) { innerPadding ->
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

    // Phone number has autoformatting, so its handled with a tracked TextFieldValue to keep track of
    // the cursor position when formatted
    var phoneNumberState by remember { mutableStateOf(TextFieldValue(text = employeeDetails.phoneNumber)) }
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
            label = "Phone Number",
            formatter = ::formatPhoneNumber, // Pass reference to format function
            value = phoneNumberState.text,
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
        digits.length >= 10 -> "${digits.substring(0, 3)}-${digits.substring(3, 6)}-${digits.substring(6, 10)}"
        else -> "${digits.substring(0, 3)}-${digits.substring(3, 6)}-${digits.substring(6)}"
    }
}

data class FieldDetail(
    val label: String,
    val value: String,
    val modifier: Modifier,
    val onValueChange: (String) -> Unit,
    val validate: (String) -> Boolean,
    val errorMessage: String,
    val formatter: ((String) -> String)? = null // optional formatter function
)


@Composable
fun ValidatedOutlinedTextField(field: FieldDetail) {
    val isError = remember { mutableStateOf(false) }
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
            isError.value = !field.validate(textFieldValue.text)

        },
        label = { Text(text = field.label) },
        isError = isError.value,
        supportingText = {
            if (isError.value) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = field.errorMessage,
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
    day: String,
    employeeDetails: EmployeeDetails,
    onSelectionChange: (EmployeeDetails) -> Unit = {},

    ) {
    var sliderPosition by remember { mutableStateOf(0f) }
    var text by remember { mutableStateOf("") }
    Column {
        Row(
            modifier = Modifier,
            //.width(50.dp)
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(text = day)
            Slider(
                value = sliderPosition,
                onValueChange = {},
                colors = SliderDefaults.colors(
                    thumbColor = colorScheme.secondary,
                ),
                steps = 1,
                valueRange = 0f..2f
            )
            text = when (sliderPosition) {
                0f -> ShiftType.DAY.toString()
                1f -> ShiftType.FULL.toString()
                else -> ShiftType.NIGHT.toString()
            }
        }
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
private fun EmployeeEntryScreenPreview() {
    //ScheduleSelector(day="monday")
}