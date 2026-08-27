package com.example.ui.components

import android.text.format.DateFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ClipCategory
import com.example.model.ClipEntity
import com.example.ui.theme.VaultTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClipDetailSheet(
    clip: ClipEntity?,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onCopy: () -> Unit,
    onTogglePin: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    onOpenUrl: (String) -> Unit
) {
    if (clip == null) return

    var showDeleteConfirm by remember { mutableStateOf(false) }
    val vc = VaultTheme.colors

    val createdDateStr = remember(clip.createdAt) {
        DateFormat.format("MMM dd, yyyy • h:mm a", clip.createdAt).toString()
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete this clip?") },
            text = { Text("This will permanently remove this clip from your local vault.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDelete()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = vc.destructive
                    )
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
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
            // Header Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    ClipBadge(category = clip.categoryEnum)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (clip.title.isNotBlank()) clip.title else clip.categoryEnum.displayName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onTogglePin,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (clip.isPinned) Icons.Default.Star else Icons.Outlined.StarOutline,
                            contentDescription = "Pin",
                            tint = if (clip.isPinned) vc.pinnedStar else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Full Content Box
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = vc.inputBackground,
                border = BorderStroke(1.dp, vc.inputBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = clip.content,
                        style = if (clip.categoryEnum == ClipCategory.CODE || clip.categoryEnum == ClipCategory.OTP) {
                            MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = if (clip.categoryEnum == ClipCategory.OTP) 2.sp else 0.sp
                            )
                        } else {
                            MaterialTheme.typography.bodyLarge
                        },
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Primary Copy Button
            Button(
                onClick = onCopy,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = vc.accentPrimary,
                    contentColor = vc.accentOnPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("detail_copy_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Copy to Clipboard",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Secondary Action Row (Open if Link, Share, Edit, Delete)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (clip.categoryEnum == ClipCategory.LINK) {
                    OutlinedButton(
                        onClick = { onOpenUrl(clip.content) },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f).height(42.dp)
                    ) {
                        Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Open", fontSize = 13.sp)
                    }
                }

                OutlinedButton(
                    onClick = onShare,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f).height(42.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Share", fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = onEdit,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f).height(42.dp)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit", fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = { showDeleteConfirm = true },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = vc.destructive
                    ),
                    border = BorderStroke(
                        1.dp,
                        vc.destructive.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.weight(1f).height(42.dp)
                ) {
                    Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete", fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Metadata Statistics breakdown
            Text(
                text = "Clip Metadata & Statistics",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = vc.inputBackground,
                border = BorderStroke(1.dp, vc.inputBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetaStatRow("Created", createdDateStr)
                    MetaStatRow("Character Count", "${clip.characterCount} characters")
                    MetaStatRow("Word Count", "${clip.wordCount} words")
                    MetaStatRow("Line Count", "${clip.lineCount} lines")
                    MetaStatRow("Times Copied", "${clip.copyCount} times")
                    if (clip.tags.isNotBlank()) {
                        MetaStatRow("Tags", clip.tags)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MetaStatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
