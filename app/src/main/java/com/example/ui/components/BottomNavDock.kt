package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.VaultTheme

enum class VaultNavTab {
    HOME,
    FAVORITES
}

/**
 * Floating Rounded Bottom Dock matching screenshot:
 * White capsule surface with Home (Navy filled active pill) and Favorites (outline)
 * Now with smooth animated color transitions between tab states.
 */
@Composable
fun BottomNavDock(
    currentTab: VaultNavTab,
    onSelectTab: (VaultNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val vc = VaultTheme.colors

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .testTag("bottom_nav_dock"),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = vc.dockBackground),
        border = BorderStroke(1.dp, vc.dockBorder),
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
            val homeTabBg by animateColorAsState(
                targetValue = if (isHome) vc.accentPrimary else Color.Transparent,
                animationSpec = tween(300),
                label = "home_tab_bg"
            )
            val homeTabContent by animateColorAsState(
                targetValue = if (isHome) vc.accentOnPrimary else vc.mutedText,
                animationSpec = tween(300),
                label = "home_tab_content"
            )

            Surface(
                onClick = { onSelectTab(VaultNavTab.HOME) },
                shape = RoundedCornerShape(20.dp),
                color = homeTabBg,
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
                        tint = homeTabContent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Home",
                        fontSize = 11.sp,
                        fontWeight = if (isHome) FontWeight.Bold else FontWeight.Medium,
                        color = homeTabContent
                    )
                }
            }

            // Favorites Tab
            val isFavorites = currentTab == VaultNavTab.FAVORITES
            val favTabBg by animateColorAsState(
                targetValue = if (isFavorites) vc.accentPrimary else Color.Transparent,
                animationSpec = tween(300),
                label = "fav_tab_bg"
            )
            val favTabContent by animateColorAsState(
                targetValue = if (isFavorites) vc.accentOnPrimary else vc.mutedText,
                animationSpec = tween(300),
                label = "fav_tab_content"
            )

            Surface(
                onClick = { onSelectTab(VaultNavTab.FAVORITES) },
                shape = RoundedCornerShape(20.dp),
                color = favTabBg,
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
                        tint = favTabContent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Favorites",
                        fontSize = 11.sp,
                        fontWeight = if (isFavorites) FontWeight.Bold else FontWeight.Medium,
                        color = favTabContent
                    )
                }
            }
        }
    }
}
