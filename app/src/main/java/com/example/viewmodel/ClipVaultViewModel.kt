package com.example.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.ClipRepository
import com.example.model.ClipCategory
import com.example.model.ClipEntity
import com.example.ui.components.VaultNavTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ClipVaultViewModel(private val repository: ClipRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    private val _currentNavTab = MutableStateFlow(VaultNavTab.HOME)
    val currentNavTab: StateFlow<VaultNavTab> = _currentNavTab.asStateFlow()

    private val _selectedCategory = MutableStateFlow<ClipCategory?>(null)
    val selectedCategory: StateFlow<ClipCategory?> = _selectedCategory.asStateFlow()

    private val _showOnlyPinned = MutableStateFlow(false)
    val showOnlyPinned: StateFlow<Boolean> = _showOnlyPinned.asStateFlow()

    private val _detectedClipboardText = MutableStateFlow<String?>(null)
    val detectedClipboardText: StateFlow<String?> = _detectedClipboardText.asStateFlow()

    private val _detailClip = MutableStateFlow<ClipEntity?>(null)
    val detailClip: StateFlow<ClipEntity?> = _detailClip.asStateFlow()

    private val _editClip = MutableStateFlow<ClipEntity?>(null)
    val editClip: StateFlow<ClipEntity?> = _editClip.asStateFlow()

    private val _isAddSheetOpen = MutableStateFlow(false)
    val isAddSheetOpen: StateFlow<Boolean> = _isAddSheetOpen.asStateFlow()

    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    private val _showOnboarding = MutableStateFlow(false)
    val showOnboarding: StateFlow<Boolean> = _showOnboarding.asStateFlow()

    private var hasCheckedOnboarding = false

    val allClips: StateFlow<List<ClipEntity>> = repository.allClips
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredClips: StateFlow<List<ClipEntity>> = combine(
        repository.allClips,
        _searchQuery,
        _selectedFilter,
        _currentNavTab
    ) { clips, query, filter, navTab ->
        clips.filter { clip ->
            // Search query filter
            val matchesQuery = query.isBlank() ||
                clip.title.contains(query, ignoreCase = true) ||
                clip.content.contains(query, ignoreCase = true) ||
                clip.tags.contains(query, ignoreCase = true) ||
                clip.source.contains(query, ignoreCase = true)

            // Filter pills: All, Work, Personal, Links
            val matchesPill = when (filter.lowercase()) {
                "work" -> clip.tags.contains("work", ignoreCase = true)
                "personal" -> clip.tags.contains("personal", ignoreCase = true)
                "links" -> clip.category == "LINK" || clip.tags.contains("links", ignoreCase = true)
                else -> true
            }

            // Bottom Nav Tab filter
            val matchesNavTab = when (navTab) {
                VaultNavTab.HOME -> true
                VaultNavTab.FAVORITES -> clip.isPinned
            }

            matchesQuery && matchesPill && matchesNavTab
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedFilter(filter: String) {
        _selectedFilter.value = filter
    }

    fun setNavTab(tab: VaultNavTab) {
        _currentNavTab.value = tab
    }

    fun setSelectedCategory(category: ClipCategory?) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
    }

    fun toggleOnlyPinned() {
        _showOnlyPinned.value = !_showOnlyPinned.value
    }

    fun openAddSheet() {
        _editClip.value = null
        _isAddSheetOpen.value = true
    }

    fun openEditSheet(clip: ClipEntity) {
        _editClip.value = clip
        _isAddSheetOpen.value = true
    }

    fun closeAddOrEditSheet() {
        _isAddSheetOpen.value = false
        _editClip.value = null
    }

    fun openDetailSheet(clip: ClipEntity) {
        _detailClip.value = clip
    }

    fun closeDetailSheet() {
        _detailClip.value = null
    }

    fun clearFeedbackMessage() {
        _feedbackMessage.value = null
    }

    fun checkOnboarding(context: Context) {
        if (!hasCheckedOnboarding) {
            hasCheckedOnboarding = true
            val prefs = context.getSharedPreferences("clips_vault_prefs", Context.MODE_PRIVATE)
            val isCompleted = prefs.getBoolean("onboarding_completed", false)
            _showOnboarding.value = !isCompleted
        }
    }

    fun completeOnboarding(context: Context) {
        val prefs = context.getSharedPreferences("clips_vault_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("onboarding_completed", true).apply()
        _showOnboarding.value = false
    }

    fun openOnboarding() {
        _showOnboarding.value = true
    }

    fun checkClipboard(context: Context) {
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            if (clipboard != null && clipboard.hasPrimaryClip()) {
                val item = clipboard.primaryClip?.getItemAt(0)
                val text = item?.text?.toString()?.trim()
                if (!text.isNullOrBlank()) {
                    val existing = allClips.value.firstOrNull()?.content?.trim()
                    if (text != existing) {
                        _detectedClipboardText.value = text
                    }
                }
            }
        } catch (_: Exception) {
            // Ignore security or permission restrictions
        }
    }

    fun saveDetectedClipboard(context: Context) {
        val text = _detectedClipboardText.value ?: return
        viewModelScope.launch {
            val detected = ClipCategory.detectCategory(text)
            val title = when (detected) {
                ClipCategory.LINK -> "Saved Web Link"
                ClipCategory.CODE -> "Code Snippet"
                ClipCategory.OTP -> "Verification Token"
                ClipCategory.EMAIL -> "Email Address"
                ClipCategory.NOTE -> "Quick Note"
                ClipCategory.TEXT -> if (text.length > 30) text.take(30) + "..." else text
            }
            val newClip = ClipEntity(
                title = title,
                content = text,
                category = detected.name,
                tags = if (detected == ClipCategory.LINK) "Links" else "Personal",
                source = "Saved just now",
                isPinned = false
            )
            repository.insert(newClip)
            _detectedClipboardText.value = null
            _feedbackMessage.value = "Saved new clip to Vault!"
            Toast.makeText(context, "Saved clip from clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    fun dismissClipboardBanner() {
        _detectedClipboardText.value = null
    }

    fun saveClip(
        title: String,
        content: String,
        category: ClipCategory,
        tags: String,
        isPinned: Boolean,
        editingId: Long? = null
    ) {
        if (content.isBlank()) return
        viewModelScope.launch {
            val cleanTitle = if (title.isBlank()) {
                if (content.length > 35) content.take(35).trim() + "..." else content.trim()
            } else {
                title.trim()
            }

            if (editingId != null && editingId > 0) {
                val existing = repository.getClipById(editingId)
                if (existing != null) {
                    val updated = existing.copy(
                        title = cleanTitle,
                        content = content,
                        category = category.name,
                        tags = tags,
                        isPinned = isPinned,
                        updatedAt = System.currentTimeMillis()
                    )
                    repository.update(updated)
                    if (_detailClip.value?.id == editingId) {
                        _detailClip.value = updated
                    }
                    _feedbackMessage.value = "Clip updated"
                }
            } else {
                val newClip = ClipEntity(
                    title = cleanTitle,
                    content = content,
                    category = category.name,
                    tags = if (tags.isBlank()) (if (category == ClipCategory.LINK) "Links" else "Personal") else tags,
                    source = "Saved just now",
                    isPinned = isPinned
                )
                repository.insert(newClip)
                _feedbackMessage.value = "Clip saved to Vault"
            }
            closeAddOrEditSheet()
        }
    }

    fun togglePin(clip: ClipEntity) {
        viewModelScope.launch {
            val newPinned = !clip.isPinned
            repository.togglePin(clip.id, newPinned)
            if (_detailClip.value?.id == clip.id) {
                _detailClip.value = _detailClip.value?.copy(isPinned = newPinned)
            }
            _feedbackMessage.value = if (newPinned) "📌 Pinned to top (Important)" else "Unpinned from top"
        }
    }

    fun deleteClip(clip: ClipEntity) {
        viewModelScope.launch {
            repository.delete(clip)
            if (_detailClip.value?.id == clip.id) {
                _detailClip.value = null
            }
            _feedbackMessage.value = "Clip removed"
        }
    }

    fun restoreClip(clip: ClipEntity) {
        viewModelScope.launch {
            repository.insert(clip)
            _feedbackMessage.value = "Clip restored"
        }
    }

    fun clearAllClips() {
        viewModelScope.launch {
            repository.clearAll()
            _detailClip.value = null
            _feedbackMessage.value = "Vault cleared"
        }
    }

    fun copyClipToClipboard(context: Context, clip: ClipEntity) {
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(clip.title, clip.content)
            clipboard.setPrimaryClip(clipData)
            viewModelScope.launch {
                repository.incrementCopyCount(clip.id)
            }
            _feedbackMessage.value = "Copied to clipboard!"
            Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
            Toast.makeText(context, "Failed to copy", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareClip(context: Context, clip: ClipEntity) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TITLE, clip.title)
            putExtra(Intent.EXTRA_TEXT, clip.content)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share clip via")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    fun openUrl(context: Context, url: String) {
        try {
            val formattedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                "https://$url"
            } else {
                url
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(formattedUrl))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(context, "Could not open link", Toast.LENGTH_SHORT).show()
        }
    }

    fun pasteFromClipboard(context: Context): String? {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            if (clipboard != null && clipboard.hasPrimaryClip()) {
                clipboard.primaryClip?.getItemAt(0)?.text?.toString()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}

class ClipVaultViewModelFactory(private val repository: ClipRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClipVaultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClipVaultViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

