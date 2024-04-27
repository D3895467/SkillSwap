package uk.ac.tees.mad.d3895467.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.d3895467.Constants
import uk.ac.tees.mad.d3895467.SkillSwapAppScreen
import uk.ac.tees.mad.d3895467.data.SkillListing
import uk.ac.tees.mad.d3895467.ui.theme.*

@Composable
fun HomeScreen(userId: String, navController: NavController) {

    var skillListings by remember { mutableStateOf<List<SkillListing>>(emptyList()) }
    LaunchedEffect(Unit) {
        skillListings = fetchSkillListings()
        Log.d("home","$skillListings")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            //.fillMaxSize()
            // .verticalScroll(rememberScrollState())
            .clip(RoundedCornerShape(28.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge,
            text = "Swap, learn, grow",
            color = primaryLight // Set text color to primaryLight
        )
        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(18.dp)
                .background(surfaceLight), // Set card background color to surfaceLight
            //contentColor = onSurfaceLight // Set content color to onSurfaceLight
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.headlineMedium,
                    text = "Most Collaborated",
                    color = primaryLight // Set text color to primaryLight
                )

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Discover the most accomplished and influential professionals "
                )
                Row() {
                    var like by remember { mutableStateOf(false) }
                    var info by remember { mutableStateOf(false) }

                    IconText(
                        icon = if (like) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                        text = "2k",
                        onClick = { like = !like })
                    Spacer(modifier = Modifier.width(16.dp))
                    IconText(
                        icon = if (!info) Icons.Outlined.Info else Icons.Filled.Info,
                        text = "1",
                        onClick = { info = !info })
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = "NearBy",
                color = primaryLight // Set text color to primaryLight
            )
            TextButton(onClick = { navController.navigate(SkillSwapAppScreen.AllSkillScreen.name) }) {
                Text(
                    style = MaterialTheme.typography.labelSmall,
                    text = "See more"
                )
            }
        }

        SkillRow(skillData = skillListings,navController)


    }
}

@Composable
fun SkillRow(skillData: List<SkillListing>,navController: NavController) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    ) {
        items(skillData,  key = { it.skillName }) {  skill ->
            SkillCard(skill = skill, navController = navController,
                modifier = Modifier.clickable (
                    onClick = {
                        Constants.skillId = skill.skillId
                        Constants.skillUser = skill.userId
                        Constants.skillImage = skill.skillImage
                        Constants.skillName = skill.skillName
                        Constants.skillDescription = skill.description
                        // Navigate to SkillDetailScreen with parameters
                        val bundle = bundleOf("name" to "John Doe") // Example parameter
                        navController.navigate("${SkillSwapAppScreen.SkillDetailScreen.name}?skillName=${skill.skillName}")
                    }
                ))
        }
    }
}

@Composable
fun SkillCard(modifier: Modifier,skill: SkillListing, navController: NavController) {
    Card(
        modifier = modifier
        /*elevation = 4.dp*/
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = rememberImagePainter(skill.skillImage), // Replace with your actual image resource
                contentDescription = "Skill Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.FillBounds
            )
            Text(
                text = skill.skillName,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )
            Text(
                text = skill.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3
            )
        }
    }
}

@Composable
fun IconText(
    icon : ImageVector,
    text : String,
    onClick: () ->  Unit
) {
    Row {
        Icon(
            modifier = Modifier.clickable ( onClick = onClick::invoke ),
            imageVector =  icon,
            contentDescription = "like"
        )
        Text(
            text
        )
    }
}

suspend fun fetchSkillListings(): List<SkillListing> {
    val db = FirebaseFirestore.getInstance()
    val skillListingsCollection = db.collection("skillListings")

    return try {
        val querySnapshot = skillListingsCollection.get().await()
        val skillListings = mutableListOf<SkillListing>()
        for (document in querySnapshot.documents) {
            val userId = document.getString("userId") ?: ""
            val skillImage = document.getString("skillImage") ?: ""
            val skillId = document.getString("skillId") ?: ""
            val skillName = document.getString("skillName") ?: ""
            val description = document.getString("description") ?: ""
            skillListings.add(SkillListing(userId,skillImage, skillId, skillName, description))
        }
        skillListings
    } catch (e: Exception) {
        // Handle error appropriately, such as logging or showing a message
        emptyList()
    }
}