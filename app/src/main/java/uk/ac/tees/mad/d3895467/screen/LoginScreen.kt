package uk.ac.tees.mad.d3895467.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.d3895467.R

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreen(
    onLoginButtonClicked: () -> Unit = {},
    onRegistered: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    modifier: Modifier = Modifier
){
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val isValidate by derivedStateOf { username.isNotBlank() && password.isNotBlank() }
    val focusManager = LocalFocusManager.current

    val context = LocalContext.current
    // Firebase Authentication instance
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = modifier,
    ) {
        val imagePainter: Painter = painterResource(id = R.drawable.background_img)

        Image(
            painter = imagePainter,
            contentDescription = "Image Content Description",
            modifier = Modifier
                .fillMaxWidth()
                /*.padding(16.dp)
                .padding(*//*top = 40.dp, bottom = 40.dp*//*)*/
        )

        Card(modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .fillMaxSize(), ) {
            Column (
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
            ){
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = "Welcome Back"
                )
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    EditNumberField(
                        label = R.string.email,
                        leadingIcon = Icons.Filled.Email,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        value = username,
                        onValueChanged = { username = it },
                        visualTransformation = VisualTransformation.None,
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                    EditNumberField(
                        label = R.string.password,
                        leadingIcon = Icons.Filled.Lock,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        value = password,
                        onValueChanged = { password = it },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth(),
                    )

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = {
                            onForgotPassword()
                        }
                        ) {
                            Text(text = "Forgot password?")
                        }

                        Button(
                            modifier = Modifier,
                            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 16.dp),
                            onClick = {
                                performFirebaseAuthentication(
                                    auth = auth,
                                    email = username,
                                    password = password,
                                    context = context,
                                    onLoginButtonClicked = onLoginButtonClicked
                                )
                            },
                            enabled = isValidate
                        ) {
                            Text(stringResource(R.string.login))
                        }
                    }
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = {
                        onRegistered()
                    }
                    ) {
                        Text(text = "Don't have account? Sign Up")
                    }
                }
            }
        }
    }
}

private fun isEmailValid(email: String): Boolean {
    val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    return emailRegex.matches(email)
}


private fun performFirebaseAuthentication(
    auth: FirebaseAuth,
    email: String,
    password: String,
    context: Context,  // Pass the context as a parameter
    onLoginButtonClicked: () -> Unit
) {

    if (!isEmailValid(email)) {
        Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
        return
    }
    if (password.length < 6) {
        Toast.makeText(context, "Password should be at least 6 characters long", Toast.LENGTH_SHORT).show()
        return
    }
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Authentication successful, you can navigate to the next screen or perform other actions
                // For example: onLoginButtonClicked()
                Toast.makeText(context, "Authentication successful", Toast.LENGTH_SHORT).show()
                onLoginButtonClicked()
            } else {
                // If authentication fails, display a message to the user
                Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    visualTransformation: VisualTransformation,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
) {
    OutlinedTextField(
        value = value,
        singleLine = true,
        leadingIcon = { Icon(leadingIcon, null) },
        modifier = modifier,
        onValueChange = onValueChanged,
        visualTransformation = visualTransformation,
        label = { Text(stringResource(label)) },
        keyboardOptions = keyboardOptions
    )
}





@Preview
@Composable
fun LoginScreenPreview(){
    LoginScreen (modifier = Modifier.fillMaxHeight() )
}

