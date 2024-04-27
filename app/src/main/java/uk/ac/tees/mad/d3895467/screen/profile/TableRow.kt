package uk.ac.tees.mad.d3895467.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TableRow(
  modifier: Modifier = Modifier,
  label: String? = null,
  hasArrow: Boolean = false,
  isDestructive: Boolean = false,
  detailContent: (@Composable RowScope.() -> Unit)? = null,
  content: (@Composable RowScope.() -> Unit)? = null,
  textColor: Color = Color.White // Default text color
) {
  Row(
    modifier = modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceTint),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    if (label != null) {
      Text(
        text = label,
        color = textColor,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
      )
    }
    if (content != null) {
      content()
    }

    if (hasArrow) {
      Icon(
        Icons.Filled.KeyboardArrowRight,
        contentDescription = "Right arrow",
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
      )
    }
    if (detailContent != null) {
      detailContent()
    }
  }
}

/*
@Composable
fun TableRow(
  modifier: Modifier = Modifier,
  label: String? = null,
  hasArrow: Boolean = false,
  isDestructive: Boolean = false,
  detailContent: (@Composable RowScope.() -> Unit)? = null,
  content: (@Composable RowScope.() -> Unit)? = null
) {
  //val textColor = if (isDestructive) Destructive else TextPrimary

  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    if (label != null) {
      Text(
        text = label,
//        style = Typography.,
//        color = textColor,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
      )
    }
    if (content != null) {
      content()
    }

    if (hasArrow) {
      Icon(
        Icons.Filled.KeyboardArrowRight,
        contentDescription = "Right arrow",
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
      )
    }
    if (detailContent != null) {
      detailContent()
    }
  }
}*/
