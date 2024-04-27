package uk.ac.tees.mad.d3895467.screen.profile

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import uk.ac.tees.mad.d3895467.Constants
import uk.ac.tees.mad.d3895467.R
import uk.ac.tees.mad.d3895467.data.UserData


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    navController: NavController,
    //userId: String
) {

    val mAuth = FirebaseAuth.getInstance()

    // Firebase Firestore instance
    val db = FirebaseFirestore.getInstance()

    // State to hold user data
    var currentUserData by remember { mutableStateOf<UserData?>(null) }

    // Check if the user is logged in and retrieve the UID
    mAuth.currentUser?.let { user ->
        val currentUserUid = user.uid
        // Fetch user data from Firestore
        db.collection("users")
            .document(currentUserUid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Convert Firestore document to UserData
                    val userData = document.toObject(UserData::class.java)
                    if (userData != null) {
                        // Update the state with user data
                        currentUserData = userData
                        Log.d("Profile", "User Data: ${userData.name}")
                    }
                } else {
                    Log.d("Profile", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Profile", "Error getting documents: ", exception)
            }
    }

    val currentUserName = currentUserData?.name
    val email = currentUserData?.email
    val image = currentUserData?.image


    Scaffold(topBar = {
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            if (image != null) {
                if (currentUserName != null) {
                    if (email != null) {
                        EditProfile(
                            imageUri = image,
                            currentUserName = currentUserName,
                            email = email,
                            phoneNumber = currentUserData?.phoneNumber.toString(),
                            address = currentUserData?.address.toString(),
                            zip = currentUserData?.zip.toString(),
                            city = currentUserData?.city.toString(),
                            country = currentUserData?.country.toString(),
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(
    imageUri: String,
    currentUserName: String,
    email: String,
    navController: NavController,
    phoneNumber: String,
    address: String,
    zip: String,
    city: String,
    country: String
) {
    val context = LocalContext.current

    var editedName by remember { mutableStateOf(currentUserName) }
    var editedemail by remember { mutableStateOf(email) }
    var phoneNumber by remember { mutableStateOf(phoneNumber) }
    var address by remember { mutableStateOf(address) }
    var zip by remember { mutableStateOf(zip) }
    var city by remember { mutableStateOf(city) }
    var country by remember { mutableStateOf(country) }


    val isValidate by derivedStateOf { currentUserName.isNotBlank() && email.isNotBlank() && imageUri.isNotBlank() }

    // State to hold the selected image URI
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Create a launcher for the image picker
    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = uri
        }
    }

    // Coroutine scope for launching image picker and other async tasks
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // Profile Image
        Image(
            painter = rememberImagePainter(data = selectedImageUri ?: imageUri, builder = {
                transformations(CircleCropTransformation())
            }),
            contentDescription = null,
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .border(2.dp, color = Color.Gray, shape = CircleShape)
                .clickable {
                    // Launch the image picker
                    pickImage.launch("image/*")
                }
        )

        Spacer(modifier = Modifier.width(10.dp))

        // User Info
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp, end = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = editedName,
                singleLine = true,
                onValueChange = { editedName = it
                    Constants.mAuthName = editedName },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                label = { Text(text = "Name") },
                isError = false,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {}
                )
            )
            OutlinedTextField(
                value = editedemail,
                singleLine = true,
                onValueChange = {editedemail = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                label = { Text(text ="Email") },
                isError = false,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {}
                )
            )

            OutlinedTextField(
                value = phoneNumber.toString(),
                singleLine = true,
                onValueChange = {phoneNumber = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                label = { Text(text ="Phone Number") },
                isError = false,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {}
                )
            )

            OutlinedTextField(
                value = address.toString(),
                singleLine = true,
                onValueChange = {address = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                label = { Text(text ="Address") },
                isError = false,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {}
                )
            )
            OutlinedTextField(
                value = city.toString(),
                singleLine = true,
                onValueChange = {city = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                label = { Text(text ="City") },
                isError = false,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {}
                )
            )
            OutlinedTextField(
                value = zip.toString(),
                singleLine = true,
                onValueChange = {zip = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                label = { Text(text ="Zip") },
                isError = false,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {}
                )
            )

            OutlinedTextField(
                value = country.toString(),
                singleLine = true,
                onValueChange = {country = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                label = { Text(text ="Country") },
                isError = false,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {}
                )
            )


            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                        updateUserData(
                            selectedImageUri,
                            editedName,
                            editedemail,
                            phoneNumber.toString(),
                            address.toString(),
                            zip.toString(),
                            city.toString(),
                            country.toString(),
                            context,
                            navController
                        )

                    Log.d("Save","Save")
                    //navController.navigate("Settings")
                },
                //enabled = isValidate
            ) {
                Text(stringResource(R.string.update))
            }

        }
    }
}

