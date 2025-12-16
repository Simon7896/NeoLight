package com.example.neolight.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.example.neolight.data.local.database.FlashOption
import com.example.neolight.ui.flashoption.FlashOptionViewModel
import com.example.neolight.ui.theme.NeoLightTheme

@Composable
fun CustomMenuScreen(
    viewModel: FlashOptionViewModel
) {

    val options = viewModel.allFlashOptions.observeAsState()
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(viewModel.selected)
    }

    NeoLightTheme {
        Scaffold {
            paddingValue -> Column(
            Modifier
                .padding(paddingValue)
                .selectableGroup(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                Divider()
                options.let { list ->
                    list.value?.forEach { option ->
                        ListOption(
                            option = option,
                            selected = (option == selectedOption),
                            modifier = Modifier.selectable(
                                selected = (option == selectedOption),
                                onClick = { onOptionSelected(option); viewModel.selected = option},
                                role = Role.RadioButton
                            )
                        )
                        Divider()
                    }
                }
            }
            AddCustomButton(viewModel)
        }
    }
}

@Composable
fun ListOption(
    option: FlashOption,
    selected: Boolean,
    modifier: Modifier,
) {
    ListItem(
        headlineContent = { Text(text = option.name) },
        supportingContent = { Text(text = "Delay: " + option.delay.toString())},
        trailingContent = {
            RadioButton(selected = selected, onClick = null)
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomButton(viewModel: FlashOptionViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                securePolicy = SecureFlagPolicy.Inherit,
            )
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                tonalElevation = AlertDialogDefaults.TonalElevation
            ){
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                ) {
                    var name by remember { mutableStateOf("") }
                    var delay by remember { mutableStateOf(0f) }
                    TextField(
                        value = name,
                        onValueChange = {name = it},
                        label = { Text(text="Name")},
                        singleLine = true
                    )
                    Text(text = "Delay: $delay ms")
                    Slider(
                        value = delay,
                        onValueChange = {delay = it},
                        valueRange = 1f..10000f
                    )
                    Row {
                        TextButton(onClick = {
                                viewModel.insert(FlashOption(name=name, delay = delay.toLong()))
                                showDialog = false
                        }
                        ) {
                            Text(text = "Confirm")
                        }
                        TextButton(onClick = { showDialog = false }) {
                            Text(text = "Cancel")
                        }
                    }
                }
            }
        }

    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = true },
            contentPadding = ButtonDefaults.TextButtonContentPadding,
        ) {
            Text(text = "Add New")
        }
    }
}
