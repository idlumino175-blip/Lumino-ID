package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class VaultNavTab {
    HOME,
    FAVORITES
}

/**
 * Floating Rounded Bottom Dock matching screenshot:
 * White capsule surface with Home (Navy filled active pill) and Favorites (outline)
 */
@Composable
fun BottomNavDock(
    currentTab: VaultNavTab,
    onSelectTab: (VaultNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.red < 0.2f
    val dockBg = if (isDark) Color(0xFF213B50) else Color.White
    val dockBorder = if (isDark) Color(0xFF385269) else Color(0xFFE5DFD4)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .testTag("bottom_nav_dock"),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = dockBg),
        border = BorderStroke(1.dp, dockBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home Tab
            val isHome = currentTab == VaultNavTab.HOME
            Surface(
                onClick = { onSelectTab(VaultNavTab.HOME) },
                shape = RoundedCornerShape(20.dp),
                color = if (isHome) {
                    if (isDark) Color(0xFFF7B98D) else Color(0xFF1D4C6B)
                } else {
                    Color.Transparent
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("nav_tab_home")
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = if (isHome) {
                            if (isDark) Color(0xFF172233) else Color.White
                        } else {
                            if (isDark) Color(0xFFC5CBD0) else Color(0xFF7A8B99)
                        },
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Home",
                        fontSize = 11.sp,
                        fontWeight = if (isHome) FontWeight.Bold else FontWeight.Medium,
                        color = if (isHome) {
                            if (isDark) Color(0xFF172233) else Color.White
                        } else {
                            if (isDark) Color(0xFFC5CBD0) else Color(0xFF7A8B99)
                        }
                    )
                }
            }

            // Favorites Tab
            val isFavorites = currentTab == VaultNavTab.FAVORITES
            Surface(
                onClick = { onSelectTab(VaultNavTab.FAVORITES) },
                shape = RoundedCornerShape(20.dp),
                color = if (isFavorites) {
                    if (isDark) Color(0xFFF7B98D) else Color(0xFF1D4C6B)
                } else {
                    Color.Transparent
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("nav_tab_favorites")
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder,
                        contentDescription = "Favorites",
                        tint = if (isFavorites) {
                            if (isDark) Color(0xFF172233) else Color.White
                        } else {
                            if (isDark) Color(0xFFC5CBD0) else Color(0xFF7A8B99)
                        },
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Favorites",
                        fontSize = 11.sp,
                        fontWeight = if (isFavorites) FontWeight.Bold else FontWeight.Medium,
                        color = if (isFavorites) {
                            if (isDark) Color(0xFF172233) else Color.White
                        } else {
                            if (isDark) Color(0xFFC5CBD0) else Color(0xFF7A8B99)
                        }
                    )
                }
            }
        }
    }
}
