package uk.ac.tees.mad.d3895467.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddSkillScreen(
    userId: String,
    navController: NavController,
) {
    var skillName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // State to hold the selected image URI
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Create a launcher for the image picker
    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = uri
        }
    }

    val scope  = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        TextField(
            value = skillName,
            onValueChange = { skillName = it },
            label = { Text("Skill Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            })
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            singleLine = false,
            maxLines = 30,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                // You can handle submission here
                /*scope.launch {
                    addSkillToFirestore(userId ,skillName, description,navController,  context)
                }*/
            })
        )

        // Image Upload Button
        Button(
            onClick = {
                // Launch the image picker
                pickImage.launch("image/*")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(text = "Upload Image")
        }
        // Add Image Preview if selected
        selectedImageUri?.let { uri ->
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }


        Button(
            onClick = {
                if (skillName.isNotEmpty() && description.isNotEmpty() && (selectedImageUri != null)) {// Handle button click
                    scope.launch {
                        addSkillToFirestore(
                            userId = userId,
                            skillImageUri = selectedImageUri!!,
                            skillName = skillName,
                            description = description,
                            navController,
                            context
                        )
                    }
                } else {
                    val errorMessage = when {
                        skillName.isEmpty() -> "Please enter skill name"
                        description.isEmpty() -> "Please enter description"
                        selectedImageUri == null -> "Please select an image"
                        else -> "Please fill all required fields"
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.align(Alignment.End),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(text = "Add Skill")
        }



    }
}

//private suspend fun addSkillToFirestore(userId: String,skillImage: Uri, skillName: String, description: String, navController: NavController, context: Context) {
//    val db = FirebaseFirestore.getInstance()
//    val skillListingsCollection = db.collection("skillListings")
//
//
//    try {
//        val newSkill = hashMapOf(
//            "userId" to userId,
//            "skillImage" to skillImage,
//            "skillName" to skillName,
//            "description" to description
//        )
//        skillListingsCollection.add(newSkill).await()
//        navController.navigateUp()
//        Toast.makeText(context, "Skill added successfully", Toast.LENGTH_SHORT).show()
//    } catch (e: Exception) {
//        Log.e("AddSkillScreen", "Error adding skill", e)
//        Toast.makeText(context, "Failed to add skill", Toast.LENGTH_SHORT).show()
//    }
//}


private suspend fun addSkillToFirestore(userId: String, skillImageUri: Uri, skillName: String, description: String, navController: NavController, context: Context) {
    val db = FirebaseFirestore.getInstance()
    val storageRef = FirebaseStorage.getInstance().reference.child("skill_images")

    try {
        // Generate a unique file name for the image
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_$timeStamp.jpg"

        // Get reference to the image file in Firebase Storage
        val imageRef = storageRef.child(fileName)

        // Upload the image to Firebase Storage
        val uploadTask = imageRef.putFile(skillImageUri)
        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Image uploaded successfully, now get the download URL
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Save skill details along with image URL to Firestore
                    val newSkill = hashMapOf(
                        "userId" to userId,
                        "skillId" to timeStamp,
                        "skillImage" to uri.toString(), // Store image URL instead of Uri
                        "skillName" to skillName,
                        "description" to description
                    )

                    val skillListingsCollection = db.collection("skillListings")
                    skillListingsCollection.add(newSkill).addOnSuccessListener {
                        // Skill added successfully
                        navController.navigateUp()
                        Toast.makeText(context, "Skill added successfully", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { e ->
                        // Failed to add skill to Firestore
                        Log.e("AddSkillScreen", "Error adding skill", e)
                        Toast.makeText(context, "Failed to add skill", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { e ->
                    // Failed to get download URL
                    Log.e("AddSkillScreen", "Error getting download URL", e)
                    Toast.makeText(context, "Failed to add skill", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Image upload failed
                Log.e("AddSkillScreen", "Error uploading image")
                Toast.makeText(context, "Failed to add skill", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: Exception) {
        // Exception occurred
        Log.e("AddSkillScreen", "Error adding skill", e)
        Toast.makeText(context, "Failed to add skill", Toast.LENGTH_SHORT).show()
    }
}