/*private fun updateUserData(
    imageUri: Uri?,
    name: String,
    email: String,
    context: Context,
    navController: NavController
) {

    val mAuth = FirebaseAuth.getInstance()
    val currentUserUid = mAuth.currentUser?.uid
    val db = FirebaseFirestore.getInstance()
    currentUserUid?.let { uid ->
        db.collection("users").document(uid).update(
            mapOf(
                "name" to name,
                "email" to email,
                "image" to imageUri?.toString()
            )
        ).addOnSuccessListener {
            // Update successful
            Log.d("EditProfile", "User data updated successfully.")
            navController.navigateUp()
            Toast.makeText(context, "User data updated successfully.", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            // Update failed
            Log.e("EditProfile", "Error updating user data.", it)
            Toast.makeText(context, "Error updating user data.", Toast.LENGTH_SHORT).show()
        }
    }
}*/


private fun updateUserData(
    imageUri: Uri?,
    name: String,
    email: String,
    phoneNumber: String,
    address:String,
    zip:String,
    city:String,
    country:String,
    context: Context,
    navController: NavController
) {
    val mAuth = FirebaseAuth.getInstance()
    val currentUserUid = mAuth.currentUser?.uid
    val db = FirebaseFirestore.getInstance()

    currentUserUid?.let { uid ->
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_images").child("$uid.jpg")

        // Check if an image is selected
        imageUri?.let { uri ->
            // Upload image to Firebase Storage
            storageRef.putFile(uri)
                .addOnSuccessListener { uploadTask ->
                    // Image upload successful, get the download URL
                    storageRef.downloadUrl
                        .addOnSuccessListener { downloadUri ->
                            // Update user data with image URL
                            updateUserWithImage(db, uid, name, email, downloadUri.toString(),phoneNumber,
                                address,
                                zip,
                                city,
                                country, context, navController)
                        }
                        .addOnFailureListener { e ->
                            Log.e("EditProfile", "Error getting download URL", e)
                            Toast.makeText(context, "Error updating user data.", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("EditProfile", "Error uploading image", e)
                    Toast.makeText(context, "Error updating user data.", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            // No image selected, update user data without image URL
            updateUserWithImage(db, uid, name, email, null, phoneNumber,
                address,
                zip,
                city,
                country, context, navController)
        }
    }
}

private fun updateUserWithImage(
    db: FirebaseFirestore,
    uid: String,
    name: String,
    email: String,
    imageUrl: String?,
    context: Context,
    navController: NavController
) {
    // Prepare data to update
    val userData = mutableMapOf(
        "name" to name,
        "email" to email
    )

    // Add image URL if available
    imageUrl?.let {
        userData["image"] = it
    }

    // Update user data in Firestore
    db.collection("users").document(uid)
        .update(userData as Map<String, Any>)
        .addOnSuccessListener {
            // Update successful
            Log.d("EditProfile", "User data updated successfully.")
            navController.navigateUp()
            Toast.makeText(context, "User data updated successfully.", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            // Update failed
            Log.e("EditProfile", "Error updating user data.", e)
            Toast.makeText(context, "Error updating user data.", Toast.LENGTH_SHORT).show()
        }
}


private fun updateUserWithImage(
    db: FirebaseFirestore,
    uid: String,
    name: String,
    email: String,
    imageUrl: String?,
    phoneNumber: String,
    address: String,
    zip: String,
    city: String,
    country: String,
    context: Context,
    navController: NavController
) {
    // Prepare data to update
    val userData = mutableMapOf(
        "name" to name,
        "email" to email,
        "phoneNumber" to phoneNumber,
        "address" to address,
        "zip" to zip,
        "city" to city,
        "country" to country
    )

    // Add image URL if available
    imageUrl?.let {
        userData["image"] = it
    }
//
//    // Add phone number if available
//    phoneNumber?.let {
//        userData["phoneNumber"] = it
//    }
//
//    // Add address if available
//    address?.let {
//        userData["address"] = it
//    }
//
//    // Add zip code if available
//    zip?.let {
//        userData["zip"] = it
//    }
//
//    // Add city if available
//    city?.let {
//        userData["city"] = it
//    }
//
//    // Add country if available
//    country?.let {
//        userData["country"] = it
//    }

    // Update user data in Firestore
    db.collection("users").document(uid)
        .update(userData as Map<String, Any>)
        .addOnSuccessListener {
            // Update successful
            Log.d("EditProfile", "User data updated successfully.")
            navController.navigateUp()
            Toast.makeText(context, "User data updated successfully.", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            // Update failed
            Log.e("EditProfile", "Error updating user data.", e)
            Toast.makeText(context, "Error updating user data.", Toast.LENGTH_SHORT).show()
        }
}

