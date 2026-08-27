package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ClipCategory
import com.example.model.ClipEntity
import com.example.ui.theme.VaultTheme

/**
 * Add / Edit Clip Sheet meeting Section 5.7 specifications:
 * 24dp top corner radius, comfortable spacing, smart category detection, and one-click save.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClipSheet(
    sheetState: SheetState,
    editingClip: ClipEntity?,
    onDismiss: () -> Unit,
    onPasteFromClipboard: () -> String?,
    onSave: (title: String, content: String, category: ClipCategory, tags: String, isPinned: Boolean, editingId: Long?) -> Unit
) {
    var title by remember { mutableStateOf(editingClip?.title ?: "") }
    var content by remember { mutableStateOf(editingClip?.content ?: "") }
    var tags by remember { mutableStateOf(editingClip?.tags ?: "") }
    var isPinned by remember { mutableStateOf(editingClip?.isPinned ?: false) }
    var manualCategory by remember { mutableStateOf<ClipCategory?>(editingClip?.categoryEnum) }

    val autoDetectedCategory = remember(content) {
        ClipCategory.detectCategory(content)
    }

    val finalCategory = manualCategory ?: autoDetectedCategory
    val isDarkTheme = isSystemInDarkTheme()
    val vc = VaultTheme.colors

    LaunchedEffect(editingClip) {
        if (editingClip != null) {
            title = editingClip.title
            content = editingClip.content
            tags = editingClip.tags
            isPinned = editingClip.isPinned
            manualCategory = editingClip.categoryEnum
        } else {
            title = ""
            content = ""
            tags = ""
            isPinned = false
            manualCategory = null
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = vc.sheetBackground,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (editingClip != null) "Edit Clip" else "Save to Vault",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Encrypted in local offline storage",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Clip Content Area & Quick Paste Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Clip Content",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Surface(
                    onClick = {
                        val pasted = onPasteFromClipboard()
                        if (!pasted.isNullOrBlank()) {
                            content = pasted
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    color = vc.accentSecondary,
                    modifier = Modifier.height(28.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentPaste,
                            contentDescription = null,
                            tint = vc.accentSecondaryText,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Paste clipboard",
                            style = MaterialTheme.typography.labelSmall,
                            color = vc.accentSecondaryText,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .testTag("add_clip_content_input"),
                placeholder = {
                    Text(
                        "Paste or type text, link, code snippet, OTP token, or note...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = vc.inputBackground,
                    unfocusedContainerColor = vc.inputBackground,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = vc.inputBorder
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Title Field
            Text(
                text = "Title (Optional)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_clip_title_input"),
                placeholder = {
                    Text(
                        "e.g. Wi-Fi Password, Git command, Project doc",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = vc.inputBackground,
                    unfocusedContainerColor = vc.inputBackground,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = vc.inputBorder
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Category Selector
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Category Format",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (manualCategory == null && content.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFF7B98D),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Auto-detected as ${autoDetectedCategory.badgeLabel}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ClipCategory.entries.forEach { cat ->
                    val isSelected = finalCategory == cat
                    Surface(
                        onClick = { manualCategory = cat },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) {
                            if (isDarkTheme) cat.bgDark else cat.bgLight
                        } else {
                            vc.inputBackground
                        },
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) {
                                vc.accentPrimary
                            } else {
                                vc.inputBorder
                            }
                        ),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = cat.icon,
                                contentDescription = null,
                                tint = if (isSelected) { if (isDarkTheme) cat.textDark else cat.textLight } else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = cat.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) { if (isDarkTheme) cat.textDark else cat.textLight } else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tags Input
            Text(
                text = "Tags",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = tags,
                onValueChange = { tags = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "e.g. Work, Personal, Project-X",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = vc.inputBackground,
                    unfocusedContainerColor = vc.inputBackground,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = vc.inputBorder
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Pin to top toggle
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = vc.inputBackground,
                border = BorderStroke(1.dp, vc.inputBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (isPinned) vc.pinnedStar else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pin to top of vault",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Switch(
                        checked = isPinned,
                        onCheckedChange = { isPinned = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = vc.accentPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Save Action Button
            Button(
                onClick = {
                    if (content.isNotBlank()) {
                        onSave(title, content, finalCategory, tags, isPinned, editingClip?.id)
                    }
                },
                enabled = content.isNotBlank(),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = vc.accentPrimary,
                    contentColor = vc.accentOnPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_clip_submit_btn")
            ) {
                Text(
                    text = if (editingClip != null) "Update Clip" else "Save Clip to Vault",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
