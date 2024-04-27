package uk.ac.tees.mad.d3895467.screen

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import uk.ac.tees.mad.d3895467.R

@Composable
fun SplashScreen(
    onLogin: () -> Unit = {},
    onHome : () -> Unit = {},
    modifier: Modifier = Modifier.background(Color.White).fillMaxSize()
) {
    val mAuth = FirebaseAuth.getInstance()

    val scale = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        val currentUser = mAuth.currentUser

        scale.animateTo(
            targetValue = 0.7f,
            // tween Animation
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                }))
        // Customize the delay time
        delay(2000L)
        // Check if the user is already logged in
        if (currentUser != null) {
            // User is logged in, navigate to HomeScreen
            onHome()
        } else {
            // User is not logged in, navigate to LoginScreen
            onLogin.invoke()
        }
    }

    // Image
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()) {
        // Change the logo
        Image(painter = painterResource(id = R.drawable.logo_for_skillswap),
            contentDescription = "Logo",
            modifier = Modifier.scale(scale.value))
    }
}
