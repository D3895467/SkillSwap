package uk.ac.tees.mad.d3895467.screen.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uk.ac.tees.mad.d3895467.data.UserData

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Edit
import uk.ac.tees.mad.d3895467.SkillSwapAppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogin: () -> Unit = {},
    navController: NavController,

) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()


    val mAuth = FirebaseAuth.getInstance()

    // Firebase Firestore instance
    val db = FirebaseFirestore.getInstance()

    // State to hold user data
    var currentUserData by remember { mutableStateOf<UserData?>(null) }

    /*// Check if the user is logged in and retrieve the UID
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
                   // if (userData != null) {
                        // Update the state with user data
                        currentUserData = userData
                        Log.d("Profile", "User Data: ${userData!!.name}")
                   // }
                } else {
                    Log.d("Profile", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Profile", "Error getting documents: ", exception)
            }
    }*/


    mAuth.currentUser?.uid?.let { uid ->
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Convert Firestore document to UserData
                    val userData = document.toObject(UserData::class.java)
                    if (userData != null) {
                        // Update the state with user data
                        currentUserData = userData
                        // Populate UI fields with user data
                        //name = userData.name
                        //emailname = userData.email
                        // You can populate other fields similarly
                    }
                } else {
                    // Handle the case when the document doesn't exist
                    Toast.makeText(context, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors that may occur
                Toast.makeText(context, "Error fetching user data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    val currentUserName = currentUserData?.name
    val email = currentUserData?.email
    val image = currentUserData?.image



    Scaffold(
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {

                Spacer(modifier = Modifier.height(16.dp))
                if (currentUserName != null && image != null) {
                    ProfileSection(currentUserName = currentUserName,image,navController)
                }

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .fillMaxWidth()
                ) {
                    TableRow(
                        label = "Notifications",
                        modifier = Modifier.clickable {
                        })
                    TableRow(
                        label = "Change Password",
                        modifier = Modifier.clickable {
                            navController.navigate(SkillSwapAppScreen.ChangePassword.name)
                        })
                }
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .fillMaxWidth()
                ){
                    TableRow(
                        label = "Send us a message",
                        modifier = Modifier.clickable {
                            navController.navigate(SkillSwapAppScreen.Feedback.name)
                        })
                    TableRow(
                        label = "Share",
                        modifier = Modifier.clickable {
                            shareApp(context)
                        })

                    TableRow(
                        label = "Logout",
                        isDestructive = true,
                        modifier = Modifier.clickable {
                            logoutUser(onLogin = onLogin, context )
                        })
                }
            }
        }
    )
}



private fun logoutUser(onLogin: () -> Unit,context: Context) {
    val mAuth = FirebaseAuth.getInstance()

    // Sign out the current user
    mAuth.signOut()
    Toast.makeText(context,"Logout Successfully...",Toast.LENGTH_SHORT).show()
    onLogin()
}

@Composable
fun ProfileSection(
    currentUserName: String,
    imageuri :String,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image
        Image(
            painter = rememberImagePainter(data = imageuri,  builder = {
                transformations(CircleCropTransformation())
            }),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, color = Color.Gray, shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(10.dp))

        // User Info
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentUserName,
                style = MaterialTheme.typography.headlineLarge
            )
            Button(
                onClick = {
                    navController.navigate(SkillSwapAppScreen.ProfileEdit.name)
                },
            ) {
                Text (
                    text = "Edit Profit",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                )
            }
        }
    }
}


private fun shareApp(context: Context) {
    // Create an Intent to share text
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Check out this awesome app!")
        type = "text/plain"
    }

    // Start the activity to share the text
    context.startActivity(Intent.createChooser(sendIntent, "Share via"))
}



