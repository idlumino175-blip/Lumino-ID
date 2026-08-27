package com.example.ui.components

import android.text.format.DateUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ClipCategory
import com.example.model.ClipEntity
import com.example.ui.theme.VaultTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Pastel ClipCard component matching user's exact specification:
 * - Category dot + uppercase type name
 * - Bookmark icon & 3-dot options menu
 * - Title heading (when available)
 * - Bold content text with 3-line preview
 * - Source & Relative date
 * - Dark Navy "Reuse" quick-copy button
 */
@Composable
fun ClipCard(
    clip: ClipEntity,
    onCopy: () -> Unit,
    onTogglePin: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    onOpenUrl: (String) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isJustCopied by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val isDarkTheme = isSystemInDarkTheme()
    val vc = VaultTheme.colors

    val relativeTime = remember(clip.createdAt) {
        DateUtils.getRelativeTimeSpanString(
            clip.createdAt,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()
    }

    val cardBg = if (isDarkTheme) clip.categoryEnum.bgDark else clip.categoryEnum.bgLight
    val inkColor = if (isDarkTheme) clip.categoryEnum.textDark else clip.categoryEnum.textLight
    val bodyTextColor = vc.bodyText

    val subtitleText = remember(clip.source, relativeTime) {
        if (clip.source.isNotBlank()) {
            if (clip.source.contains("ago", ignoreCase = true) || clip.source.contains("now", ignoreCase = true)) {
                clip.source
            } else {
                "${clip.source} · $relativeTime"
            }
        } else {
            relativeTime
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("clip_card_${clip.id}")
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            // Top Row: Dot + Category name, Bookmark Icon, 3-dots Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Category Dot and Label + Pinned Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = inkColor,
                        modifier = Modifier.size(7.dp)
                    ) {}
                    Text(
                        text = clip.categoryEnum.badgeLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        color = inkColor,
                        fontSize = 12.sp
                    )

                    if (clip.isPinned) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = vc.pinnedStar.copy(alpha = 0.18f),
                            border = BorderStroke(1.dp, vc.pinnedStar.copy(alpha = 0.45f)),
                            modifier = Modifier.height(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PushPin,
                                    contentDescription = null,
                                    tint = vc.pinnedStar,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = "PINNED",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp,
                                    color = vc.pinnedStar
                                )
                            }
                        }
                    }
                }

                // Actions: Pin to top toggle + 3-dots
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onTogglePin,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("pin_button_${clip.id}")
                    ) {
                        Icon(
                            imageVector = if (clip.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                            contentDescription = if (clip.isPinned) "Unpin from top" else "Pin to top (Important)",
                            tint = if (clip.isPinned) vc.pinnedStar else inkColor.copy(alpha = 0.75f),
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    Box {
                        IconButton(
                            onClick = { showMenu = true },
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("more_menu_${clip.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = "More Options",
                                tint = inkColor.copy(alpha = 0.85f),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(if (clip.isPinned) "Unpin from Top" else "Pin to Top (Important)") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (clip.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                                        contentDescription = null,
                                        tint = if (clip.isPinned) vc.pinnedStar else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onTogglePin()
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Edit Clip") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onEdit()
                                }
                            )

                            if (clip.categoryEnum == ClipCategory.LINK) {
                                DropdownMenuItem(
                                    text = { Text("Open in Browser") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.OpenInBrowser,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    },
                                    onClick = {
                                        showMenu = false
                                        onOpenUrl(clip.content)
                                    }
                                )
                            }

                            DropdownMenuItem(
                                text = { Text("Share") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onShare()
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onDelete()
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title (shown when available)
            if (clip.title.isNotBlank()) {
                Text(
                    text = clip.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = bodyTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Body Content Snippet (3-line preview preserving newlines)
            val displayPreview = remember(clip.content) {
                clip.content.trim()
            }

            Text(
                text = displayPreview,
                style = if (clip.categoryEnum == ClipCategory.CODE || clip.categoryEnum == ClipCategory.OTP) {
                    MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = if (clip.categoryEnum == ClipCategory.OTP) 1.sp else 0.sp
                    )
                } else {
                    MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        lineHeight = 21.sp
                    )
                },
                color = bodyTextColor,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Row: Source/Date + Reuse Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = subtitleText,
                    style = MaterialTheme.typography.labelSmall,
                    color = inkColor.copy(alpha = 0.8f),
                    fontSize = 11.5.sp,
                    modifier = Modifier.weight(1f, fill = false)
                )

                // "Reuse" Pill Button
                Surface(
                    onClick = {
                        onCopy()
                        isJustCopied = true
                        scope.launch {
                            delay(1400)
                            isJustCopied = false
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isJustCopied) vc.reuseBgCopied else vc.reuseBg,
                    modifier = Modifier
                        .height(30.dp)
                        .testTag("reuse_button_${clip.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isJustCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                            contentDescription = "Reuse",
                            tint = vc.reuseText,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (isJustCopied) "Copied!" else "Reuse",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = vc.reuseText
                        )
                    }
                }
            }
        }
    }
}
