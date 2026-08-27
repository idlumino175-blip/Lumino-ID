package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.BadgeCodeBg
import com.example.ui.theme.BadgeCodeText
import com.example.ui.theme.BadgeEmailBg
import com.example.ui.theme.BadgeEmailText
import com.example.ui.theme.BadgeLinkBg
import com.example.ui.theme.BadgeLinkText
import com.example.ui.theme.BadgeNoteBg
import com.example.ui.theme.BadgeNoteText
import com.example.ui.theme.BadgeOtpBg
import com.example.ui.theme.BadgeOtpText
import com.example.ui.theme.BadgeTextBg
import com.example.ui.theme.BadgeTextText

enum class ClipCategory(
    val displayName: String,
    val badgeLabel: String,
    val icon: ImageVector,
    val bgLight: Color,
    val textLight: Color
) {
    LINK(
        displayName = "Links",
        badgeLabel = "LINK",
        icon = Icons.Default.Link,
        bgLight = BadgeLinkBg,
        textLight = BadgeLinkText
    ),
    CODE(
        displayName = "Code",
        badgeLabel = "CODE",
        icon = Icons.Default.Code,
        bgLight = BadgeCodeBg,
        textLight = BadgeCodeText
    ),
    OTP(
        displayName = "OTPs & Keys",
        badgeLabel = "OTP",
        icon = Icons.Default.Key,
        bgLight = BadgeOtpBg,
        textLight = BadgeOtpText
    ),
    EMAIL(
        displayName = "Emails",
        badgeLabel = "EMAIL",
        icon = Icons.Default.Email,
        bgLight = BadgeEmailBg,
        textLight = BadgeEmailText
    ),
    NOTE(
        displayName = "Notes",
        badgeLabel = "NOTE",
        icon = Icons.Default.Notes,
        bgLight = BadgeNoteBg,
        textLight = BadgeNoteText
    ),
    TEXT(
        displayName = "General",
        badgeLabel = "TEXT",
        icon = Icons.Default.TextFields,
        bgLight = BadgeTextBg,
        textLight = BadgeTextText
    );

    companion object {
        fun detectCategory(content: String): ClipCategory {
            val trimmed = content.trim()
            if (trimmed.isEmpty()) return TEXT

            // Check URL
            if (trimmed.startsWith("http://", ignoreCase = true) ||
                trimmed.startsWith("https://", ignoreCase = true) ||
                trimmed.startsWith("ftp://", ignoreCase = true) ||
                trimmed.matches(Regex("^(www\\.)?[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}(/\\S*)?$"))
            ) {
                return LINK
            }

            // Check OTP / Verification Code (4-8 digits or alphanumeric code like "123456" or "G-839201")
            if (trimmed.matches(Regex("^[0-9]{4,8}$")) ||
                trimmed.matches(Regex("^[A-Z0-9]{3,4}-[A-Z0-9]{3,4}$")) ||
                (trimmed.length in 4..10 && trimmed.all { it.isDigit() || it.isWhitespace() } && trimmed.count { it.isDigit() } in 4..8)
            ) {
                return OTP
            }

            // Check Email
            if (trimmed.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))) {
                return EMAIL
            }

            // Check Code
            val codeKeywords = listOf(
                "function", "const ", "let ", "var ", "val ", "def ", "class ", "import ",
                "export ", "return ", "console.log", "public static", "void ", "SELECT ",
                "FROM ", "WHERE ", "git ", "npm ", "docker ", "curl ", "sudo ", "kubectl",
                "fun ", "override ", "<?xml", "<html>", "<div", "{}", "=>"
            )
            val hasCodeKeyword = codeKeywords.any { trimmed.contains(it) }
            val hasCodeStructure = (trimmed.contains("{") && trimmed.contains("}")) ||
                (trimmed.contains("(") && trimmed.contains(")") && trimmed.contains(";")) ||
                (trimmed.lines().size > 1 && (trimmed.contains("    ") || trimmed.contains("\t")))

            if (hasCodeKeyword || hasCodeStructure) {
                return CODE
            }

            // Check Note
            if (trimmed.startsWith("TODO", ignoreCase = true) ||
                trimmed.startsWith("Note:", ignoreCase = true) ||
                trimmed.startsWith("- [ ]") ||
                trimmed.lines().size >= 3
            ) {
                return NOTE
            }

            return TEXT
        }
    }
}
