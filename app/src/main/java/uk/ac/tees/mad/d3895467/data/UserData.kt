package uk.ac.tees.mad.d3895467.data

import java.util.Date

data class UserData(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val image: String = "",
    val phoneNumber: String = "",
    val address: String ="",
    val zip: String ="",
    val city: String ="",
    val country: String ="",
)

data class SkillListing(
    val userId: String,
    val skillImage: String,
    val skillId: String,
    val skillName: String,
    val description: String
)

data class Message(
    val messageId: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Date
)

data class UserProfile(
    val userId: String,
    val username: String,
    val email: String,
    val skillsOffered: List<SkillListing>,
    val skillsInterested: List<SkillListing>,
    val location: Location
)

data class Location(
    val latitude: Double,
    val longitude: Double
)
