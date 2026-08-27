package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ClipEntity
import com.example.ui.theme.VaultTheme

@Composable
fun TopVaultHeader(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedFilter: String, // "All", "Work", "Personal", "Links"
    onSelectFilter: (String) -> Unit,
    clips: List<ClipEntity>,
    filteredCount: Int,
    onAddNewClip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalCount = clips.size
    val vc = VaultTheme.colors

    val workCount = clips.count { it.tags.contains("Work", ignoreCase = true) }
    val personalCount = clips.count { it.tags.contains("Personal", ignoreCase = true) }
    val linksCount = clips.count { it.category == "LINK" || it.tags.contains("Links", ignoreCase = true) }

    val filterList = listOf(
        FilterTab("All", totalCount),
        FilterTab("Work", workCount),
        FilterTab("Personal", personalCount),
        FilterTab("Links", linksCount)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        // App Title & Add (+) Button Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Icon + "Clips" Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Squircle App Icon with Folded Paper
                Surface(
                    shape = RoundedCornerShape(13.dp),
                    color = vc.accentPrimary,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Canvas(modifier = Modifier.size(24.dp)) {
                            val w = size.width
                            val h = size.height
                            // Soft warm peach paper sheet
                            val paperPath = Path().apply {
                                moveTo(w * 0.15f, h * 0.15f)
                                lineTo(w * 0.65f, h * 0.15f)
                                lineTo(w * 0.85f, h * 0.40f)
                                lineTo(w * 0.85f, h * 0.88f)
                                lineTo(w * 0.15f, h * 0.88f)
                                close()
                            }
                            drawPath(paperPath, color = Color(0xFFF8BA8E))

                            // Folded flap
                            val flapPath = Path().apply {
                                moveTo(w * 0.65f, h * 0.15f)
                                lineTo(w * 0.65f, h * 0.40f)
                                lineTo(w * 0.85f, h * 0.40f)
                                close()
                            }
                            drawPath(flapPath, color = Color(0xFFE29E6F))
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Clips",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Add (+) Button
            Surface(
                onClick = onAddNewClip,
                shape = RoundedCornerShape(13.dp),
                color = vc.accentPrimary,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("top_add_clip_button")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Clip",
                        tint = vc.accentOnPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar Input (18dp corner radius)
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("vault_search_input"),
            placeholder = {
                Text(
                    text = "Search by keyword or content...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = vc.mutedText,
                    fontSize = 14.5.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = if (searchQuery.isNotEmpty()) vc.accentPrimary else vc.mutedText,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { onSearchChange("") },
                        modifier = Modifier.testTag("clear_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = vc.mutedText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                imeAction = androidx.compose.ui.text.input.ImeAction.Search
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = vc.cardSurface,
                unfocusedContainerColor = vc.cardSurface,
                focusedBorderColor = vc.inputBorderFocused,
                unfocusedBorderColor = vc.inputBorder
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Filter Pills (All 3, Work, Personal, Links)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            filterList.forEach { tab ->
                val isSelected = selectedFilter.equals(tab.name, ignoreCase = true)

                val bg = if (isSelected) vc.accentPrimary else vc.accentSecondary
                val textColor = if (isSelected) vc.accentOnPrimary else vc.accentSecondaryText

                Surface(
                    onClick = { onSelectFilter(tab.name) },
                    shape = RoundedCornerShape(14.dp),
                    color = bg,
                    modifier = Modifier.height(34.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isSelected) "${tab.name} ${tab.count}" else tab.name,
                            style = MaterialTheme.typography.labelMedium,
                            color = textColor,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Section Title: "Recently saved  3 clips"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Recently saved",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$filteredCount clips",
                style = MaterialTheme.typography.bodyMedium,
                color = vc.mutedText,
                fontSize = 13.sp
            )
        }
    }
}

private data class FilterTab(val name: String, val count: Int)
