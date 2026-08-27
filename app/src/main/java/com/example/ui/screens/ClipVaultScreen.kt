package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.ClipEntity
import com.example.ui.components.AddClipSheet
import com.example.ui.components.BottomNavDock
import com.example.ui.components.ClipCard
import com.example.ui.components.ClipDetailSheet
import com.example.ui.components.ClipboardBanner
import com.example.ui.components.EmptyVaultState
import com.example.ui.components.TopVaultHeader
import com.example.viewmodel.ClipVaultViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClipVaultScreen(
    viewModel: ClipVaultViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val allClips by viewModel.allClips.collectAsStateWithLifecycle()
    val filteredClips by viewModel.filteredClips.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val currentNavTab by viewModel.currentNavTab.collectAsStateWithLifecycle()
    val detectedClipboardText by viewModel.detectedClipboardText.collectAsStateWithLifecycle()
    val isAddSheetOpen by viewModel.isAddSheetOpen.collectAsStateWithLifecycle()
    val editingClip by viewModel.editClip.collectAsStateWithLifecycle()
    val detailClip by viewModel.detailClip.collectAsStateWithLifecycle()
    val feedbackMessage by viewModel.feedbackMessage.collectAsStateWithLifecycle()
    val showOnboarding by viewModel.showOnboarding.collectAsStateWithLifecycle()

    val addSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val detailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Check onboarding status on launch
    LaunchedEffect(Unit) {
        viewModel.checkOnboarding(context)
    }

    // Check system clipboard on app launch and resume
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.checkClipboard(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Feedback Snackbar Trigger
    LaunchedEffect(feedbackMessage) {
        feedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearFeedbackMessage()
        }
    }

    if (showOnboarding) {
        OnboardingScreen(
            onFinish = { viewModel.completeOnboarding(context) }
        )
        return
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.navigationBarsPadding()
            )
        },
        bottomBar = {
            BottomNavDock(
                currentTab = currentNavTab,
                onSelectTab = { viewModel.setNavTab(it) },
                modifier = Modifier.navigationBarsPadding()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Header (Clips logo, Add (+) button, Search, Filter Pills All 3/Work/Personal/Links, Recently saved)
            TopVaultHeader(
                searchQuery = searchQuery,
                onSearchChange = { viewModel.setSearchQuery(it) },
                selectedFilter = selectedFilter,
                onSelectFilter = { viewModel.setSelectedFilter(it) },
                clips = allClips,
                filteredCount = filteredClips.size,
                onAddNewClip = { viewModel.openAddSheet() },
                onOpenOnboarding = { viewModel.openOnboarding() }
            )

            // Smart Clipboard Banner (if new un-saved clip detected)
            ClipboardBanner(
                detectedText = detectedClipboardText,
                onSave = { viewModel.saveDetectedClipboard(context) },
                onDismiss = { viewModel.dismissClipboardBanner() }
            )

            // Content List or Empty State
            if (filteredClips.isEmpty()) {
                EmptyVaultState(
                    isSearching = searchQuery.isNotBlank() || selectedFilter != "All" || currentNavTab == com.example.ui.components.VaultNavTab.FAVORITES,
                    searchQuery = searchQuery,
                    onClearSearch = {
                        viewModel.setSearchQuery("")
                        viewModel.setSelectedFilter("All")
                        if (currentNavTab == com.example.ui.components.VaultNavTab.FAVORITES) {
                            viewModel.setNavTab(com.example.ui.components.VaultNavTab.HOME)
                        }
                    },
                    onAddClip = { viewModel.openAddSheet() },
                    onPasteClip = {
                        val pasted = viewModel.pasteFromClipboard(context)
                        if (!pasted.isNullOrBlank()) {
                            val detected = com.example.model.ClipCategory.detectCategory(pasted)
                            viewModel.saveClip(
                                title = "",
                                content = pasted,
                                category = detected,
                                tags = if (detected == com.example.model.ClipCategory.LINK) "Links" else "Personal",
                                isPinned = false
                            )
                        } else {
                            viewModel.openAddSheet()
                        }
                    },
                    onOpenTour = { viewModel.openOnboarding() },
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = filteredClips,
                        key = { it.id }
                    ) { clip ->
                        SwipeableClipItem(
                            clip = clip,
                            onCopy = { viewModel.copyClipToClipboard(context, clip) },
                            onTogglePin = { viewModel.togglePin(clip) },
                            onEdit = { viewModel.openEditSheet(clip) },
                            onDelete = {
                                val removedClip = clip
                                viewModel.deleteClip(clip)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Clip deleted",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.restoreClip(removedClip)
                                    }
                                }
                            },
                            onShare = { viewModel.shareClip(context, clip) },
                            onOpenUrl = { url -> viewModel.openUrl(context, url) },
                            onClick = { viewModel.openDetailSheet(clip) },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }

    // Add / Edit Modal Bottom Sheet
    if (isAddSheetOpen) {
        AddClipSheet(
            sheetState = addSheetState,
            editingClip = editingClip,
            onDismiss = { viewModel.closeAddOrEditSheet() },
            onPasteFromClipboard = { viewModel.pasteFromClipboard(context) },
            onSave = { title, content, category, tags, isPinned, editingId ->
                viewModel.saveClip(
                    title = title,
                    content = content,
                    category = category,
                    tags = tags,
                    isPinned = isPinned,
                    editingId = editingId
                )
            }
        )
    }

    // Detail Inspection Modal Bottom Sheet
    if (detailClip != null) {
        ClipDetailSheet(
            clip = detailClip,
            sheetState = detailSheetState,
            onDismiss = { viewModel.closeDetailSheet() },
            onCopy = { detailClip?.let { viewModel.copyClipToClipboard(context, it) } },
            onTogglePin = { detailClip?.let { viewModel.togglePin(it) } },
            onEdit = {
                val toEdit = detailClip
                viewModel.closeDetailSheet()
                if (toEdit != null) {
                    viewModel.openEditSheet(toEdit)
                }
            },
            onDelete = {
                val toDel = detailClip
                if (toDel != null) {
                    viewModel.deleteClip(toDel)
                }
            },
            onShare = { detailClip?.let { viewModel.shareClip(context, it) } },
            onOpenUrl = { url -> viewModel.openUrl(context, url) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableClipItem(
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
    val haptic = LocalHapticFeedback.current
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart || dismissValue == SwipeToDismissBoxValue.StartToEnd) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            val direction = dismissState.dismissDirection
            val alignment = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                else -> Alignment.CenterEnd
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFE2594D))
                    .padding(horizontal = 24.dp),
                contentAlignment = alignment
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "Delete",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        },
        modifier = modifier
    ) {
        ClipCard(
            clip = clip,
            onCopy = onCopy,
            onTogglePin = onTogglePin,
            onEdit = onEdit,
            onDelete = onDelete,
            onShare = onShare,
            onOpenUrl = onOpenUrl,
            onClick = onClick
        )
    }
}

