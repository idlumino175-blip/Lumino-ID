package com.example.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ClipCategory

/**
 * Semantic Type Badges (Design System 5.2)
 * Pill / rounded-sm badge with uppercase label and pastel semantic color tones.
 * Uses ClipCategory's built-in light/dark color pairs for proper theming.
 */
@Composable
fun ClipBadge(
    category: ClipCategory,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()
    val bgColor = if (isDarkTheme) category.bgDark else category.bgLight
    val textColor = if (isDarkTheme) category.textDark else category.textLight

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        modifier = modifier.height(22.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(11.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = category.badgeLabel,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                letterSpacing = 0.6.sp
            )
        }
    }
}
