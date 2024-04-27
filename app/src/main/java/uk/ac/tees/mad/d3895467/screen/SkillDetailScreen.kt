package uk.ac.tees.mad.d3895467.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import uk.ac.tees.mad.d3895467.Constants
import uk.ac.tees.mad.d3895467.data.UserData

@Composable
fun SkillDetailScreen(
    userId: String,
    navController: NavHostController,
    skillName: String?,
    /*skillImage: String,
    skillName: String,
    description: String*/
) {

    val skillId = Constants.skillId
    val skillImage = Constants.skillImage
    val skillName = Constants.skillName
    val skillDescription = Constants.skillDescription
    val skillUser = Constants.skillUser

    var userName = ""
    var userImage = ""

   // var skillUserId by remember { mutableStateOf<List<UserData>>(emptyList()) }
    LaunchedEffect(Unit) {
         val userData = fetchUserData(skillUser)
        if (userData != null) {
            userName = userData.email
            userImage = userData.image
            // User data is fetched successfully, you can use it here
            Log.d("UserData", "Fetched user: $userData")
        } else {
            // User data could not be fetched
            Log.d("UserData", "User data not found for ID: $userId")
        }
    }

    val skillNameFromArgs = remember {
        navController.currentBackStackEntry?.arguments?.getString("skillName")
    }

    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val skillName1 = navBackStackEntry?.arguments?.getString("skillName")


    Column (modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp)){
        Image(
            painter = rememberImagePainter(skillImage), // Replace with your actual image resource
            contentDescription = "Skill Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = androidx.compose.ui.layout.ContentScale.FillBounds
        )
        Column (
            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        ){

            /*Row (verticalAlignment = Alignment.CenterVertically){
                Image(
                    painter = rememberImagePainter(userImage), // Replace with your actual image resource
                    contentDescription = "Skill Image",
                    modifier = Modifier
                        .size(80.dp)//.background(shape =CircleShape, color = Color.Transparent)
                        .border(1.5.dp, color = Color.Blue, shape = CircleShape, ),
                    contentScale = androidx.compose.ui.layout.ContentScale.FillBounds
                )
                Text(
                    text = "$userName",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }*/

            Text(
                text = "$skillName",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )

                Text(
                    text = "$skillDescription",
                    style = MaterialTheme.typography.bodyMedium,
                )

        }
    }
}

class UserViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _userState: MutableStateFlow<Result<UserData>> = MutableStateFlow(Result.failure(Exception("User not loaded")))
    val userState: StateFlow<Result<UserData>> = _userState

    fun getUserDataByUserId(userId: String) {
        viewModelScope.launch {
            try {
                val userDataSnapshot = withContext(Dispatchers.IO) {
                    // Fetch user data from Firestore using the userId
                    db.collection("users").document(userId).get().await()
                }
                val userData = userDataSnapshot.toObject(UserData::class.java)
                if (userData != null) {
                    _userState.value = Result.success(userData)
                } else {
                    throw Exception("User data is null")
                }
            } catch (e: Exception) {
                _userState.value = Result.failure(e)
            }
        }
    }
}

//suspend fun fetchUserData(): List<UserData> {
//    val db = FirebaseFirestore.getInstance()
//    val skillUserCollection = db.collection("users")
//
//    return try {
//        val querySnapshot = skillUserCollection.get().await()
//        val skillUser = mutableListOf<UserData>()
//        for (document in querySnapshot.documents) {
//            val userId = document.getString("userId") ?: ""
//            val email = document.getString("skillImage") ?: ""
//            val image = document.getString("skillName") ?: ""
//            val name = document.getString("description") ?: ""
//            skillUser.add(UserData(userId,email, image, name,""))
//        }
//        skillUser
//    } catch (e: Exception) {
//        // Handle error appropriately, such as logging or showing a message
//        emptyList()
//    }
//}

suspend fun fetchUserData(userId: String): UserData? {
    val db = FirebaseFirestore.getInstance()
    val userDocument = db.collection("users").document(userId)

    return try {
        val documentSnapshot = userDocument.get().await()
        if (documentSnapshot.exists()) {
            val userId = documentSnapshot.getString("userId") ?: ""
            val email = documentSnapshot.getString("email") ?: ""
            val password = documentSnapshot.getString("password") ?: ""
            val image = documentSnapshot.getString("image") ?: ""
            UserData(userId, email, password, image, "", "", "", "", "")
        } else {
            null
        }
    } catch (e: Exception) {
        // Handle error appropriately, such as logging or showing a message
        null
    }
}
