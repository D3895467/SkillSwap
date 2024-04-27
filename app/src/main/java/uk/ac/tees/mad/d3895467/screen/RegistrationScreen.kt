package uk.ac.tees.mad.d3895467.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uk.ac.tees.mad.d3895467.Constants.mAuthName
import uk.ac.tees.mad.d3895467.R
import uk.ac.tees.mad.d3895467.data.UserData

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun RegistrationScreen(
    onLoginButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier.background(color = Color.White)
){
    var name by rememberSaveable { mutableStateOf("") }
    var emailname by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmpassword by rememberSaveable { mutableStateOf("") }

    val isValidate by derivedStateOf { name.isNotBlank() &&emailname.isNotBlank() && password.isNotBlank() }
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
            .fillMaxSize(),) {
            Column (
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ){

                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = "Welcome"
                )
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    EditNumberField(
                        label = R.string.name,
                        leadingIcon = Icons.Filled.AccountCircle,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        value = name,
                        onValueChanged = { name = it },
                        visualTransformation = VisualTransformation.None,
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                    EditNumberField(
                        label = R.string.email,
                        leadingIcon = Icons.Filled.Email,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        value = emailname,
                        onValueChanged = { emailname = it
                            mAuthName = emailname
                                         },
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
                    EditNumberField(
                        label = R.string.confirm_password,
                        leadingIcon = Icons.Filled.Lock,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        value = confirmpassword,
                        onValueChanged = { confirmpassword = it },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth(),
                    )


                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Button(
                            modifier = Modifier
                                /*.fillMaxWidth()*/,
                            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 16.dp),
                            onClick = {
                                performFirebaseRegistration(auth, email= emailname, password = password,confirmpassword,name, context , onRegistered = onLoginButtonClicked)
                            },
                            enabled = isValidate
                        ) {
                            Text("Sign In")
                        }
                    }
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = {
                        onLoginButtonClicked()
                    }
                    ) {
                        Text(text = "Already have an account? Sign In?")
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


private fun performFirebaseRegistration(
    auth: FirebaseAuth,
    email: String,
    password: String,
    confirmpassword:String,
    name: String,
    context: Context,
    onRegistered: () -> Unit
) {

    if (!isEmailValid(email)) {
        Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
        return
    }
    if (password.length < 6) {
        Toast.makeText(context, "Password should be at least 6 characters long", Toast.LENGTH_SHORT)
            .show()
        return
    }
    if (password != confirmpassword) {
        Toast.makeText(context, "Password does not match", Toast.LENGTH_SHORT).show()
        return
    }

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid
                // Create a Firestore document for the user
                uid?.let {
                    val userData = UserData(
                        name = name,
                        email = email,
                        password = password,
                        image = R.drawable.ic_user.toString(),
                        phoneNumber = "",
                        address ="",
                        zip ="",
                        city ="",
                        country ="",
                    )
                    FirebaseFirestore.getInstance().collection("users")
                        .document(uid)
                        .set(userData)
                        .addOnSuccessListener {
                            // Registration and data storage successful
                            Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT)
                                .show()
                            onRegistered()
                        }
                        .addOnFailureListener {
                            // Registration successful, but data storage failed
                            Toast.makeText(context, "Data storage failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            } else {
                val exception = task.exception
                // If registration fails, display a message to the user
                Toast.makeText(
                    context,
                    "Registration failed ${exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}







@Preview
@Composable
fun RegistrationScreenPreview(){
    RegistrationScreen (modifier = Modifier.fillMaxHeight() )
}

