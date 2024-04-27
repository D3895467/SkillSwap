package uk.ac.tees.mad.d3895467.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.d3895467.SkillSwapAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatConversationScreen(navController: NavController, otherPersonEmail: String) {
    var messageText by remember { mutableStateOf(TextFieldValue()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(modifier = Modifier.padding(vertical = 10.dp)) {
                        Icon(
                            Icons.Rounded.KeyboardArrowLeft, contentDescription = "ChatConversation"
                        )
                        Text(text = "$otherPersonEmail")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                          Icon( Icons.Filled.KeyboardArrowLeft, contentDescription = "")
                },
                /*navigationIcon = {
                    Surface(
                        onClick = navController::popBackStack,
                        color = Color.Transparent,
                    ) {
                        Row(modifier = Modifier.padding(vertical = 10.dp)) {
                            Icon(
                                Icons.Rounded.KeyboardArrowLeft, contentDescription = "ChatConversation"
                            )
                            Text(text = "$otherPersonEmail")
                        }
                    }
                }*/
            )
        },
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Chat messages list
            ChatMessagesList()

            // Input field for sending new messages
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                label = { Text("Type a message...") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(Icons.Filled.Send, contentDescription = "send", Modifier.clickable {  })
                }
            )
        }
    }
}

@Composable
fun ChatMessagesList() {
    // Dummy data for chat messages (replace with actual data from Firestore)
    val messages = listOf(
        "Hello!",
        "Hi there!",
        "How are you?",
        "I'm good, thanks!"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        messages.forEach { message ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
