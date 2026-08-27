package com.example.ui.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ClipCategory

/**
 * Semantic Type Badges (Design System 5.2)
 * Pill / rounded-sm badge with uppercase label and pastel semantic color tones.
 */
@Composable
fun ClipBadge(
    category: ClipCategory,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.red < 0.2f

    val (bgColor, textColor) = when (category) {
        ClipCategory.LINK -> Color(0xFFC8DED5) to Color(0xFF163C3A)
        ClipCategory.CODE -> Color(0xFFD9D5EA) to Color(0xFF332857)
        ClipCategory.OTP -> Color(0xFFF5DF9B) to Color(0xFF5E4B10)
        ClipCategory.NOTE -> Color(0xFFF7B98D) to Color(0xFF6F3C28)
        ClipCategory.EMAIL -> Color(0xFFE2D6CB) to Color(0xFF5A3C2A)
        ClipCategory.TEXT -> if (isDark) Color(0xFF2F4658) to Color(0xFFF8F6F1) else Color(0xFFEEE9DF) to Color(0xFF42546A)
    }

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
