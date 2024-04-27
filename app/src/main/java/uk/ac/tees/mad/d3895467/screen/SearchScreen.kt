package uk.ac.tees.mad.d3895467.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.d3895467.data.SkillListing
import uk.ac.tees.mad.d3895467.ui.theme.primaryLight

@Composable
fun SearchScreen(userId: String, navController: NavController) {
    var text by rememberSaveable { mutableStateOf("") }

    var skillListings by remember { mutableStateOf<List<SkillListing>>(emptyList()) }

    // State to hold the filtered skill listings
    var filteredSkillListings by remember { mutableStateOf<List<SkillListing>>(emptyList()) }

    // Function to update filtered skill listings based on search query
    fun updateFilteredSkillListings(query: String) {
        filteredSkillListings = searchSkillListings(skillListings, query)
    }

    LaunchedEffect(Unit) {
        skillListings = fetchSkillListings()
        Log.d("search","$skillListings")
        if (text.isNotBlank()) {
            updateFilteredSkillListings(text)
        }
    }

    Column (modifier = Modifier
        .padding(18.dp)
        .fillMaxSize()
    ){

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = {
                Text("Search")
            },
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Localized description",
                    Modifier.clickable { updateFilteredSkillListings(text) },
                    tint = primaryLight // Set tint color to primaryLight
                )
            },
            modifier = Modifier
                .padding(vertical = 1.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                disabledBorderColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.onPrimary,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
            ),
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = {
                        text = ""
                        updateFilteredSkillListings(text)
                    }
                ) {
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "Localized description",
                        tint = primaryLight // Set tint color to primaryLight
                    )
                }
            }
        )

        // Display filtered skill listings
        SkillRow(skillData = filteredSkillListings, navController  )
    }
}

/*
@Composable
fun SearchScreen(userId: String, navController: NavController) {
    var text by rememberSaveable { mutableStateOf("") }


    var skillListings by remember { mutableStateOf<List<SkillListing>>(emptyList()) }

    // Filter the skillListings based on searchQuery
    */
/*val filteredSkillListings = remember {
        derivedStateOf {
            searchSkillListings(skillListings, text)
        }
    }*//*


    // State to hold the filtered skill listings
    var filteredSkillListings by remember { mutableStateOf<List<SkillListing>>(emptyList()) }

    // Function to update filtered skill listings based on search query
    fun updateFilteredSkillListings(query: String) {
        filteredSkillListings = searchSkillListings(skillListings, query)
    }

    LaunchedEffect(Unit) {
        skillListings = fetchSkillListings()
        Log.d("search","$skillListings")
        if (text.isNotBlank()) {
            updateFilteredSkillListings(text)
        }
    }

    Column (modifier = Modifier
        .padding(18.dp)
        .fillMaxSize()
        ){


        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = {
                Text(
                    "Search"
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Localized description",
                    Modifier.clickable { updateFilteredSkillListings(text) }
                )
            },
            modifier = Modifier
                .clip(RoundedCornerShape(28.dp))
                .padding( vertical = 1.dp)
                .fillMaxWidth()
            */
/*.weight(weight = 0.09f, fill = true)*//*
,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                disabledBorderColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.onPrimary,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
            ),
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = {
                        text = ""
                        updateFilteredSkillListings(text)
                    }
                ) {
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "Localized description",
                    )
                }
            }
        )

        // Display filtered skill listings
        SkillRow(skillData = filteredSkillListings, navController  )
    }
}
*/

// Function to filter skill listings based on search query
fun searchSkillListings(skillListings: List<SkillListing>, query: String): List<SkillListing> {
    if (query.isBlank()) {
        return skillListings // Return original list if query is blank
    } else {
        // Filter the skillListings based on the search query
        return skillListings.filter { skill ->
            skill.skillName.contains(query, ignoreCase = true) || skill.description.contains(
                query,
                ignoreCase = true
            )
        }
    }
}
